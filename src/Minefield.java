import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import javax.swing.*;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("serial")
public class Minefield extends JPanel{
	private Spot[][] field;
    public static final int COURT_WIDTH = 400;
    public static final int COURT_HEIGHT = 400;
    private JLabel status;
    private JLabel numBombsLeft;
    private JFrame frame;
    private boolean playing;
    private Date pressedTime;
    public static final int numTotalBombs = 40;
    private int currentHS;
    private Map<String, Spot> bombSpots;
    private Map<String, Spot> flaggedSpots;
    
	public Minefield (JFrame frame, JLabel status, JLabel numBombsLeft) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.status = status;
		this.numBombsLeft = numBombsLeft;
		this.frame = frame;
		bombSpots = new TreeMap<String, Spot>();
		//flaggedSpots = new TreeMap<String, Spot>();
		
		addMouseListener(new MouseAdapter() {
			int mx = 0;
			int my = 0;
	            
	        public void mousePressed(MouseEvent e) {
	            	mx = e.getX() / 20;
	                my = e.getY() / 20;
	                
	                pressedTime = new Date();
	            }
	            
	        public void mouseReleased(MouseEvent e) {
	            if (playing && !field[mx][my].isRevealed()) {
		            if (new Date().getTime() - pressedTime.getTime() <= 400) {
		                field[mx][my].reveal();
		                
		                if (field[mx][my].isEmpty()) {
		                	revealEmpties(mx, my);
		                }
		                repaint();
		                
		                if (field[mx][my].isLost()) {
		                	playing = false;
		                	if (getActualNumBombs() <= currentHS) {
		                		JOptionPane.showMessageDialog(frame,
		                    			"Congratulations! You have the best score with only " + getActualNumBombs() +
		                    			" bombs left!\nGood luck next time, and press reset to play again",
		                        	    "Winning Message",
		                        	    JOptionPane.PLAIN_MESSAGE);
		                		try {
									Writer wr = new FileWriter("highscore");
									wr.write("" + getActualNumBombs());
									wr.flush();
									wr.close();
								} catch (IOException e1) {
									System.out.println("error while checking document: " + e1.getMessage());
								}
		                	} else {
		                		JOptionPane.showMessageDialog(frame,
		                			"The least number of bombs left was " + currentHS + ". \nGood luck next time, "
		                        			+ "and press reset to play again!",
		                       	    "Lost Message",
		                       	    JOptionPane.PLAIN_MESSAGE);
		                	}
	                        status.setText("You lost! Try not to click on bombs next time!");
	                    }
		                
	                } else {
	                	field[mx][my].flag();
		                numBombsLeft.setText("Number of Bombs Left: " + getNumBombs() + "   ");
	                    repaint();
	                }
		            
		            if (isWon()) {
	                	playing = false;
	                	try {
							Writer wr = new FileWriter("highscore");
							wr.write("0");
							wr.flush();
							wr.close();
						} catch (IOException e1) {
							System.out.println("error while checking document: " + e1.getMessage());
						}
	                	JOptionPane.showMessageDialog(frame,
	                			"Wow you won this game!!! Congratulations. \nThe highscore is all yours,"
	                			+ "but if you want to restart the highscore count, you can use the \"reset highscore\""
	                			+ "button. \nPress reset to play again!",
	                       	    "CONGRATULATIONS",
	                       	    JOptionPane.PLAIN_MESSAGE);
	                	status.setText("You won at Minesweeper!");
	                }
            	}
            }
        });
	}
	
	private boolean isWon() {
		for (int i = 0; i < 20; i ++) {
        	for (int j = 0; j < 20; j ++) {
        		if (!field[i][j].isFlagged() && !field[i][j].isRevealed()) {
        			return false;
        		}
        	}
		}
		return true;
	}
	
	private void assignValues () {
		for (int i = 0; i < 16; i += 3) {
        	for (int j = 0; j < 16; j += 3) {
        		int xPlaceBomb = ThreadLocalRandom.current().nextInt(i, i + 3);
        		int yPlaceBomb = ThreadLocalRandom.current().nextInt(j, j + 3);
        		field[xPlaceBomb][yPlaceBomb].setBomb();
        		bombSpots.put("Spot Position " + xPlaceBomb + " " + yPlaceBomb, field[xPlaceBomb][yPlaceBomb]);
        	}
        }
		
		//edge cases place bombs
		field[18][ThreadLocalRandom.current().nextInt(0, 20)].setBomb();
		field[19][ThreadLocalRandom.current().nextInt(0, 20)].setBomb();
		field[ThreadLocalRandom.current().nextInt(0, 20)][18].setBomb();
		field[ThreadLocalRandom.current().nextInt(0, 20)][19].setBomb();
		
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				if (!field[i][j].isBomb()) {
					int count = 0;
					for (int si = Integer.max(i - 1, 0); si <= Integer.min(i + 1, 19); si ++) {
						for (int sj = Integer.max(j - 1, 0); sj <= Integer.min(j + 1, 19); sj++) {
							if (field[si][sj].isBomb()) {
								count += 1;
							}
						}
					}
					field[i][j].setNum(count);
				}
			}
		}
	}
	
	public void reset() {
		try {
			Reader r = new FileReader("highscore");
			int first = r.read() - 48;
			int second = r.read() - 48;
			if (second == -1) {
				currentHS = first;
			}
			r.close();
			currentHS = first * 10 + second;
		} catch (IOException e) {
			System.out.println("error while checking document: " + e.getMessage());
		}
		playing = true;
        status.setText("Running...");
        numBombsLeft.setText("Number of Bombs Left: " + numTotalBombs + "   ");
        
        field = new Spot[20][20];
        for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
        		field[i][j] = new Spot(20 * i, 20 * j, COURT_WIDTH, COURT_HEIGHT);
        	}
        }
        assignValues();
        repaint();
        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        	for (int i = 0; i < 20; i++) {
        		for (int j = 0; j < 20; j++) {
        			field[i][j].draw(g);
        		}
        	}
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
    
    public int getNumBombs() {
    	int num = 0;
    	for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
        		if (field[i][j].isFlagged()) {
        			num++;
        		}
        	}
    	}
    	return numTotalBombs - num;
    }
    
    public int getActualNumBombs() {
    	int num = 0;
    	for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
        		if (field[i][j].isFlagged() && field[i][j].isBomb()) {
        			num++;
        		}
        	}
    	}
    	return numTotalBombs - num;
    }
    
    public void revealEmpties(int x, int y) {
    	
    	for (int i = Integer.max(x - 1, 0); i <= Integer.min(x + 1, 19); i ++) {
			for (int j = Integer.max(y - 1, 0); j <= Integer.min(y + 1, 19); j++) {
				if (i != x || j != y) {
					if (!field[i][j].isEmpty()) {
						field[i][j].reveal();
					}
					
					if (field[i][j].isEmpty() && !field[i][j].isRevealed()) {
						field[i][j].reveal();
						revealEmpties(i, j);
					}
					
				}
			}
    	}
    }
}
