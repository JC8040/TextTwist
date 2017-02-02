import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.util.*;

/**
 * Class: GameGUI
 * Purpose: Parent class for displaying the various game screens (excluding 100-word). 
 * Allows subclasses (different types of games) to share the same methods and structure. 
 * It extends JPanel and implements ActionListener
 * @author Christina
 *
 */
public class GameGUI extends JPanel implements ActionListener {
	/**
	 * variables are protected so subclass can use them
	 */
	protected int wordLength;
	protected int width, height;
	protected Dictionary dict;

	protected URL codeBase;
	protected ArrayList<JList> pSolutions;

	protected Puzzle puzzle;

	protected JTable tableSolutions;
	protected JScrollPane scrollSolutions;

	protected JTextField enteredText;

	protected HashMap<String, Integer[]> hashSolutions;
	protected ArrayList<ArrayList<String>> arrList;
	protected LinkedList<LinkedList<String>> linkedList;
	protected DefaultTableModel model;

	protected int score;

	protected JButton submitButton;
	protected JLabel scoreLabel;
	protected int solutionsFound;
	protected GameTimer timer;
	protected JLabel timeLeftLabel;
	protected JLabel puzzleWordLabel;
	protected JButton randomizeButton;

	protected JLabel labelcorrectOrNot;
	/**
	 * Constructor to set up the layout and variables  
	 * @param codeBase get the URL's code base from MainGUI, so it can be used for reading/saving files to the server
	 */
	GameGUI(URL codeBase) {
		this.codeBase = codeBase;
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		getDictionary(this.codeBase);
		this.solutionsFound = 0;
		this.score = 0;
	}

