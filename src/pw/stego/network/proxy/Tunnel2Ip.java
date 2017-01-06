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
 * Class to redirect traffic from tunnel to host
 * Created by lina on 26.12.16.
 */
public class Tunnel2Ip extends Proxy {
    Tunnel2Ip(String ipOut, int portOut, int portIn, ContainerFactory factory) throws IOException {
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
        ContainerFactory factory;
        switch (args[4]) {
            case "RandomPNGFactory":
                factory = new RandomPNGFactory();
                break;

            default:
                return;
        }

        Proxy proxy = new Tunnel2Ip(
                args[0],
                Integer.parseInt(args[1]),
                Integer.parseInt(args[2]),
                factory
        );

        System.out.print("Enter password: ");
        proxy.start(args[3], new Sign(new String(System.console().readPassword()).getBytes()));
    }
}
