package danogl.gui;

import javax.imageio.ImageIO;
import java.io.*;

/**
 * A package private class
 * @author Dan Nirel
 */
class ResourceReader {
    public InputStream readResource(String path) throws IOException {
        InputStream stream;
        try {
            stream = new FileInputStream(new File(path).getAbsoluteFile());
        } catch (FileNotFoundException e) { //then try reading from the jar
            stream = getClass().getResourceAsStream("/"+path);
            if(stream == null)
                throw new IOException("Could not find the resource within the jar. Note that the path is case-sensitive.");
        }
        return new BufferedInputStream(stream);
    }
}
