package pw.stego.network.proxy.tunnel;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * StegoTunnel
 * T is tunneling connection
 * S in steganography algorighm
 * Created by lina on 20.12.16.
 */
public abstract class Tunnel<T, S extends Steganography> {
    final Sign sign;

    final T connection;
    final S algo;

    private Queue<File> filePool = new LinkedList<>();

    Tunnel(Sign sign, T connection, S algo) {
        this.sign = sign;
        this.connection = connection;
        this.algo = algo;
    }

    abstract public void send(byte[] data) throws IOException;
    abstract public byte[] receive() throws IOException;
    abstract public void close() throws IOException;

    File getNextContainer() {
        if (filePool.size() == 0)
            throw new ArrayIndexOutOfBoundsException("No containers provided");

        return filePool.poll();
    }

    public void addContainers(File... files) {
        for (File file : files)
            if (algo.isAcceptableContainer(file))
                filePool.add(file);
    }

    public int getPoolSize() {
        return filePool.size();
    }
}
