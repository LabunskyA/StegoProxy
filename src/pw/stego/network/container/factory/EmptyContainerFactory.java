package pw.stego.network.container.factory;

import pw.stego.network.container.Container;
import pw.stego.network.container.FileBytesContainer;
import pw.stego.network.container.factory.ContainerFactory;

import java.io.IOException;

/**
 * Empty containers (who does need this?)
 * Created by lina on 09.04.17.
 */
public class EmptyContainerFactory implements ContainerFactory {
    @Override
    public Container createContainer(String[] params) throws IOException {
        return new FileBytesContainer(new byte[0]);
    }
}
