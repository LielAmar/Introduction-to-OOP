import pepse.util.NoiseGenerator;

public class NoiseGeneratorTest {

    public static void main(String[] args) {
        double smoothness = 35;
        NoiseGenerator noiseGenerator = new NoiseGenerator(50);

        for(int i = 0; i < 512; i++) {
            System.out.println(noiseGenerator.noise((float) (i / smoothness)));
        }


        System.out.println((-95) % 30);
        System.out.println(40 % 30);
    }
}
