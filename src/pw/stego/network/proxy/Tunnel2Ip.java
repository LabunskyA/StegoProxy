package pw.stego.network.proxy;

import pw.stego.network.container.Sign;
import pw.stego.network.steganography.Steganography;
import pw.stego.network.container.factory.ContainerFactory;
import pw.stego.network.proxy.tunnel.SocketTunnel;
import pw.stego.network.proxy.tunnel.Tunnel;
import pw.stego.network.proxy.util.CLI;
import pw.stego.network.proxy.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

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

        System.out.println("["+CLI.getTimestamp()+"] New incoming connection accepted!");
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

            System.out.println("["+CLI.getTimestamp()+"] Closed connection on " + t);
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length > 1) for (String arg : args)
            if (CLI.process(arg))
                return;

        Path confPath;
        if (args.length == 1) {
            confPath = Paths.get(args[0]);
        } else {
            String localPath = Tunnel2Ip.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            localPath = localPath.substring(0, localPath.lastIndexOf("/") + 1);

            confPath = Paths.get(localPath + "config/Server.conf");
        }

        Config config = new Config(confPath);

        ContainerFactory factory = Config.factoryByName(config.getValue(Config.FACTORY));
        if (factory == null)
            return;

        String[] proxyAddr = config.getValue(Config.DESTINATION).split(":");
        Proxy proxy = new Tunnel2Ip(
                proxyAddr[0],
                Integer.parseInt(proxyAddr[1]),
                Integer.parseInt(config.getValue(Config.ACCEPTOR)),
                factory
        );

        System.out.print("Enter password: ");
        proxy.start(config.getValue(Config.ALGO), new Sign(new String(System.console().readPassword()).getBytes()));
    }
}
