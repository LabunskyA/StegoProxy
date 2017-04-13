package pw.stego.network.container.factory.png;

import pw.stego.network.container.Container;
import pw.stego.network.container.LosslessImageContainer;
import pw.stego.network.container.factory.ContainerFactory;
import pw.stego.network.proxy.util.Config;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Random PNG from random factory that creates png files
 * Handles all factories in png package at the same time
 * Created by lina on 18.01.17.
 */
public class RandomPNG implements ContainerFactory<BufferedImage> {
    private static final String[] registeredNames = new String[]{
            "lorempixelpng", "robohashpng"
    };

    private final Random r = new Random();
    private final List<ContainerFactory<BufferedImage>> factories = new LinkedList<>();

    public RandomPNG() {
        for (String name : registeredNames)
            //noinspection unchecked
            factories.add(Config.factoryByName(name));
    }

    @Override
    public Container<BufferedImage> createContainer(String[] params) throws IOException {
        return factories.get(r.nextInt(factories.size())).createContainer(params);
    }
}
