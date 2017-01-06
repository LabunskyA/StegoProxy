package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.util.ContainerFactory;
import pw.stego.network.container.util.RandomPNGFactory;
import pw.stego.network.proxy.tunnel.SocketTunnel;
import pw.stego.network.proxy.tunnel.Tunnel;

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
    Ip2Tunnel(int portIn, ContainerFactory factory, String ipTo, int portTo) throws IOException {
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
        ContainerFactory factory;
        switch (args[4]) {
            case "RandomPNGFactory":
                factory = new RandomPNGFactory();
                break;

            default:
                return;
        }

        Proxy proxy = new Ip2Tunnel(
                Integer.parseInt(args[0]),
                factory,
                args[1],
                Integer.parseInt(args[2])
        );

        System.out.print("Enter password: ");
        proxy.start(args[3], new Sign(new String(System.console().readPassword()).getBytes()));
    }
}
