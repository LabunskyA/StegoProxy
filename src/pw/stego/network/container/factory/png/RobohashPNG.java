package pw.stego.network.container.factory.png;

import pw.stego.network.container.Container;
import pw.stego.network.container.LosslessImageContainer;
import pw.stego.network.container.factory.ContainerFactory;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

/**
 * Robots images from Robohash.org
 * Created by lina on 30.03.17.
 */
public class RobohashPNG implements ContainerFactory {
    private Random r = new Random();

    @Override
    public Container createContainer(String[] params) throws IOException {
        StringBuilder url = new StringBuilder("https://robohash.org/");
        int length = r.nextInt(128);
        for (int i = 0; i < length; i++)
            url.append((char) (60 + r.nextInt(30)));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(ImageIO.read(new URL(url.toString())), "PNG", out);
            return new LosslessImageContainer(out.toByteArray());
        }
    }
}
