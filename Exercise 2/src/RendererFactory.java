import java.util.Locale;

public class RendererFactory {

    /**
     * Builds a renderer from the given string
     *
     * @param renderer   Name of the renderer to build
     * @param size       Board size for the renderer to use
     * @return           Built renderer object
     */
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
