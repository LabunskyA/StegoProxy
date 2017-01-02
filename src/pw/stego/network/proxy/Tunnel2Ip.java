package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.steganography.Stego;
import pw.stego.network.container.steganography.StegoSynch;
import pw.stego.network.proxy.tunnel.SocketTunnel;
import pw.stego.network.proxy.tunnel.Tunnel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to redirect traffic from tunnel to host
 * Created by lina on 26.12.16.
 */
public class Tunnel2Ip {
    private ServerSocket tunneller;
    private String ipOut;
    private int portOut;

    Tunnel2Ip(String ipOut, int portOut, int portIn) throws IOException {
        this.tunneller = new ServerSocket(portIn);
        this.ipOut = ipOut;
        this.portOut = portOut;
    }

    public void accept(Steganography algo, Sign sign) throws IOException {
        Tunnel tunnel = new SocketTunnel<>(sign, algo, tunneller.accept());
        tunnel.addContainers(new File("che2.png"));

        new ProxyWorker(new Socket(ipOut, portOut), tunnel).start();
    }

    private class ProxyWorker extends Thread {
        private final Socket out;
        private final Tunnel in;

        ProxyWorker(Socket out, Tunnel in) {
            this.out = out;
            this.in = in;
        }

        @Override
        public void run() {
            try {
                final InputStream inS = out.getInputStream();
                final OutputStream outS = out.getOutputStream();

                // a new thread for uploading to the out
                new Thread(() -> {
                    byte[] reply;

                    try {
                        while ((reply = in.receive()).length >= 0) {
                            outS.write(reply, 0, reply.length);
                            outS.flush();
                        }
                    } catch (IOException ignored) {}

                    stop();
                }).start();

                // current thread manages streams from out to out (DOWNLOAD)
                try {
                    final byte[] request = new byte[1024];

                    int count;
                    while ((count = inS.read(request)) != -1) {
                        byte[] msg = new byte[count];
                        System.arraycopy(request, 0, msg, 0, count);

                        in.send(msg);
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
        Tunnel2Ip translator = new Tunnel2Ip("localhost", 3128, 6001);
        while (true)
            translator.accept(new Stego(), new Sign("labunsky".getBytes()));
    }
}
