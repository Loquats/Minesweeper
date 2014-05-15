
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Cell {									/// the actual damn thing
	
	private int myX, myY;
	private boolean isMine;
	private boolean isRevealed;
	private boolean isRedMine = false;
	private int Flag;
	private int myNeighbors;

	private final Color DEFAULT_COVERED = Color.LIGHT_GRAY;
	private final Color DEFAULT_REVEALED = new Color(50,205,50);				// colors go up to 255


	public Cell(int x, int y) {
		this(x, y, false);
	}
	
	public Cell(int row, int col, boolean alive) {
		isMine = alive;
		myX = col;
		myY = row;
	}

	public boolean isMine() {
		return isMine;
	}
	
	public boolean isRedMine() {
		return isRedMine;
	}
	/*
	 * 0: no flag
	 * 1: flag
	 * 2: question mark
	 */
	public int flagStatus() {
		return Flag;
	}
	
	public void setMine(boolean status, boolean redStatus) {
		isMine = status;
		isRedMine = redStatus;
	}
	
	public void setFlag(int num) {
		Flag = num;
	}
	
	public int getX() {
		return myX;
	}
	
	public int getY() {
		return myY;
	}

	public void reveal() {
		isRevealed = true;
	}
	
	public void unReveal() {
		isRevealed = false;
	}
	
	public boolean getRevealed(){
		return isRevealed;
	}

	public int getNeighbors() {
		return myNeighbors;
	}
	
	public void setNeighbors(int count) {
		myNeighbors = count;
	}
	
	public void setRedMine(){
		isRedMine = true;
	}

	public void draw(int x_offset, int y_offset, int width, int height,          // draws the cell
			Graphics g, BufferedImage img) {
		// I leave this understanding to the reader
		int xleft = x_offset + 1 + (myX * (width + 1));
		int xright = x_offset + width + (myX * (width + 1));
		int ytop = y_offset + 1 + (myY * (height + 1));
		int ybottom = y_offset + height + (myY * (height + 1));
		
	/*    BufferedImage img = null;
		try {
			img = ImageIO.read(new File("one.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	    if (isRevealed){
	    	if (img == null){
	    		g.setColor(DEFAULT_REVEALED);
	    		g.fillRect(xleft, ytop, width, height);
	    	}
	    	else{
	    		g.drawImage(img, xleft, ytop, null);
	    	}
	    }
	    else {
	    	g.setColor(DEFAULT_COVERED);
	    	g.fillRect(xleft, ytop, width, height);
	    }
	}
	/*
	public void draw(int x_offset, int y_offset, int width, int height,
			Graphics g) {
		// I leave this understanding to the reader
		int xleft = x_offset + 1 + (myX * (width + 1));
		int xright = x_offset + width + (myX * (width + 1));
		int ytop = y_offset + 1 + (myY * (height + 1));
		int ybottom = y_offset + height + (myY * (height + 1));
		Color temp = g.getColor();

		g.setColor(myColor);
		g.fillRect(xleft, ytop, width, height);

	}
	*/
	

}