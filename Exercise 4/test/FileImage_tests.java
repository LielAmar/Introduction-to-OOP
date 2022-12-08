import image.FileImage;
import image.Image;
import image.SubImage;

import java.io.IOException;

public class FileImage_tests {

    public static void main(String[] args) throws IOException {
        FileImage fileImage = new FileImage("./board.jpeg");

        Image[][] subImages = fileImage.splitToSubImages(4);

        for (Image[] subImageRow : subImages) {
            for (Image singleSubImage : subImageRow) {
                System.out.print(((SubImage) singleSubImage).print());
            }

            System.out.println();
        }
    }
}
