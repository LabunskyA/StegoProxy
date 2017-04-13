package pw.stego.network.container.factory.png;

import pw.stego.network.container.Container;
import pw.stego.network.container.LosslessImageContainer;
import pw.stego.network.container.factory.ContainerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;

/**
 * Random PNG containers from lorempixel.com
 * Created by lina on 05.01.17.
 */
public class LoremPixelPNG implements ContainerFactory<BufferedImage> {
    private final static String IMG_URL = "http://lorempixel.com/%X/%Y/";
    private final Random r = new Random();

    /**
     * @param params contains image resolution in int format in first two elements
     */
    @Override
    public Container<BufferedImage> createContainer(String[] params) throws IOException {
        String x = String.valueOf(r.nextInt(500) + 1);
        String y = String.valueOf(r.nextInt(500) + 1);

        if (params.length >= 2) {
            x = params[0];
            y = params[1];
        }

        BufferedImage image = null;
        while (image == null)
            image = ImageIO.read(new URL(IMG_URL
                    .replace("%X", x)
                    .replace("%Y", y)
            ));

        return new LosslessImageContainer(image);
    }

    @Override
    public String toString() {
        return "lorempixelpng";
    }
}
