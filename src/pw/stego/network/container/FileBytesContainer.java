package pw.stego.network.container;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Container of file bytes as byte array
 * Simple to use as part of "files out of filesystem" ideology
 * Created by lina on 08.04.17.
 */
public class FileBytesContainer implements Container {
    private final byte[] bytes;

    public FileBytesContainer(byte[] bytes) {
        this.bytes = bytes;
    }

    @Override
    public byte[] asObject() {
        return bytes;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public File toFile(Path path) throws IOException {
        Files.write(path, bytes);
        return path.toFile();
    }
}
