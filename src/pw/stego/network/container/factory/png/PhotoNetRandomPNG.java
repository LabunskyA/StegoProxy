package pw.stego.network.container.factory.png;

import pw.stego.network.container.Container;
import pw.stego.network.container.LosslessImageContainer;
import pw.stego.network.container.factory.ContainerFactory;
import pw.stego.network.container.util.HTML;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Random;

/**
 * Random not porn/nude png pictures from http://photo.net
 * Created by lina on 18.01.17.
 */
@Deprecated
public class PhotoNetRandomPNG implements ContainerFactory {
    private static final String URL = "http://photo.net/photodb/random-photo?category=NoNudes";
    private static final String TRIGGER = "<img class=\"\" src=\"";

    private Random r = new Random();

    private BufferedImage getImage() throws IOException {
        String html = HTML.getHTML(URL, "UTF-8");

        String imgUrl = html.substring(html.indexOf(TRIGGER) + TRIGGER.length());
        imgUrl = imgUrl.substring(0, imgUrl.indexOf("\""));

        return ImageIO.read(new URL(imgUrl));
    }

    /**
     * @param params contains image resolution in int format in first two elements
     */
    @Override
    public Container createContainer(String[] params) throws IOException {
        BufferedImage image = getImage();
        if (params.length >= 2) {
            int w = Integer.parseInt(params[0]);
            int h = Integer.parseInt(params[1]);

            do {
                if (w > image.getWidth() || h > image.getHeight()) {
                    image = getImage();
                    continue;
                }

                int startX = r.nextInt(image.getWidth() - w);
                int startY = r.nextInt(image.getHeight() - h);

                image = image.getSubimage(startX, startY, w, h);
                break;
            } while (true);
        }

        return new LosslessImageContainer(image);
    }
}
