package client.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alek on 3/6/2018.
 */
public class Images {

    public static Map<String, Image> imageCache = new HashMap<>();

    public static void initialize() {
        loadImage("background_0.jpg");
        loadImage("background_1.jpg");
        loadImage("1.png");
        loadImage("6.png");
        loadImage("7.png");
        loadImage("8.png");
        loadImage("9.png");
        loadImage("wood.png");
        loadImage("stone.png");
        loadImage("closeicon.png");
    }

    public static Image getImage(String name) {
        return imageCache.get(name);
    }

    public static Image loadImage(String fileName) {
        if (imageCache.containsKey(fileName)) {
            return imageCache.get(fileName);
        }
        try {
            final InputStream imageInputStream = Images.class.getResourceAsStream("img/" + fileName);
            final Image img = ImageIO.read(imageInputStream);
            imageCache.put(fileName.split("\\.")[0], img);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
