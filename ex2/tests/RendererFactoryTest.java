import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <B>Tests for the RendererFactory Class,</B>
 * featured in Exercise 2 of the new "Introduction to OOP" course,
 * HUJI, Winter 2021-2022 Semester.
 *
 * @author Erel Debel.
 */
class RendererFactoryTest {
    /**
     * string representing a console renderer.
     */
    public static final String CONSOLE = "console";

    /**
     * string representing a void renderer.
     */
    public static final String NONE = "none";
    private static final int DEFAULT_BOARD_SIZE = 4;
    private static final String CONSOLE_RENDERER = "ConsoleRenderer";
    private static final String VOID_RENDERER = "VoidRenderer";

    private final RendererFactory rendererFactory = new RendererFactory();

    /**
     * Checks that buildRenderer builds according to the correct strings and returns null otherwise.
     */
    @Test
    void checkBuildRenderer() {
        assertNull(rendererFactory.buildRenderer("", DEFAULT_BOARD_SIZE));
        assertNull(rendererFactory.buildRenderer("renderer", DEFAULT_BOARD_SIZE));

        Renderer consoleRenderer = rendererFactory.buildRenderer(CONSOLE, DEFAULT_BOARD_SIZE);
        assertNotNull(consoleRenderer);
        assertEquals(CONSOLE_RENDERER, consoleRenderer.getClass().getSimpleName());

        Renderer voidRenderer = rendererFactory.buildRenderer(NONE, DEFAULT_BOARD_SIZE);
        assertNotNull(voidRenderer);
        assertEquals(VOID_RENDERER, voidRenderer.getClass().getSimpleName());
    }
}