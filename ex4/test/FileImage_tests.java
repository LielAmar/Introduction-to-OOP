//import image.Image;
//import image.SubImage;
//
//import java.io.IOException;
//
//public class FileImage_tests {
//
//    public static void main(String[] args) throws IOException {
//        Image fileImage = Image.fromFile("./board.jpeg");
//
//        if(fileImage == null) {
//            return;
//        }
//
//        Image[][] subImages = fileImage.splitToSubImages(4);
//
//        for (Image[] subImageRow : subImages) {
//            for (Image singleSubImage : subImageRow) {
//                System.out.print(((SubImage) singleSubImage).print());
//            }
//
//            System.out.println();
//        }
//    }
//}
