package danogl.gui.rendering;

import danogl.util.Vector2;

import java.awt.*;

/**
 * A simple class for rendering strings
 * @author Dan Nirel
 */
public class TextRenderable implements Renderable {

    /**
     * Possible behaviors of multiline strings in regard
     * to the specified rendering size
     */
    public enum MultilineHandling {
        /** The overall height will be the specified height multiplied by the number of lines */
        HEIGHT_IS_FOR_SINGLE_LINE,
        /** The overall height will be the specified height */
        HEIGHT_IS_FOR_ALL_LINES
    }

    private static final String LINE_BREAK = "\n";
    private static final String DEFAULT_FONT = Font.SANS_SERIF;
    private static final float FONT_PTS_PER_INCH = 2*72f;

    private ConfigureGraphics configureGraphics = new ConfigureGraphics();
    private String str;
    private String[] lines;
    private MultilineHandling multilineHandling = MultilineHandling.HEIGHT_IS_FOR_ALL_LINES;
    private int gapBetweenLinesInPixels = 5;
    private int linePixelHeight;
    private int lastRenderHeight = 10;
    private Font font;
    private Color textColor = Color.black;

    /** Construct a TextRenderable for the specified string */
    public TextRenderable(String str) { this(str, DEFAULT_FONT); }

    /** Construct a TextRenderable for the specified string in the specified font */
    public TextRenderable(String str, String fontName) {
        this(str, fontName, false, false);
    }

    /** Construct a TextRenderable with the specified string, font, and styling */
    public TextRenderable(String str, String fontName, boolean isItalic, boolean isBold) {
        int style = 0;
        if(isItalic)
            style |= Font.ITALIC;
        if(isBold)
            style |= Font.BOLD;
        font = new Font(fontName, style, 10); //meaningless size, will change in setString
        this.str = str;
    }

    /** Returns the rendered string */
    public String renderedString() {
        return String.join(LINE_BREAK, lines);
    }

    /** Set the text color */
    public void setColor(Color textColor) {
        this.textColor = textColor;
    }

    /** Change the rendered string */
    public void setString(String str)
        { setString(str, multilineHandling, gapBetweenLinesInPixels); }

    /**
     * Set the rendered string.
     * @param str the new string
     * @param multilineHandling How to handle string with multiple lines in regard to expected size
     * @param gapBetweenLinesInPixels How many pixels should separate different lines
     */
    public void setString(String str, MultilineHandling multilineHandling, int gapBetweenLinesInPixels) {
        if(this.str.equals(str)
                && this.multilineHandling == multilineHandling
                && this.gapBetweenLinesInPixels == gapBetweenLinesInPixels) {
            return;
        }
        this.str = str;
        this.multilineHandling = multilineHandling;
        this.gapBetweenLinesInPixels = gapBetweenLinesInPixels;
        resetDrawSettings();
    }

    @Override
    public void render(Graphics2D g, Vector2 topLeftCorner, Vector2 dimensions,
                       double degreesCounterClockwise,
                       boolean isFlippedHorizontally, boolean isFlippedVertically,
                       double opaqueness) {

        if(isFlippedHorizontally)
            degreesCounterClockwise *= -1;
        if(isFlippedVertically)
            degreesCounterClockwise *= -1;
        if(dimensions.y() != lastRenderHeight) {
            lastRenderHeight = (int)dimensions.y();
            resetDrawSettings();
        }
        g.setFont(font);
        g.setColor(textColor);

        configureGraphics.init(g, topLeftCorner, dimensions, degreesCounterClockwise, opaqueness);
        configureGraphics.setRotation();
        configureGraphics.setOpaqueness();

        for(int i = 0 ; i < lines.length ; i++) {
            g.drawString(lines[i], topLeftCorner.x(), topLeftCorner.y()
                    +linePixelHeight+i*(linePixelHeight + gapBetweenLinesInPixels));
        }

        configureGraphics.rollbackChanges();
    }

    private void resetDrawSettings() {
        this.lines = str.split(LINE_BREAK);
        switch(multilineHandling) {
            case HEIGHT_IS_FOR_SINGLE_LINE:
                linePixelHeight = lastRenderHeight;
                break;
            case HEIGHT_IS_FOR_ALL_LINES:
                linePixelHeight =
                        (lastRenderHeight-(lines.length-1)*gapBetweenLinesInPixels)/lines.length;
                break;
        }
        font = new Font(font.getFontName(), font.getStyle(),
                (int)(FONT_PTS_PER_INCH *
                        (float)linePixelHeight/Toolkit.getDefaultToolkit().getScreenResolution()));
    }
}
