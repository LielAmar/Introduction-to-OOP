package danogl;

import danogl.gui.UserInputListener;
import danogl.gui.mouse.MouseAction;
import danogl.gui.mouse.MouseActionParams;
import danogl.gui.mouse.MouseButton;
import danogl.util.Vector2;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.function.UnaryOperator;

/**
 * A package-private class for the implementation of mouse-action callbacks
 * @author Dan Nirel
 */
class NotifyOfMouseActions extends MouseActionParams implements MouseListener, MouseWheelListener {
    private GameManager gameManager;
    private UserInputListener inputListener;
    private UnaryOperator<Vector2> screenToWorldPos;

    public NotifyOfMouseActions(GameManager gameManager, UserInputListener inputListener) {
        this.gameManager = gameManager;
        this.inputListener = inputListener;
    }

    public void setScreenToWorldPos(UnaryOperator<Vector2> screenToWorldPos) {
        this.screenToWorldPos = screenToWorldPos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setFields(e);
        setMouseAction(MouseAction.BUTTON_CLICK);
        gameManager.onMouseAction(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        setFields(e);
        setMouseAction(MouseAction.BUTTON_DOWN);
        gameManager.onMouseAction(this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setFields(e);
        setMouseAction(MouseAction.BUTTON_UP);
        gameManager.onMouseAction(this);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        setFields(e);
        setMouseAction(MouseAction.SCROLL);
        gameManager.onMouseAction(this);
    }

    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    private void setFields(MouseEvent e) {
        setMouseScreenPos(inputListener.getMouseScreenPos());
        setMouseWorldPos(
                screenToWorldPos == null?
                        getMouseScreenPos():
                        screenToWorldPos.apply(getMouseScreenPos()));
        setButton(MouseButton.mouseIntToMouseButton(e.getButton()));
        setScrollClicks(inputListener.mouseWheelClicksThisFrame());
    }
}
