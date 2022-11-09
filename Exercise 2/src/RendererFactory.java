import java.util.Locale;

public class RendererFactory {

    public Renderer buildRenderer(String renderer, int size) {
        switch(renderer.toLowerCase(Locale.ENGLISH)) {
            case "console":
                return new ConsoleRenderer(size);
            case "none":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
