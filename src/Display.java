 
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;

// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.


public class Display extends JComponent implements MouseListener, MouseMotionListener {									/// the actual damn thing
	public static final int ROWS = 10;
	public static final int COLS = 10;
	
	private static final int numberOfMines = 10;
	private int numberOfFlags = 0;
	private boolean gameStatus = true;
	
	public static Cell[][] cell = new Cell[ROWS][COLS];                 // array of cell objects in row*column (makes an array of cell class objects call "cell")
	private final int X_GRID_OFFSET = 25; // pixels from left
	private final int Y_GRID_OFFSET = 40; // pixels from top
	private final int CELL_WIDTH = 20;
	private final int CELL_HEIGHT = 20;

	// Note that a final field can be initialized in constructor
	private final int DISPLAY_WIDTH;   
	private final int DISPLAY_HEIGHT;
	private ResetButton reset;
	private RevealButton reveal;
//	private boolean paintloop = false;
	
	private BufferedImage one;
	private BufferedImage two;
	private BufferedImage three;
	private BufferedImage four;
	private BufferedImage five;
	private BufferedImage six;
	private BufferedImage seven;
	private BufferedImage eight;
	private BufferedImage mine;
	private BufferedImage redMine;
	private BufferedImage flag;
	private BufferedImage question;


	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;

		try {
			one = ImageIO.read(new File("one.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			two = ImageIO.read(new File("two.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			three = ImageIO.read(new File("three.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			four = ImageIO.read(new File("four.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			five = ImageIO.read(new File("five.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			six = ImageIO.read(new File("six.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			seven = ImageIO.read(new File("seven.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			eight = ImageIO.read(new File("eight.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			mine = ImageIO.read(new File("mine.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			redMine = ImageIO.read(new File("redmine.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			flag = ImageIO.read(new File("flag.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			question = ImageIO.read(new File("question.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		init();
	}


	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();
		fillMines();
		calcNeighbors(cell);

		addMouseListener(this);
		addMouseMotionListener(this);

		// Example of setting up a button.
		// See the StartButton class nested below.
		reset = new ResetButton();
		reset.setBounds(250, 50, 100, 36);
		add(reset);
		reset.setVisible(true);
		
		reveal = new RevealButton();
		reveal.setBounds(250, 100, 100, 36);
		add(reveal);
		reveal.setVisible(true);
		
		repaint();
		System.out.println("init repaint");
	}


	public void paintComponent(Graphics g) {		// this is constantly called and refreshed

	//	System.out.println("paintComponent");
		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		countFlags();
	//	drawButtons();

//		if (paintloop) {							///////////////
//			repaint();
//			System.out.println("repaint");
//		}
	}


	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}
	}


//	public void togglePaintLoop() {
//		paintloop = !paintloop;
//	}


//	public void setPaintLoop(boolean value) {
//		paintloop = value;
//	}


	void drawGrid(Graphics g) {                               // draws the display grid
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
		
		g.drawString("Mines left:  " + (numberOfMines - numberOfFlags), 25, 25);
		
		if (!gameStatus){
			g.drawString("Click Reset to Play Again :D", 250, 25);
		}
		repaint();
	}

	
	void drawCells(Graphics g) {
		// Have each cell draw itself									///// <---
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[i][j].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g, getImage(cell[i][j]));
			}
		}
	}
	
	public void countFlags(){
		numberOfFlags = 0;
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLS; j++) {
				if (cell[i][j].flagStatus() == 1)
					numberOfFlags++;
			}
		}
	}

	private BufferedImage getImage(Cell cell) {
		if (cell.flagStatus() == 1)
			return flag;
		else if (cell.flagStatus() == 2)
			return question;
		else if (cell.isMine())
			if (cell.isRedMine())
				return redMine;
			else return mine;
		else if (cell.getNeighbors()==1)
			return one;
		else if (cell.getNeighbors()==2)
			return two;
		else if (cell.getNeighbors()==3)
			return three;
		else if (cell.getNeighbors()==4)
			return four;
		else if (cell.getNeighbors()==5)
			return five;
		else if (cell.getNeighbors()==6)
			return six;
		else if (cell.getNeighbors()==7)
			return seven;
		else if (cell.getNeighbors()==8)
			return eight;
		else return null;
	}


	// real stuff
	
	public void calcNeighbors(Cell[][] cell) {
		
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				
				if (cell[i][j].isMine())
					cell[i][j].setNeighbors(-1);
				else cell[i][j].setNeighbors(0);
				
				for (int a = -1; a < 2; a++) {
					for (int b = -1; b < 2; b++) {
						
						int neighborX = i + a;
						int neighborY = j + b;
						
						// if neighbor to be checked is within bounds of array
						if (0 <= neighborX && neighborX < cell.length && 0 <= neighborY && neighborY < cell[i].length) {
							
							if (cell[neighborX][neighborY].isMine()) {
								cell[i][j].setNeighbors(cell[i][j].getNeighbors() + 1);
							}	
						}
					}
				}
			}
		}
	}
	
	public void revealCells() {
		
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				cell[i][j].reveal();
//				System.out.print(" " + cell[i][j].getNeighbors());
			}
//			System.out.println();
		}
		repaint();
	}
	
