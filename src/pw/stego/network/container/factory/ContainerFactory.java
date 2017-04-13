package pw.stego.network.container.factory;

import pw.stego.network.container.Container;

import java.io.File;
import java.io.IOException;

/**
 * Factory to create containers for proxy
 * Created by lina on 05.01.17.
 */
public interface ContainerFactory<T> {
    /**
     * @param params arguments to generate custom container
     * @return new container
     * @throws IOException on file creation error
     */
    Container<T> createContainer(String[] params) throws IOException;
}
