package danogl.gui;

import danogl.gui.mouse.MouseButton;
import danogl.util.MutableVector2;
import danogl.util.Vector2;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A package private class
 * @author Dan Nirel
 */
class KeyAdapterUserInputListener extends KeyAdapter implements
        UserInputListener, MouseInputListener, MouseWheelListener {
    private Set<Integer> pressedKeys = new HashSet<>();
    private Set<Integer> releasedKeys = new HashSet<>();
    private MutableVector2 mousePos = new MutableVector2();
    private EnumSet<MouseButton> pressedMouseButtons = EnumSet.noneOf(MouseButton.class);
    private EnumSet<MouseButton> clickedMouseButtons = EnumSet.noneOf(MouseButton.class);
    private double mouseWheelClicksThisFrame;

    public KeyAdapterUserInputListener() { }

    public void update(float deltaTime) {
        releasedKeys.clear();
        clickedMouseButtons.clear();
        mouseWheelClicksThisFrame = 0;
    }

    @Override
    public boolean isKeyPressed(int keyFromKeyEvent) {
        return pressedKeys.contains(keyFromKeyEvent);
    }

    @Override
    public boolean wasKeyReleasedThisFrame(int keyFromKeyEvent) {
        return releasedKeys.contains(keyFromKeyEvent);
    }

    @Override
    public boolean wasMouseButtonClickedThisFrame(MouseButton button) {
        return clickedMouseButtons.contains(button);
    }

    @Override
    public boolean isMouseButtonPressed(MouseButton button) {
        return pressedMouseButtons.contains(button);
    }

    @Override
    public Vector2 getMouseScreenPos() {
        return mousePos;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        releasedKeys.add(e.getKeyCode());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePos.setXY(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var button = MouseButton.mouseIntToMouseButton(e.getButton());
        if(button != null)
            clickedMouseButtons.add(button);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        var button = MouseButton.mouseIntToMouseButton(e.getButton());
        if(button != null)
            pressedMouseButtons.add(button);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        var button = MouseButton.mouseIntToMouseButton(e.getButton());
        if(button != null)
            pressedMouseButtons.remove(button);
    }

    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }
    @Override
    public void mouseDragged(MouseEvent e) {
        mousePos.setXY(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelClicksThisFrame = e.getPreciseWheelRotation();
    }

    @Override
    public double mouseWheelClicksThisFrame() {
        return mouseWheelClicksThisFrame;
    }
}
