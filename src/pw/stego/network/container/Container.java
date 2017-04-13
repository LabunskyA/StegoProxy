package pw.stego.network.container;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Base container interface
 * Created by lina on 30.03.17.
 */
public interface Container<T> {
    T asObject();
    byte[] getBytes() throws IOException;
    File toFile(Path path) throws IOException;
}
