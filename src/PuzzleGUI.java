
 /* ------------------------------------------------ 
  * Prog 4: 8 Tiles UI * 
  *
  * Class: CS 342, Fall 2016  
  * System: OS X, Eclipse IDE
  * Author Code Number: brio  
  *
  * -------------------------------------------------
  */

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferStrategy;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;


/**
 * This class only builds the GUI for the puzzle
 * Builds the board in which the tiles can move
 * to complete the puzzle.
 * 
 * The logic behind the 8-tile puzzle is in the other classes
 */

public class PuzzleGUI extends Canvas implements Runnable, KeyListener {

 
    private int tileSize = 128;
    // table size
    private int size;


    private Board board;
    private Board objective;
    private boolean objectiveReached = false;

    // transition animations
    // position of which tile is being animated 
    private int animTileX = -1;
    private int animTileY = -1;
    // initial pixel position
    private int x1;
    private int y1;
    // actual position in pixels
    private float currentX = 0;
    private float currentY = 0;
    // speed of the animation
    private float speed = 1000f;
    
    private float velX;
    private float velY;
    private boolean animationFinished = true;

    private double deltaTime = 0;
    
    // Stores the direction in which the blank space has to move 
    private Direction direction;

    //
    private int marginH = 50;
    private int marginV = 50;

    private Color lineColor = new Color(0, 0, 0, 120);
    
    public PuzzleGUI(Board board, Board objective, int size) {
        this.board = board;
        this.objective = objective;
	this.size = size;

        addKeyListener(this);
        setFocusable(true);
        setSize(new Dimension(size * tileSize + marginH, size * tileSize+marginV));
    }

    public void start() {
        new Thread(this, "Game").start();
    }

    public void setBoard(Board board) {
	this.board = board;
    }

    public void setObjectiveBoard(Board ob) {
	this.objective = ob;
    }

    public Board getBoard() {
        return board;
    }

    public Board getObjectiveBoard() {
        return objective;
    }

    public void checkObjectiveReached() {
        objectiveReached = board.equals(objective);
    }
    
    private void drawTile(Graphics2D g, int value, int x, int y, int size) {
        Color color = new Color(150, 73, 47, 255);
        int w = 5;
        if(objectiveReached && animationFinished)
            color = new Color(55, 122, 40);
        if(!objectiveReached)
            new Color(150, 73, 47, 255);

        Color light = color.brighter();
        Color dark = color.darker();

        g.setColor(light);
        g.fillRect(x, y, size, size);
        g.setColor(dark);
        g.fillRect(x + w, y + w, size - w, size - w);
        g.setColor(color);
        g.fillRect(x + w, y + w, size - 2*w, size -2*w);

        g.setColor(Color.white);
        g.drawString("" + value, x + size/2, y + size/2);
    }

   
    public void keyPressed(KeyEvent e){}
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){
    Direction dir;

        //obtains the direction in which the blank space was moved 
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                dir = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                dir = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                dir = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                dir = Direction.RIGHT;
                break;
            default:
                return;
        }

        moveBlank(dir);
    }

    public void moveBlank(Direction dir) {
        direction = dir;
    }

 
    private void move() {
        animTileX = board.getBlankX();
        animTileY = board.getBlankY();

        if(!board.move(direction)) {
            animTileX = animTileY = -1;
            direction = null;
            return;
        }
        board.clearHistory();

        x1 = board.getBlankX() * tileSize;
        y1 = board.getBlankY() * tileSize;

        currentX = currentY = 0;
        
        switch(direction) {
            case UP:
                velX = 0;
                velY = speed;
                break;
            case DOWN:
                velX = 0;
                velY = -speed;
                break;
            case LEFT:
                velX = speed;
                velY = 0;
                break;
            case RIGHT:
                velX = -speed;
                velY = 0;
                break;
        }

        checkObjectiveReached();
        direction = null;
    }
    
    public void run() {
	long previous = System.currentTimeMillis();
	long elapsed, current, sleepTime;
	long frameTime = 1000 / 20; // 20 FPS

        checkObjectiveReached();
        // Game Loop
        while(true) {
	    current = System.currentTimeMillis();
	    elapsed = current - previous;
	    previous = current;
	    deltaTime = elapsed / 1000.0;

            if(direction != null) move();
            render();

	    sleepTime = frameTime - (System.currentTimeMillis() - current);
	    if(sleepTime < 0) sleepTime = 0;
	    try { Thread.sleep(sleepTime); }
	    catch(InterruptedException e) {}
        }
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());

        // Draw the board
        Color boardColor = new Color(51, 51, 255, 255);
        int outerMargin = 20;
        g.setColor(boardColor);
        g.fillRect(marginH/2-outerMargin, marginV/2-outerMargin,
                        size*tileSize+2*outerMargin, size*tileSize+2*outerMargin
                        );
        g.setColor(lineColor);
        g.drawRect(marginH/2-outerMargin, marginV/2-outerMargin,
                        size*tileSize+2*outerMargin, size*tileSize+2*outerMargin
                        );
        g.setColor(boardColor.darker().darker());
        g.fillRect(marginH/2, marginV/2, size * tileSize, size * tileSize);
        g.setColor(lineColor);
        g.drawRect(marginH/2, marginV/2, size * tileSize, size * tileSize);
	
	animationFinished = true;
        for(int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                byte value = board.getValue(x, y);

                // Animate the tile movements
                if(x == animTileX && y == animTileY) {
		    animationFinished = false;
                    drawTile(g,
                             value,
                             x1 + (int)currentX + marginH / 2,
                             y1 + (int)currentY + marginV / 2,
                             tileSize);
                    currentX += velX * deltaTime;
                    currentY += velY * deltaTime;
                    if(Math.sqrt(currentX*currentX + currentY*currentY) > tileSize) {
                        animTileX = animTileY = -1;
			animationFinished = true;
		    }
		        
                }
                else if(value != 0){
                    drawTile(g, value,
                             x * tileSize + marginH / 2,
                             y * tileSize + marginV / 2,
                             tileSize);
                }
            }
        }
        
        g.dispose();
        bs.show();
    }

}