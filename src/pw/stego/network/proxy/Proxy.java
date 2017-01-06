package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.NoStego;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.steganography.Stego;
import pw.stego.network.container.util.ContainerFactory;
import pw.stego.network.proxy.tunnel.Tunnel;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * Proxy class
 * Created by lina on 06.01.17.
 */
public abstract class Proxy {
    private static final int BUFF_SIZE = 2048;
    private static final String[] params = new String[]{"", ""};

    static {
        Random r = new Random();

        int dim = (int) Math.sqrt(BUFF_SIZE * 4);
        dim += r.nextInt(dim);

        params[0] = String.valueOf(dim);
        params[1] = String.valueOf(BUFF_SIZE * 4 / dim + r.nextInt(dim));
    }

    private final ContainerFactory factory;
    final ServerSocket acceptor;

    final String ipOut;
    final int portOut;

    Proxy(ContainerFactory factory, ServerSocket acceptor, String ipOut, int portOut) {
        this.factory = factory;
        this.acceptor = acceptor;
        this.ipOut = ipOut;
        this.portOut = portOut;
    }

    /**
     * Accepts client with algo and sign
     * @param algo to use in communication
     * @param sign to use to extract data
     */
    public abstract void accept(Steganography algo, Sign sign) throws IOException;

    /**
     * Accepting clients forever
     * @param algoName algorithm name string
     * @param sign to use to extract data
     * @throws IOException on accept failure
     */
    void start(String algoName, Sign sign) throws IOException {
        algoName = algoName.toLowerCase();

        while (true) {
            Steganography algo;
            switch (algoName) {
                case "stego":
                    algo = new Stego();
                    break;

                case "nostego":
                    algo = new NoStego();
                    break;

                default:
                    return;
            }

            accept(algo, sign);
        }
    }

    abstract class ProxyWorker extends Thread {
        final Socket s;
        final Tunnel t;

        ProxyWorker(Socket s, Tunnel t) {
            this.s = s;
            this.t = t;
        }

        void translate(InputStream from, Tunnel to) throws IOException {
            final byte[] request = new byte[BUFF_SIZE];

            int count;
            while ((count = from.read(request)) != -1) {
                to.addContainers(factory.getContainer(params));

                if (count < request.length) {
                    byte[] msg = new byte[count];
                    System.arraycopy(request, 0, msg, 0, count);

                    to.send(msg);
                } else to.send(request);
            }
        }
    }
}
