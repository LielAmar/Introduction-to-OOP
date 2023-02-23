public class StringTests {

    public static void main(String[] args) {
        String line = "final int a= 10, b,c=20,d , e   =   90 ,g ;";

        String[] a = line.replaceAll("\\s*=\\s*", "=").split("(\\s*,\\s*|\\s+)");

        for(String item : a) {
            System.out.println(item);
        }


        System.out.println("abc".split("=").length);
    }
}
