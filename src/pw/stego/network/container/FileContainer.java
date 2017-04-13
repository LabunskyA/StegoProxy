package pw.stego.network.container;

import pw.stego.network.container.Container;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Container as a file at filesystem
 * Created by lina on 08.04.17.
 */
public class FileContainer implements Container<File> {
    private final File container;

    public FileContainer(final File container) {
        this.container = container;
    }

    @Override
    public File asObject() {
        return container;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(container.toPath());
    }

    @Override
    public File toFile(Path path) throws IOException {
        return container;
    }
}
