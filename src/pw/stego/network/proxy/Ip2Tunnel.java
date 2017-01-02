package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.steganography.Stego;
import pw.stego.network.proxy.tunnel.SocketTunnel;
import pw.stego.network.proxy.tunnel.Tunnel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to redirect traffic from port to tunnel
 * Created by lina on 22.12.16.
 */
public class Ip2Tunnel {
    private ServerSocket acceptor;

    private String ipTo;
    private int portTo;

    Ip2Tunnel(int portIn, String ipTo, int portTo) throws IOException {
        this.acceptor = new ServerSocket(portIn);
        this.ipTo = ipTo;
        this.portTo = portTo;
    }

    public void accept(Steganography algo, Sign sign) throws IOException {
        Socket client = acceptor.accept();

        Tunnel tunnel = new SocketTunnel<>(sign, algo, new Socket(ipTo, portTo));
        tunnel.addContainers(new File("che1.png"));

        new ProxyWorker(client, tunnel).start();
    }

    private class ProxyWorker extends Thread {
        private final Socket in;
        private final Tunnel out;

        ProxyWorker(Socket in, Tunnel out) {
            this.in = in;
            this.out = out;
        }

        @Override
        public void run() {
            final byte[] request = new byte[1024];
            byte[] reply;

            try {
                final InputStream inS = in.getInputStream();
                final OutputStream outS = in.getOutputStream();

                // a new thread for uploading to the in
                new Thread(() -> {
                    try {
                        int count;
                        while ((count = inS.read(request)) != -1) {
                            byte[] msg = new byte[count];
                            System.arraycopy(request, 0, msg, 0, count);

                            out.send(msg);
                        }
                    } catch (IOException ignored) {}

                    stop();
                }).start();

                // current thread manages streams from in to in (DOWNLOAD)
                try {
                    while ((reply = out.receive()).length >= 0) {
                        outS.write(reply, 0, reply.length);
                        outS.flush();
                    }
                } catch (IOException ignored) {} finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            stop();
        }
    }

    public static void main(String[] args) throws IOException {
        Ip2Tunnel translator = new Ip2Tunnel(6000, "localhost", 6001);
        while (true)
            translator.accept(new Stego(), new Sign("labunsky".getBytes()));
    }
}
