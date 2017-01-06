package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.util.ContainerFactory;
import pw.stego.network.proxy.tunnel.SocketTunnel;
import pw.stego.network.proxy.tunnel.Tunnel;
import pw.stego.network.proxy.util.CLI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to redirect traffic from port to tunnel
 * Created by lina on 22.12.16.
 */
public class Ip2Tunnel extends Proxy {
    public Ip2Tunnel(int portIn, ContainerFactory factory, String ipTo, int portTo) throws IOException {
        super(factory, new ServerSocket(portIn), ipTo, portTo);
    }

    @Override
    public void accept(Steganography algo, Sign sign) throws IOException {
        new Worker(
                acceptor.accept(),
                new SocketTunnel<>(sign, algo, new Socket(ipOut, portOut))
        ).start();
    }

    private class Worker extends ProxyWorker {
        Worker(Socket in, Tunnel out) {
            super(in, out);
        }

        @Override
        public void run() {
            try {
                final InputStream inS = s.getInputStream();
                final OutputStream outS = s.getOutputStream();

                new Thread(() -> {
                    try {
                        translate(inS, t);
                    } catch (IOException ignored) {}
                }).start();

                try {
                    byte[] reply;
                    while ((reply = t.receive()).length >= 0) {
                        outS.write(reply, 0, reply.length);
                        outS.flush();
                    }
                } catch (IOException ignored) {} finally {
                    try {
                        t.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("Usage: \"java -jar StegoClient.jar " +
                    "[port to accept connections] " +
                    "[steganography proxy server address] " +
                    "[steganography algorithm]" +
                    "[container factory]\"");
            System.out.println();

            CLI.help();

            System.out.println();
            System.out.println("Example: \"java -jar StegoClient.jar 6000 192.168.1.1:6000 stego randompngfactory\"");
            System.out.println("It will launch proxy on port 6000 to steganography proxy server on 192.168.1.1 on port 6000");
        }

        ContainerFactory factory = CLI.getContainerFactory(args[3]);
        if (factory == null)
            return;

        String[] proxyAddr = args[1].split(":");
        Proxy proxy = new Ip2Tunnel(
                Integer.parseInt(args[0]),
                factory,
                proxyAddr[0],
                Integer.parseInt(proxyAddr[1])
        );

        System.out.print("Enter password: ");
        proxy.start(args[2], new Sign(new String(System.console().readPassword()).getBytes()));
    }
}
