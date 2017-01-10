package pw.stego.network.container.factory;

import pw.stego.network.container.factory.ContainerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Empty files generator
 * Created by lina on 06.01.17.
 */
public class EmptyFilesFactory implements ContainerFactory {
    @Override
    public File getContainer(String[] params) throws IOException {
        File container = Files.createTempFile("empty", "").toFile();
        Files.write(container.toPath(), new byte[0]);

        return container;
    }
}
