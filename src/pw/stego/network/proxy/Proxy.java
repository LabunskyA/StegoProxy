package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.container.factory.ContainerFactory;
import pw.stego.network.container.steganography.BarcodeStego;
import pw.stego.network.container.steganography.NoStego;
import pw.stego.network.container.steganography.Steganography;
import pw.stego.network.container.steganography.Stego;
import pw.stego.network.proxy.tunnel.Tunnel;
import pw.stego.network.proxy.util.CLI;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Proxy class
 * Created by lina on 06.01.17.
 */
public abstract class Proxy {
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

        System.out.println(
                "["+ CLI.getTimestamp()+"] " +
                "Starting proxy on port " + acceptor.getLocalPort() + " " +
                "to " + ipOut+":"+portOut+" " +
                "using " + factory + " factory " +
                "and " + algoName + " steganography algorithm"
        );

        while (true) {
            Steganography algo;
            switch (algoName) {
                case "stego":
                    algo = new Stego();
                    break;

                case "nostego":
                    algo = new NoStego();
                    break;

                case "barcodestego":
                    algo = new BarcodeStego();
                    break;

                default:
                    return;
            }

            accept(algo, sign);
        }
    }

    abstract class ProxyWorker extends Thread {
        private static final int BUFF_SIZE = 2048;

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
                if (count == request.length) {
                    to.addContainers(factory.createContainer(to.getAlgorithm().getOptimalContainerParams(request)));
                    to.send(request);

                } else {

                    byte[] msg = new byte[count];
                    System.arraycopy(request, 0, msg, 0, count);

                    to.addContainers(factory.createContainer(to.getAlgorithm().getOptimalContainerParams(msg)));
                    to.send(msg);
                }
            }
        }
    }
}
