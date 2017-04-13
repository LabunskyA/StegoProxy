package pw.stego.network.proxy.tunnel;

import pw.stego.network.container.Container;
import pw.stego.network.container.Sign;
import pw.stego.network.steganography.Steganography;

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

    private Queue<Container> pool = new LinkedList<>();

    Tunnel(Sign sign, T connection, S algo) {
        this.sign = sign;
        this.connection = connection;

        this.algo = algo;
    }

    abstract public void send(byte[] data) throws IOException;
    abstract public byte[] receive() throws IOException;
    abstract public void close() throws IOException;

    Container getNextContainer() {
        if (pool.size() == 0)
            throw new ArrayIndexOutOfBoundsException("No containers provided");

        return pool.poll();
    }

    public void addContainers(Container... files) throws IOException {
        for (Container c : files)
            if (algo.isAcceptableContainer(c))
                pool.add(c);
    }

    public int getPoolSize() {
        return pool.size();
    }
    public Steganography getAlgorithm() {
        return algo;
    }

    @Override
    public String toString() {
        return connection.toString();
    }
}
