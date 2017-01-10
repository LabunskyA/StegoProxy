package pw.stego.network.container.factory;

import pw.stego.network.container.factory.ContainerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

/**
 * Random PNG containers
 * Created by lina on 05.01.17.
 */
public class LorempixelPNGFactory implements ContainerFactory {
    private final static String IMG_URL = "http://lorempixel.com/%X/%Y/";

    /**
     * @param params contains image resolution in int format in first two elements
     */
    public File createContainer(String[] params) throws IOException {
        BufferedImage image = null;
        while (image == null)
            image = ImageIO.read(new URL(IMG_URL
                    .replace("%X", params[0])
                    .replace("%Y", params[1])
            ));

        File container = Files.createTempFile("random", ".png").toFile();
        if (ImageIO.write(image, "PNG", container))
            return container;

        throw new IOException("Failed to write image");
    }
}
