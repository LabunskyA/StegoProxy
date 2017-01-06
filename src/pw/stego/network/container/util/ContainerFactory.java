package pw.stego.network.container.util;

import java.io.File;
import java.io.IOException;

/**
 * Factory to create containers for proxy
 * Created by lina on 05.01.17.
 */
public interface ContainerFactory {
    File getContainer(String[] params) throws IOException;
}
