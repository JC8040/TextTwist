import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.util.*;
/**
 * Class: PresetGameGUI
 * Purpose: Subclasses GameGUI to make use of its methods. 
 * It overrides some default methods to provide for saving time and score.  
 * It extends GameGUI
 * @author Christina
 *
 */
public class PresetGameGUI extends GameGUI {
	private JPanel selectPuzzlePanel;
	private JPanel displayGame;
	private String selectedPuzzle;
	private int selected;
	private JList puzzleList;

	private JPanel panel;
	private Player player;
	private boolean sixLettersFound;

	private JScrollPane scrollPuzzles;

	private ArrayList<String> puzzlesPlayed;

	private int fastestTime;
	/**
	 * Constructor that adds panels created by this class's methods to this panel
	 * Creates a panel that displays list of puzzles. Once user selects a puzzle, 
	 * switch to a different panel to display the game
	 * @param codeBase
	 * @param player the current player, used for saving scores
	 */
	PresetGameGUI(URL codeBase, Player player) {
		super(codeBase);
		this.player=player;
		this.codeBase = codeBase;
		this.selectPuzzlePanel = new JPanel();

		this.displayGame = new JPanel();
		displayGame.setLayout(new BoxLayout(displayGame, BoxLayout.PAGE_AXIS));
		this.add(selectPuzzlePanel);
		selectPuzzlePanel.setLayout(new BoxLayout(selectPuzzlePanel, BoxLayout.PAGE_AXIS));
		this.selectedPuzzle = displayPuzzles();
		this.revalidate();
		this.repaint();

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(selectPuzzlePanel);
		panel.add(displayGame);
		this.sixLettersFound=false;
		this.puzzlesPlayed=getPuzzlesPlayed();
	}


	/**
	 * Return panel that contains all the components for kids mode
	 * @return panel
	 */
	public JPanel getPanel(){
		return panel;
	}

