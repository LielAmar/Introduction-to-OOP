package danogl.gui;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ConcurrentModificationException;

/**
 * The class depends on a GameManager it receives in the constructor:
 * it calls GameManager's initializeGame()), update() and render() methods.
 * Thus normal use of the engine shouldn't be concerned about this class.
 * @author Dan Nirel
 * @see GameManager
 */
public class GameGUIComponent extends Canvas implements WindowController {
    private static final String LOADSCREEN_PATH = "danoglAssets/DanoGameLabLoading.png";
    private static final int INITIAL_MIN_IDLE_TIME_IN_MILLIS = 3;
    private static final int FPS_HITS_BEFORE_WARNING_USER = 100;
    private static final float LOADSCREEN_FRAMES = 20;
    //if the game is running an FPS less than 1/MAX_FRAME_TIME, it will be slowed down
    private static final float MAX_FRAME_TIME = 0.1f;

    private int targetFramerate = 120;
    private int minIdleTimeInMillis = INITIAL_MIN_IDLE_TIME_IN_MILLIS;
    private JFrame window;
    private float timescale = 1;
    private boolean isRunning = false;
    private GameManager gameManager;
    private int fpsHits = 0;
    private Vector2 windowDimensions;
    private boolean isPaused;
    private int exitButton = KeyEvent.VK_ESCAPE;
    private int pauseButton = -1;
    private GameObject loadScreen;
    private Renderable renderableCursor;
    private Vector2 cursorDimensions;
    private Vector2 cursorOffset;
    private KeyAdapterUserInputListener inputListener;
    private BufferStrategy bufferStrategy;

