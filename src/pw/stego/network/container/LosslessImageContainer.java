package pw.stego.network.container;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Lossless image container (basically, BufferedImage as PNG)
 * Created by lina on 30.03.17.
 */
public class LosslessImageContainer implements Container<BufferedImage> {
    private final BufferedImage image;

    public LosslessImageContainer(BufferedImage image) {
        this.image = image;
    }

    public LosslessImageContainer(byte[] image) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(image)) {
            this.image = ImageIO.read(in);
        }
    }

    @Override
    public File toFile(Path path) throws IOException {
        ImageIO.write(image, "PNG", path.toFile());
        return path.toFile();
    }

    @Override
    public BufferedImage asObject() {
        return image;
    }

    @Override
    public byte[] getBytes() throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "PNG", os);
            return os.toByteArray();
        }
    }
}
