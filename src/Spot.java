import java.awt.*;
public class Spot extends GameObj{
	private int num;
	private boolean bomb;
	private boolean revealed;
	private boolean isFlagged;

    private final int COURT_WIDTH;
    private final int COURT_HEIGHT;
	
    public Spot(int xPos, int yPos, int courtWidth, int courtHeight) {
    	super(0, 0, xPos, yPos, 20, 20, courtWidth, courtHeight);
    	num = 0;
    	bomb = false;
    	revealed = false;
    	isFlagged = false;
    	COURT_WIDTH = courtWidth;
    	COURT_HEIGHT = courtHeight;
    }
    
    @Override
    public void draw(Graphics p) {
        if (revealed == true && bomb) {
        	BombObj bombpic= new BombObj(getPx(), getPy(), COURT_WIDTH, COURT_HEIGHT);
        	bombpic.draw(p);
        } else if (revealed == true && !bomb) {
        	p.setColor(Color.BLUE);
        	p.drawString("" + num, getPx() + 8, getPy() + 15);
        } else if (isFlagged == true) {
        	FlagObj flagpic = new FlagObj(getPx(), getPy(), COURT_WIDTH, COURT_HEIGHT);
        	flagpic.draw(p);
        } else {
        	unopenedObj unopenedpic= new unopenedObj(getPx(), getPy(), COURT_WIDTH, COURT_HEIGHT);
        	unopenedpic.draw(p);
        }
    }
    
    public boolean isEmpty() {
    	return (this.num == 0 && !bomb);
    }
    
    public void setNum(int setNum) {
    	this.num = setNum;
    }
    
    public void setBomb() {
    	this.bomb = true;
    }
    
    public void flag() {
    	isFlagged = !isFlagged;
    }
    
    public boolean isBomb() {
    	return bomb;
    }
    
    public boolean isRevealed() {
    	return revealed;
    }
    
    public void reveal() {
    	if(!isFlagged) {
    		revealed = true;
    	}
    }
    
    public boolean isFlagged() {
    	return isFlagged;
    }
    
    public boolean isLost() {
    	if (revealed && bomb) {
    		return true;
    	}
    	return false;
    }
    
    public int getNum() {
    	return num;
    }
}