    public GameGUIComponent(
            GameManager gameManager,
            String windowTitle) {
        this(gameManager, windowTitle,
                (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
                (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
                true);
    }

    public GameGUIComponent(
            GameManager gameManager,
            String windowTitle,
            int windowWidth,
            int windowHeight, boolean fullScreen) {

        super();

        this.gameManager = gameManager;
        this.windowDimensions = new Vector2(windowWidth, windowHeight);
        setPreferredSize(new Dimension(windowWidth, windowHeight));

        window = new JFrame(windowTitle);
        if (fullScreen) {
            window.setUndecorated(true);
            window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.add(this);
        window.pack(); //match size to components
        window.setVisible(true);
        setFocusable(true);
        requestFocusInWindow();
        createBufferStrategy(2);
        bufferStrategy = getBufferStrategy();
        initLoadScreen();
    }

    public void run() {
        while(true)
            runSingleGame();
    }

    public void runSingleGame() {
        timescale = 1;
        inputListener = new KeyAdapterUserInputListener();
        addKeyListener(inputListener);
        addMouseListener(inputListener);
        addMouseMotionListener(inputListener);
        addMouseWheelListener(inputListener);
        cursorDimensions = new Vector2(5,5);
        setMouseCursor(
                new OvalRenderable(Color.GRAY), cursorDimensions, cursorDimensions.mult(-.5f));

        render();

        gameManager.initializeGame(
                new ImageReader(this),
                new SoundReader(this), inputListener,
                this
        );

        //game loop
        long timAtPrevFrameStart = System.nanoTime();
        isRunning = true;
        while(isRunning) {
            long timeAtFrameStart = System.nanoTime();
            float deltaTime = (float)((timeAtFrameStart-timAtPrevFrameStart)/1_000_000_000D);
            timAtPrevFrameStart = timeAtFrameStart;
            if(inputListener.wasKeyReleasedThisFrame(exitButton))
                closeWindow();
            if(inputListener.wasKeyReleasedThisFrame(pauseButton))
                isPaused = !isPaused;
            if(!isPaused)
                gameManager.update(Math.min(MAX_FRAME_TIME, deltaTime * timescale));
            inputListener.update(deltaTime);
            render();
            if(!isRunning)
                continue;
            long frameTime = System.nanoTime()-timeAtFrameStart;
            idleTime(frameTime);
        }
        removeKeyListener(inputListener);
    }

    @Override
    public void closeWindow() {
        window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void resetGame() {
        isRunning = false;
    }

    @Override
    public void showMessageBox(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    @Override
    public boolean openYesNoDialog(String msg) {
        int res = JOptionPane.showOptionDialog(
                this, msg, "",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        return res == 0;
    }

    public double getTimeScale() { return timescale; }
    public void setTimeScale(float value) { timescale = value; }

    @Override
    public Vector2 getWindowDimensions() {
        return windowDimensions;
    }

    @Override
    public void setTargetFramerate(int targetFramerate) {
        this.targetFramerate = targetFramerate;
    }

    @Override
    public void setPauseButton(int keyFromKeyEvent) {
        pauseButton = keyFromKeyEvent;
    }

    @Override
    public void setExitButton(int keyFromKeyEvent) {
        exitButton = keyFromKeyEvent;
    }

    @Override
    public void setMouseCursor(Renderable renderable, Vector2 dimensions, Vector2 offset) {
        renderableCursor = renderable;
        cursorDimensions = dimensions;
        cursorOffset = offset;
    }

    @Override
    public Renderable getMouseCursor() {
        return renderableCursor;
    }

    @Override
    public Vector2 getMouseCursorDimensions() {
        return cursorDimensions;
    }


    private void initLoadScreen() {
        ImageRenderable loadScreenImage =
                new ImageReader(this).readImage(
                        LOADSCREEN_PATH, false);

        Vector2 loadScreenDimensions = new Vector2(
                windowDimensions.x(),
                windowDimensions.x()/loadScreenImage.ratioAsWidthDivHeight());
        Vector2 loadScreenPos = windowDimensions
                .subtract(loadScreenDimensions).mult(0.5f);

        loadScreen = new GameObject(
                loadScreenPos, loadScreenDimensions, loadScreenImage);
        loadScreen.setVelocity(Vector2.UP.mult(3));
        loadScreen.renderer().fadeOut(LOADSCREEN_FRAMES);

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);
    }

    private void render() {
        Graphics g = bufferStrategy.getDrawGraphics();
        Graphics2D g2d = ((Graphics2D)g);

        g2d.setBackground(Color.WHITE);
        g2d.clearRect(0,0,(int)windowDimensions.x(),(int)windowDimensions.y());
        g2d.setColor(Color.BLACK);

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if(gameManager != null && isRunning) {
            gameManager.render(g2d);
            if (renderableCursor != null) {
                renderableCursor.render(
                        g2d,
                        inputListener.getMouseScreenPos().add(cursorOffset),
                        cursorDimensions);
            }
        }
        if(loadScreen != null) {
            loadScreen.render(g2d);
            loadScreen.update(1);
            if(loadScreen.renderer().getOpaqueness() <= 0)
                loadScreen = null;
        }
        g.dispose();
        bufferStrategy.show(); // flip screen
    }

    private void idleTime(long frameTime) {
        //in order to reach a frame rate of 100 for example, each
        //frame should take 10 millis (1000/100=10).
        //by now the frame already took frameTime (supplied in nanoseconds)
        //so sleep the remainder. Painting, which is executed on a helper thread,
        //is expected to run during this time.
        double millisToSleep = 1000D/ targetFramerate - frameTime/1_000_000D;
        millisToSleep = Math.max(millisToSleep, minIdleTimeInMillis);
        if(millisToSleep == minIdleTimeInMillis) {
            fpsHits++;
            if(fpsHits == FPS_HITS_BEFORE_WARNING_USER) {
                System.err.println("Warning: your frames are taking " +
                    "too long to update, which means the target " +
                    "frame-rate ("+ targetFramerate +") cannot be reached. " +
                    "If your frame-rate is low, then either your update pass " +
                    "is taking too long (too many objects? an overly complex " +
                    "logic?), or the target frame-rate is set too high for the " +
                    "hardware.");
            }
        }
        try {
            Thread.sleep((int)millisToSleep);
        } catch (InterruptedException e) {
            System.err.println("Failed to sleep: " + e.getMessage());
        }
    }
}
