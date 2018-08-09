/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("MINESWEEPER");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        JLabel numBombsLeft = new JLabel("" + Minefield.numTotalBombs);
        

        // Reset button
        final JPanel control_panel = new JPanel();
        control_panel.add(numBombsLeft);
        
        
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we define it as an
        // anonymous inner class that is an instance of ActionListener with its actionPerformed()
        // method overridden. When the button is pressed, actionPerformed() will be called.
        
        final Minefield mineField = new Minefield(frame, status, numBombsLeft);
        frame.add(mineField, BorderLayout.CENTER);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
          	mineField.reset();
          }
        });
        control_panel.add(reset);
        
        JButton resetHS= new JButton("Reset Highscore");
        resetHS.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			Writer wr = new FileWriter("highscore");
					wr.write("40");
					wr.flush();
					wr.close();
        		} catch (IOException e1) {
					System.out.println("error while checking document: " + e1.getMessage());
				}
        	}
        });
        control_panel.add(resetHS);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        mineField.reset();
        
        JOptionPane.showMessageDialog(frame,
        	    "Welcome to the Windows Classic Game - Minesweeper!\nThe rules are simple - "
        	    + "don't click on bombs, and long hold to mark bombs.\nThere are 40 bombs, "
        	    + "so try not to lose!\n"
        	    + "Player One, Good luck have fun!",
        	    "Introductory Message",
        	    JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}