	public void unRevealCells() {
		
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				cell[i][j].unReveal();
			}
		}
		repaint();
	}

	public void fillMines() {
		
		for (int i = 0; i < numberOfMines; i++){
			Random num = new Random();
			int position = num.nextInt(ROWS * COLS);
			
			int row = position / 10;
			int col = position % 10;
			
			if (cell[row][col].isMine())
				i--;
			else 
				cell[row][col].setMine(true, false);
		}
		repaint();
	}
	
	public void clearMines() {
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				cell[i][j].setMine(false, false);
			}
		}
		repaint();
	}
	
	public void clearFlags() {
		for (int i = 0; i < cell.length; i++) {                
			for (int j = 0; j < cell[i].length; j++) {
				cell[i][j].setFlag(0);
			}
		}
		repaint();
	}
	
	public void cascade(Cell blankCell) {					/////////
		
		blankCell.reveal();

		for (int a = -1; a < 2; a++){
			for (int b = -1; b < 2; b++) {
				
				final int i = blankCell.getX() + a;
				final int j = blankCell.getY() + b;
				
				if (i != blankCell.getX() || j != blankCell.getY()){
					if (0 <= j && j < cell.length && 0 <= i && i < cell[0].length) {
						if (!cell[j][i].getRevealed() && !cell[j][i].isMine()){
							if (cell[j][i].getNeighbors() == 0){
								cascade(cell[j][i]);
							}
							else 
								cell[j][i].reveal();
						}
					}
				}
			}
		}
		repaint();
	}

	
	// Mouse stuff
	
	public void mouseClicked(MouseEvent arg0) {
		
		if (gameStatus) {
			
			int x = arg0.getX();
			int y = arg0.getY();
			
			x = (int) (x - X_GRID_OFFSET - 1)/(CELL_WIDTH + 1);					// coordinates in cell[][]
			y = (int) (y - Y_GRID_OFFSET - 2)/(CELL_HEIGHT + 1);
			
			
			if (x > -1 && x < COLS && y > -1 && y < ROWS){
				if (arg0.getButton() == MouseEvent.BUTTON1) {
					if (cell[y][x].isMine()) {
						if (cell[y][x].flagStatus() == 0){						// if it's not a flag
						clearFlags();
						revealCells();
						cell[y][x].setRedMine();
						gameStatus = false;											// gamestatus
						}
					}
					else if (cell[y][x].getNeighbors() == 0){
						cascade(cell[y][x]);
					}
					else {
						cell[y][x].reveal();
					}
				}
				
				else if (arg0.getButton() == MouseEvent.BUTTON2){					//////   <------------------  middle click auto-reveal
					
				}
				
				else if (arg0.getButton() == MouseEvent.BUTTON3) {
					if (!cell[y][x].getRevealed() && cell[y][x].flagStatus() == 0){
						cell[y][x].reveal();
						cell[y][x].setFlag(1);
					}
					else if (cell[y][x].flagStatus() == 1)
						cell[y][x].setFlag(2);
					else if (cell[y][x].flagStatus() == 2){
						cell[y][x].setFlag(0);
						cell[y][x].unReveal();
					}
				}
			}
			
			repaint();
		}
	}

/*
	public void mouseClicked(MouseEvent arg0) { 
		
		int x = arg0.getX();
		int y = arg0.getY();
		
		x = (int) (x - X_GRID_OFFSET - 1)/(CELL_WIDTH + 1);					// coordinates in cell[][]
		y = (int) (y - Y_GRID_OFFSET - 2)/(CELL_HEIGHT + 1);

		if (arg0.getButton() == MouseEvent.BUTTON1) { 
		; //the first button (left)
		} else if (arg0.getButton() == MouseEvent.BUTTON2) { 
		; //the second button (center)
		} else if (arg0.getButton() == MouseEvent.BUTTON3) { 
		; //the third button (right)
		}
	}

*/

	public void mouseEntered(MouseEvent arg0) {

	}


	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {

	}


	public void mouseDragged(MouseEvent arg0) {

	}


	public void mouseMoved(MouseEvent arg0) {
		
	}
	

	private class ResetButton extends JButton implements ActionListener {
		ResetButton() {
			super("Reset");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			
			gameStatus = true; 
			unRevealCells();
			clearMines();
			clearFlags();
			fillMines();
			calcNeighbors(cell);
			repaint();
		}
	}
	
	
	
	private class RevealButton extends JButton implements ActionListener {			/////
		RevealButton() {
			super("Cheat");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			calcNeighbors(cell);
			revealCells();
			repaint();
		}
	}
}


/* for original StartButton

public void actionPerformed(ActionEvent arg0) {

	if (this.getText().equals("Start")) {
		togglePaintLoop();
		setText("Stop");
	} else {
		togglePaintLoop();
		setText("Start");
	}
	repaint();
}
*/