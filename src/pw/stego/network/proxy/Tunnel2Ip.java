package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.factory.ContainerFactory;
import pw.stego.network.proxy.tunnel.SocketTunnel;
import pw.stego.network.proxy.tunnel.Tunnel;
import pw.stego.network.proxy.util.CLI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to redirect traffic from tunnel to host
 * Created by lina on 26.12.16.
 */
public class Tunnel2Ip extends Proxy {
    public Tunnel2Ip(String ipOut, int portOut, int portIn, ContainerFactory factory) throws IOException {
        super(factory, new ServerSocket(portIn), ipOut, portOut);
    }

    @Override
    public void accept(Steganography algo, Sign sign) throws IOException {
        new Worker(
                new Socket(ipOut, portOut),
                new SocketTunnel<>(sign, algo, acceptor.accept())
        ).start();
    }

    private class Worker extends ProxyWorker {
        Worker(Socket out, Tunnel in) {
            super(out, in);
        }

        @Override
        public void run() {
            try {
                final InputStream inS = s.getInputStream();
                final OutputStream outS = s.getOutputStream();

                // a new thread for uploading to the out
                new Thread(() -> {
                    try {
                        byte[] reply;
                        while ((reply = t.receive()).length >= 0) {
                            outS.write(reply, 0, reply.length);
                            outS.flush();
                        }
                    } catch (IOException ignored) {}
                }).start();


                try {
                    translate(inS, t);
                } catch (IOException ignored) {} finally {
                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                t.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 4) {
            System.out.println("Usage: \"java -jar StegoServer.jar " +
                    "[port to accept connections] " +
                    "[real proxy server address] " +
                    "[steganography algorithm] " +
                    "[container factory]\"");
            System.out.println();

            CLI.help();

            System.out.println();
            System.out.println("Example: \"java -jar StegoServer.jar 6000 192.168.1.1:6000 stego lorempixelpng\"");
            System.out.println("It will launch steganography proxy port 6000 to real proxy server on 192.168.1.1 " +
                    "on port 6000 and will use Stego algorithm with lorempixel's png as containers");

            return;
        }

        ContainerFactory factory = CLI.getContainerFactory(args[3]);
        if (factory == null)
            return;

        String[] proxyAddr = args[1].split(":");
        Proxy proxy = new Tunnel2Ip(
                proxyAddr[0],
                Integer.parseInt(proxyAddr[1]),
                Integer.parseInt(args[0]),
                factory
        );

        System.out.print("Enter password: ");
        proxy.start(args[2], new Sign(new String(System.console().readPassword()).getBytes()));
    }
}
