package danogl.gui.mouse;

import java.awt.event.MouseEvent;

/**
 * Lists the different mouse buttons
 * @author Dan Nirel
 */
public enum MouseButton {
    LEFT_BUTTON, RIGHT_BUTTON, MIDDLE_BUTTON, BUTTON4, BUTTON5, BUTTON6, BUTTON7;

    public static MouseButton mouseIntToMouseButton(int button) {
        switch (button) {
            case MouseEvent.BUTTON1:
                return LEFT_BUTTON;
            case MouseEvent.BUTTON2:
                return MIDDLE_BUTTON;
            case MouseEvent.BUTTON3:
                return RIGHT_BUTTON;
            case 4:
                return BUTTON4;
            case 5:
                return BUTTON5;
            case 6:
                return BUTTON6;
            case 7:
                return BUTTON7;
        }
        return null;
    }
}