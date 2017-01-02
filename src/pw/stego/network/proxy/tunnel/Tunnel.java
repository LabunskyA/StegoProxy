package pw.stego.network.proxy.tunnel;

import pw.stego.network.container.Sign;
import pw.stego.network.container.steganography.Steganography;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * StegoTunnel
 * T is tunneling connection
 * S in steganography algorighm
 * Created by lina on 20.12.16.
 */
public abstract class Tunnel<T, S extends Steganography> {
    protected final Sign sign;

    protected final T connection;
    protected final S algo;

    private int cursor = 0;
    private List<File> filePool = new LinkedList<>();

    protected Tunnel(Sign sign, T connection, S algo) {
        this.sign = sign;
        this.connection = connection;
        this.algo = algo;
    }

    abstract public void send(byte[] data) throws IOException;
    abstract public byte[] receive() throws IOException;
    abstract public void close() throws IOException;

    protected File getNextContainer() {
        if (filePool.size() == 0)
            throw new ArrayIndexOutOfBoundsException("No containers provided");

        if (filePool.size() > cursor)
            return filePool.get(cursor++);

        return filePool.get(cursor = 0);
    }

    public void addContainers(File... files) {
        for (File file : files)
            if (algo.isAcceptableContainer(file)) try {
                Path temp = Files.createTempFile(file.getName(), "");
                Files.write(temp, Files.readAllBytes(file.toPath()));

                filePool.add(temp.toFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public int getPoolSize() {
        return filePool.size();
    }
}