	/**
	 * When user first clicks on Preset, this method presents user with list of puzzles they can play
	 * @return the puzzle selected
	 */
	private String displayPuzzles() {
		try {
			Date date = new Date();	// to get around caching issues
			// Changed
			URL puzzleURL = new URL(codeBase, "files/puzzle.txt?"+date.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					puzzleURL.openStream()));

			String line = "";
			final List<String> list = new ArrayList<String>();
			// Iterate through each line, and add it to the list
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			JLabel puzzleLabel = new JLabel("Select a preset puzzle to play:");
			puzzleList = new JList(list.toArray());

			scrollPuzzles=new JScrollPane(puzzleList);
			scrollPuzzles.setSize(new Dimension(150,200));
			scrollPuzzles.setMinimumSize(new Dimension(150,200));
			scrollPuzzles.setPreferredSize(new Dimension(150,200));
			scrollPuzzles.setMaximumSize(new Dimension(150,200));

			puzzleList.revalidate();
			puzzleList.repaint();
			scrollPuzzles.revalidate();
			scrollPuzzles.repaint();

			this.revalidate();
			puzzleLabel.setAlignmentX(CENTER_ALIGNMENT);
			scrollPuzzles.setAlignmentX(CENTER_ALIGNMENT);
			selectPuzzlePanel.add(puzzleLabel);
			selectPuzzlePanel.add(scrollPuzzles);

			// Action listener for when user selected a puzzle from the list
			puzzleList.addListSelectionListener(new ListSelectionListener() {
				/**
				 * Method that gets the selected puzzle. If user haven't played it yet, let them play it.
				 * Other wise, tell user they can't play it
				 */
				public void valueChanged(ListSelectionEvent le) {
					selected = puzzleList.getSelectedIndex();
					selectedPuzzle=list.get(selected);
					// If user already played puzzle, can't play again
					if (puzzlesPlayed.contains(selectedPuzzle)){
						JOptionPane
						.showMessageDialog(
								new JFrame(),
								"You've already played this puzzle. Please pick another one",
								"Error", JOptionPane.DEFAULT_OPTION);
					}else{
						// Set this list puzzle fram invisible, and activate the other frames
						selectPuzzlePanel.setVisible(false);
						displayGame();
						// Save the puzzle as played
						if(savePuzzlePlayed());

					}
				}
			});

			// this.removeAll();

			return list.get(selected);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Display the screen where user can play the puzzle
	 * Similar set up as the other game modes
	 */
	private void displayGame() {
		this.puzzle = new Puzzle(selectedPuzzle,  super.dict);
		displayGame.add(puzzlePanel(puzzle));
		displayGame.add(timeScorePanel());
		String[] columnNames = { "3-Letter", "4-Letter", "5-Letter", "6-Letter" };

		displayGame.add(enterLettersPanel(4, columnNames));
		displayGame.add(correctOrNot());
		displayGame.add(foundWordsPanel());
		panel.remove(selectPuzzlePanel);
		panel.add(displayGame);
	}

	/**
	 * Override parent method to also save the time when user first finds the 6-letter word
	 */
	@Override
	public void incrementScore(String word) {
		int length = word.length();
		if (length == 6) {
			score += 50;
			if (!sixLettersFound){
				// Get the time it took to fidn the word
				fastestTime=(getTime()-timer.getTime());
				// save the word
				saveFastestTime(fastestTime);
				// Already found one 6-letter word
				// Won't let the game save the time again
				sixLettersFound=true;
			}
		}
		score += 40 * length;
		scoreLabel.setText("Score: " + score);
		// Save the score
		if (saveScore());
	}

	/**
	 * Method for saving the score
	 * @return true if saved
	 */
	public boolean saveScore(){
		try {

			URL scoreFile = new URL(codeBase + "files/modifyScore.php");
			URLConnection connection = scoreFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("uid=" + player.getfbID());
			out.write("&puzzle=" + puzzle.getPuzzle());
			out.write("&score=" + score);
			out.flush();
			out.close();
			connection.getInputStream();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * Method for saving the time it took to find the 6-letter word
	 * @param time it took
	 * @return true if found
	 */
	public boolean saveFastestTime(int time){
		try {

			URL timeFile = new URL(codeBase + "files/modifyTime.php");
			URLConnection connection = timeFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("uid=" + player.getfbID());
			out.write("&puzzle=" + puzzle.getPuzzle());
			out.write("&time=" + time);
			out.flush();
			out.close();
			connection.getInputStream();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}

	/**
	 * Save the current puzzle so user can't play it again
	 * @return true if saved
	 */
	public boolean savePuzzlePlayed(){
		try {

			URL addFile = new URL(codeBase + "files/addstats.php");
			URLConnection connection = addFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("uid=" + player.getfbID());
			out.write("&puzzle=" + puzzle.getPuzzle());
			out.write("&score=" + score);
			out.write("&time=" + 1000);	
			out.flush();
			out.close();
			connection.getInputStream();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}
	/**
	 * Get all puzzles played by current user, by going through the file that saves their scores
	 * @return puzzles in an array list
	 */
	private ArrayList<String> getPuzzlesPlayed(){
		ArrayList<String> puzzlesPlayed = new ArrayList<String>();
		try {

			Date date = new Date(); // to get around caching issues
			URL playerPuzzlesFile = new URL(codeBase, "files/players/"+player.getfbID()+".txt?"
					+ date.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					playerPuzzlesFile.openStream()));
			String line="";
			// Read each score line, but just get the puzzle they've played
			while ((line=br.readLine())!=null){
				int index = line.indexOf("|");
				String aPuzzle = line.substring(0, index);
				puzzlesPlayed.add(aPuzzle);

			}
			br.close();

			return puzzlesPlayed;

		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Get the current time used for games
	 * @return time
	 */
	private int getTime() {
		try {

			Date date = new Date(); // to get around caching issues
			URL timerFile = new URL(codeBase, "files/time.txt?"
					+ date.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					timerFile.openStream()));

			int time = Integer.parseInt(br.readLine());
			br.close();

			return time;
		} catch (NumberFormatException e) {
			return -1;
		} catch (FileNotFoundException e) {
			return -1;
		} catch (IOException e) {
			return -1;
		}

	}
}
