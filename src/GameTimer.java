
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Class: GameTimer
 * Purpose: Create the countdown timer label
 * It extends JPanel
 * @author Christina
 *
 */
public class GameTimer extends JPanel {

	private static final String DISPLAY_FORMAT_STR = "%02d:%02d";
	private int time;
	private int min;
	private int sec;
	private JLabel timerLabel;
	private Timer timer;
	private String displayString;
	private boolean gameOver;
	private GameGUI gameGUI;
	private  TimeClass tc;
	/**
	 * Constructor to set up the time (in minutes and seconds) for the Gui
	 * @param time
	 * @param gameGUI
	 */
	GameTimer(int time, GameGUI gameGUI) {
		this.time = time; // take time in num of seconds
		this.timerLabel = new JLabel();
		this.tc= new TimeClass(time);
		min = (int) ((time / 60) % 60);
		sec = (int) (time % 60);
		displayString = String.format(DISPLAY_FORMAT_STR, min, sec);
		timerLabel.setText(displayString);
		this.timer = new Timer(1000, tc);
		this.gameGUI=gameGUI;
		timer.start();

	}

	/**
	 * Get the timer label
	 * @return timer label
	 */
	public JLabel getLabel() {
		return timerLabel;
	}

	/**
	 * returns whether game is over
	 * @return true if game is over
	 */
	public boolean getGameOver(){
		return gameOver;
	}

	/**
	 * Gets the time
	 * @return int time
	 */
	public int getTime(){
		return tc.getTime();
	}

	/**
	 * Stops the timer
	 */
	public void stop(){
		timer.stop();
	}

	/**
	 * Class: TimeClass
	 * Purpose: Create the countdown timer
	 * It implements action listener
	 * @author Christina
	 *
	 */
	public class TimeClass implements ActionListener {

		int time;

		/**
		 * Constructor to initialize time
		 * @param time
		 */
		public TimeClass(int time) {
			this.time = time;
		}

		/**
		 * Return time
		 * @return int time
		 */
		public int getTime(){
			return time;
		}

		@Override
		/**
		 * Override default method to update the timer after each second
		 */
		public void actionPerformed(ActionEvent tc) {
			--time;
			// While there's more time, count down and set the label 
			if (time > 0) {
				min = (int) ((time / 60) % 60);
				sec = (int) (time % 60);
				displayString = String.format(DISPLAY_FORMAT_STR, min, sec);
				timerLabel.setText(displayString);
			} else {
				// If time is done, call gameGUI's gameover method
				timer.stop();
				timerLabel.setText("Done");
				gameGUI.gameOver();

			}
		}
	}

}