	/**
	 * Create the panel to display the puzzle
	 * @param puzzle get the puzzle word to display
	 * @return the JPanel that contains these objects
	 */
	public JPanel puzzlePanel(final Puzzle puzzle) {
		JPanel puzzlePanel = new JPanel();
		// Get the solutions
		this.hashSolutions = puzzle.getSolutions();

		JLabel puzzleLabel = new JLabel("Puzzle: ");

		puzzleWordLabel = new JLabel(puzzle.toString());
		randomizeButton = new JButton("Randomize");

		// ActionListener for randomizing the letters of the puzzle
		randomizeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				puzzleWordLabel.setText(puzzle.reorderLetters());
			}
		});

		puzzlePanel.add(puzzleLabel);
		puzzlePanel.add(puzzleWordLabel);
		puzzlePanel.add(randomizeButton);

		return puzzlePanel;

	}

	/**
	 * Create panel to display the time and score
	 * @return Panel that contains these components
	 */
	public JPanel timeScorePanel() {
		JPanel timeScorePanel = new JPanel();
		URL timeURL;
		String r = "";
		int time = 0;
		try {
			// Get the time from server
			Date date = new Date(); // to get around caching issues
			timeURL = new URL(codeBase, "files/time.txt?" + date.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					timeURL.openStream()));
			time = Integer.parseInt(br.readLine());

			br.close();
		} catch (NumberFormatException e) {

		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		JLabel timerLabel = new JLabel("Time Remaining: ");
		this.timer = new GameTimer(time, this);

		this.timeLeftLabel = timer.getLabel();
		timeScorePanel.add(timerLabel);
		timeScorePanel.add(timeLeftLabel);

		scoreLabel = new JLabel("Score: " + score);
		timeScorePanel.add(scoreLabel);
		return timeScorePanel;
	}

	/**
	 * When game is done, this method displays all the solutions in the table. 
	 * It also sets objects disabled, so user can't enter more words
	 */
	public void gameOver() {
		Iterator it = hashSolutions.entrySet().iterator();
		// Iterate through the solutions
		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry) it.next();
			String key = pairs.getKey().toString();
			// Get the location of where the solution should appear in the table
			int length = key.length(); // Length of a solution
			int column = length - 3; // Column it should be in (e.g. 3 - 3 = 0)
			Integer d[] = hashSolutions.get(key);
			int row = d[1];
			// If it hasn't be set as the solution yet, set cell as the solution
			if (tableSolutions.getValueAt(row, column) != key) {
				tableSolutions.setValueAt(key, row, column);
			}
		}
		// Disable textfields and buttons
		enteredText.setEnabled(false);
		enteredText.setEditable(false);
		submitButton.setEnabled(false);
		randomizeButton.setEnabled(false);

	}

	/**
	 * Create panel for user to enter and submit words, and generate the solutions table
	 * @param numColumns How many columns the solutions table should contain (4 for kids vs 6 for preset/random) 
	 * @param columnNames The names of the columns 
	 * @return Panel that contains these components
	 */
	public JPanel enterLettersPanel(int numColumns, String[] columnNames) {
		final JPanel enterLettersPanel = new JPanel();

		createTable(numColumns, columnNames);

		JLabel enterLettersLabel = new JLabel("Enter word:");
		enterLettersPanel.add(enterLettersLabel);
		enteredText = new JTextField(6);
		enteredText.addActionListener(this);
		enterLettersPanel.add(enteredText);

		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);

		enterLettersPanel.add(submitButton);
		return enterLettersPanel;
	}

	/**
	 * Create a panel that tells user the word they found is correct or not 
	 * @return Panel that contains these components
	 */
	public JPanel correctOrNot(){
		JPanel correctOrNot = new JPanel();
		labelcorrectOrNot=new JLabel("Solutions");
		correctOrNot.add(labelcorrectOrNot);
		return correctOrNot;
	}

	/**
	 *  Create a panel that contains the solutions table 
	 * @return Panel that contains these components
	 */
	public JPanel foundWordsPanel() {
		JPanel foundWordsPanel = new JPanel();
		tableSolutions = new JTable(model);
		// User can't interact with the table
		tableSolutions.setEnabled(false);
		scrollSolutions = new JScrollPane(tableSolutions);
		scrollSolutions.setPreferredSize(new Dimension(500, 300));
		foundWordsPanel.add(scrollSolutions);

		return foundWordsPanel;

	}

	/**
	 * Method that creates the table of solutions
	 * @param numArrays Number of column arrays
	 * @param columnNames the names of the columns
	 */
	public void createTable(int numArrays, String[] columnNames) {
		arrList = new ArrayList<ArrayList<String>>();

		// e.g. for 6-letter words, 
		// make 4 arraylists to hold words; one for 3-letter, 4-letter, 5-letter,
		// 6-letter
		for (int i = 0; i < numArrays; i++) {
			arrList.add(new ArrayList<String>());
		}
		Iterator it = hashSolutions.entrySet().iterator();

		// Iterate through the solutions
		// In the table, add "?" marks as placeholder to where letters should appear
		while (it.hasNext()) {

			Map.Entry pairs = (Map.Entry) it.next();
			String key = pairs.getKey().toString();

			int length = key.length(); // Length of a solution
			int column = length - 3; // Column it should be in (e.g. 3 - 3 = 0)

			StringBuilder placeHolder = new StringBuilder();
			for (int i = 0; i < length; i++) {
				placeHolder.append("?");
			}

			arrList.get(column).add(placeHolder.toString());

			int row = arrList.get(column).lastIndexOf(placeHolder.toString());

			hashSolutions.put(key, new Integer[] { column, row });
		}

		// Add each array list to a column
		model = new DefaultTableModel();
		for (int i = 0; i < numArrays; i++) {
			model.addColumn(columnNames[i], arrList.get(i).toArray());
		}
	}

	/**
	 * Get the dictionary instance
	 * @param codeBase URL of the server
	 */
	public void getDictionary(URL codeBase) {
		try {
			this.dict = Dictionary
					.instance(new URL(codeBase, "files/dictionary.txt"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Return this panel; used by MainGUI
	 * @return this panel
	 */
	public JPanel getPanel() {
		return this;
	}

	/**
	 * Override default actionPerformed to provide implementation for objects of this class
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submitButton || e.getSource() == enteredText) {
			// Submit a word
			// (user pressed button or hit enter)
			String entered = enteredText.getText();

			// If it's a valid solution
			if (hashSolutions.containsKey(entered)) {
				Integer[] value = hashSolutions.get(entered);
				Integer d[] = hashSolutions.get(entered);
				int col = d[0];
				int row = d[1];
				String tableValue = model.getValueAt(row, col).toString();
				// If the solution hasn't been found yet
				if (tableValue.contains("?")) {
					// Add it to the table
					model.setValueAt(entered, row, col);
					incrementScore(entered);
					++solutionsFound;
					// If user found all solutions, 
					if (solutionsFound == hashSolutions.size()) {
						JOptionPane.showMessageDialog(this,
								"Congrats! You've solved the puzzle.",
								"Congrats!", JOptionPane.DEFAULT_OPTION);
						// Disable components
						enteredText.setEnabled(false);
						enteredText.setEditable(false);
						submitButton.setEnabled(false);
						timer.getTime(); // gets the time right now
						timeLeftLabel.setText("Congrats!");
						timer.stop();
						// Post score to server
						// and time
					}
					labelcorrectOrNot.setText(entered+" is correct.");

				} else {
					// Otherwise, user already found solution
					labelcorrectOrNot.setText("You've already entered " + entered);
				}
			} else {
				// User didn't find a valid solution
				labelcorrectOrNot.setText(entered+ " is not a solution.");

			}
			// clear field
			enteredText.setText("");
			enteredText.requestFocus();
		}
		// If user clicked randomize button
		// reorder the letters
		if (e.getSource()==randomizeButton)
			puzzleWordLabel.setText(puzzle.reorderLetters());


	}

	/**
	 * Method for incrementing the score
	 * @param word
	 */
	public void incrementScore(String word) {
		int length = word.length();
		if (length == 6) {
			score += 50;
		}
		score += 40 * length;
		scoreLabel.setText("Score: " + score);
	}
}