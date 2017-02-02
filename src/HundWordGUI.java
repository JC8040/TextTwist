import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
/**
 * Class: HundWordGUI
 * Purpose: Panel for 100 word game and statistics
 * It extends JPanel and implements ActionListener
 * @author Tim, Mark (Statistics stuff), Christina (integrated files by Tim and Mark to make it work)
 *
 */
public class HundWordGUI extends JPanel implements ActionListener {

	private int rowcount = 0, colcount = 0;
	private static final long serialVersionUID = 1L;

	private JPanel hundPanel;
	private JLabel title, subtitle;
	private JTextField input;

	private Hashtable<String, Integer> WordHash;

	private JButton endGame;
	private JButton submit;
	private JTable table;
	private JScrollPane scrollTable;

	private JLabel soltionLabel;
	private JPanel welcomePanel;
	private JPanel statsPanel ;
	private JPanel submitPanel;

	private JPanel solutionPanel;

	private JLabel scoreLabel;
	//private JLabel timeLabel;
	private int score=0;

	private URL codeBase;
	private Player player;
	private String uid;
	private String name;
	private int frequency;
	private int lastScore;
	private int highestScore;
	private int highestFreq;

	private JLabel previousStatsLabel;
	private JLabel highLabel;
	private JLabel freqLabel;

	private ArrayList<String> foundWords;

	private JPanel yourStatsPanel;

	private JLabel inputHelper;
	/**
	 * Constructor to initalize variables and set up components
	 * @param codeBase URL of server
	 * @param player Current player
	 */
	HundWordGUI(URL codeBase, Player player) {
		this.codeBase=codeBase;
		this.player=player;
		this.uid=player.getfbID();
		this.name=player.getName();
		foundWords=new ArrayList<String>();
		frequency=0;
		lastScore=0;
		highestScore =0;
		highestFreq=0;
		createFile();
		initFrame();

		getStats();
		getHighestScore();
		getMostFreq();

	}

	/**
	 * Return panel containing objects belonging to it
	 * @return panel
	 */
	public JPanel getPanel() {
		return hundPanel;
	}

	/**
	 * Create table model
	 */
	public DefaultTableModel tableModel = new DefaultTableModel() {

		public int getColumnCount() {
			return 5;
		}

		public int getRowCount() {
			return 20;
		}

		public boolean isCellEditable(int row, int column) {
			// all cells false
			return false;
		}

		public void setValueAt(String value, int row, int col) {
			String[][] rowData = null;
			rowData[row][col] = value;
			fireTableCellUpdated(row, col);
		}
	};

	/**
	 * create objects to be used in this panel
	 */
	public void creation() {

		prepairWordHash();

		hundPanel = new JPanel();
		hundPanel.setLayout(new BoxLayout(hundPanel, BoxLayout.PAGE_AXIS));
		welcomePanel = new JPanel();
		welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.PAGE_AXIS));

		statsPanel  = new JPanel();
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
		submitPanel=new JPanel();

		yourStatsPanel = new JPanel();
		yourStatsPanel.setLayout(new BoxLayout(yourStatsPanel, BoxLayout.PAGE_AXIS));

		solutionPanel = new JPanel();
		solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.PAGE_AXIS)); 

		subtitle = new JLabel(
				"Can you name the 100 most common words in the English language?");
		subtitle.setAlignmentX(CENTER_ALIGNMENT);
		previousStatsLabel=new JLabel("Previous statistics:");
		previousStatsLabel.setAlignmentX(CENTER_ALIGNMENT);
		highLabel=new JLabel("Highest score: ");
		highLabel.setAlignmentX(CENTER_ALIGNMENT);

		freqLabel=new JLabel("Most frequent player: ");
		freqLabel.setAlignmentX(CENTER_ALIGNMENT);

		scoreLabel= new JLabel("Your current Score: " +score);
		scoreLabel.setAlignmentX(CENTER_ALIGNMENT);

		submit = new JButton("Submit");

		endGame = new JButton ("End game - Save my score");
		endGame.setAlignmentX(CENTER_ALIGNMENT);

		inputHelper =new JLabel("Enter a word:");
		input = new JTextField(10);
		input.setEditable(true);

		soltionLabel = new JLabel("Solutions:");
		soltionLabel.setAlignmentX(CENTER_ALIGNMENT);

		table = new JTable(tableModel);
		table.setAlignmentX(CENTER_ALIGNMENT);

		table.setVisible(true);
		table.setEnabled(false);

	}

	/**
	 * Initiate components by adding them to the panel
	 */
	public void initFrame() {
		creation();
		setupEventHandler();

		welcomePanel.add(subtitle);
		hundPanel.add(welcomePanel);

		TitledBorder overallStatsBorder= BorderFactory.createTitledBorder("Overall Statistics");
		statsPanel.setBorder(overallStatsBorder);
		statsPanel.add(highLabel);
		statsPanel.add(freqLabel);
		hundPanel.add(statsPanel);

		TitledBorder yourStatsBorder= BorderFactory.createTitledBorder("Your Statistics");
		yourStatsPanel.setBorder(yourStatsBorder);
		yourStatsPanel.add(previousStatsLabel);
		yourStatsPanel.add(scoreLabel);

		hundPanel.add(yourStatsPanel);

		submitPanel.add(inputHelper);
		submitPanel.add(input);
		submitPanel.add(submit);
		hundPanel.add(submitPanel);

		solutionPanel.add(endGame);
		solutionPanel.add(soltionLabel);
		scrollTable = new JScrollPane(table);
		solutionPanel.add(scrollTable);

		hundPanel.add(Box.createRigidArea(new Dimension(10,10)));
		hundPanel.add(solutionPanel);

	}

	/**
	 * Initalize solutions table
	 */
	public void createTable() {
		int row = tableModel.getRowCount();
		int col = tableModel.getColumnCount();

		for (int i=0; i<row; i++){
			for (int j=0; j<col; j++){
				tableModel.setValueAt("", row, col);
			}
		}
	}

	/**
	 * Create user file to contain their stats for the 100 word game
	 */
	public void createFile(){

		try {
			URL saveStats = new URL(codeBase + "files/createHundFile.php");
			URLConnection connection = saveStats.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("uid=" + uid);
			out.flush();
			out.close();
			connection.getInputStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Set up action listeners
	 */
	public void setupEventHandler() {
		input.addActionListener(this);
		submit.addActionListener(this);
		endGame.addActionListener(this);

	}

	/**
	 * When game is over, disable components and save score and frequency
	 */
	public void gameOver (){
		if (score!=0)
			frequency++;
		input.setEditable(false);
		input.setEnabled(false);
		submit.setEnabled(false);
		endGame.setEnabled(false);
		soltionLabel.setText("Game Over");
		// Save score
		saveHundScore();
		getStats();
		getHighestScore();
		getMostFreq();
	}

	/**
	 * Get current player's stats
	 * @return true if stats retrieved
	 */
	private boolean getStats(){

		try {
			Date date = new Date();
			URL playerURL = new URL(codeBase+"files/100WordStats/"+uid+".txt?"+date.toString());
			URLConnection playerCon = playerURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					playerCon.getInputStream()));

			String inputLine = in.readLine();		// only one line in file

			// line is  frequency||score
			if (inputLine==null){
				return false;
			}
			StringTokenizer tokens = new StringTokenizer(inputLine, "||");


			frequency = Integer.parseInt(tokens.nextToken());
			lastScore = Integer.parseInt(tokens.nextToken());

			previousStatsLabel.setText("Your have played: "+frequency+" times. Your highest score: "+lastScore);
			previousStatsLabel.revalidate();

			in.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Loads the player's score and name with the HIGHEST SCORE
	 * @return true if saved
	 */
	private boolean getHighestScore(){

		try {
			Date date = new Date();
			URL high = new URL(codeBase+"files/100WordStats/highscore.txt?"+date.toString());
			URLConnection playerCon = high.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					playerCon.getInputStream()));

			String inputLine = in.readLine();		// only one line in file
			if (inputLine==null)
				return false;
			// line is  score||name

			StringTokenizer tokens = new StringTokenizer(inputLine, "||");
			String highName;

			// if the file was JUST created, so there is no highest score saved on the server
			if (tokens.countTokens() != 2)
			{
				highestScore =0;
				highName = "No one";

			} else {
				highestScore = Integer.parseInt(tokens.nextToken());
				highName = tokens.nextToken();		
			}

			highLabel.setText(highName+" has the highest score of "+highestScore);
			highLabel.revalidate();

			in.close();
			return true;
		} catch (Exception e) {
			return false;
			//errorlabel.setText("Error loading " + e.getMessage());
		}
	}

	/**
	 * Loads the player's frequency and name with the MOST TIMES PLAYED
	 * @return true if saved
	 */
	private boolean getMostFreq(){

		try {
			Date date = new Date();
			URL high = new URL(codeBase+"files/100WordStats/mostplayed.txt?"+date.toString());
			URLConnection playerCon = high.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					playerCon.getInputStream()));

			String inputLine = in.readLine();		// only one line in file
			// line is  frequency||name
			if (inputLine==null)
				return false;
			StringTokenizer tokens = new StringTokenizer(inputLine, "||");

			String highName;


			highestFreq = Integer.parseInt(tokens.nextToken());
			highName = tokens.nextToken();			

			freqLabel.setText("Most frequent player "+highName + " played "+highestFreq+" times");
			//freqLabel.getParent().revalidate();
			freqLabel.revalidate();

			in.close();
			return true;
		} catch (Exception e) {
			return false;
			//errorlabel.setText("Error loading " + e.getMessage());
		}
	}

	/**
	 * save highest scored player	
	 * @param newScore
	 * @return true is saved
	 */
	private boolean saveHighestScore(int newScore){
		try {
			URL saveHighScore = new URL(codeBase + "files/modifyHighScore.php?");
			URLConnection connection = saveHighScore.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("name=" + name);
			out.write("&score=" + newScore);

			out.flush();
			out.close();
			connection.getInputStream();
			return true;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}

	/**
	 * Save the highest frequency and their name
	 */
	private void saveHighestFreq(){

		try {
			URL saveFreq = new URL(codeBase + "files/modifyMostPlayed.php");
			URLConnection connection = saveFreq.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("name=" + name);
			out.write("&frequency=" + frequency);

			out.flush();
			out.close();
			connection.getInputStream();
		} catch (Exception e) {
		}
	}

	/**
	 * Save current player's stats 
	 */
	private void saveHundScore() {
		try {
			int newScore=score;
			if (score<lastScore){
				newScore = lastScore;	//only save their highestscore
			}

			URL saveScore = new URL(codeBase + "files/modify100WordStats.php");

			URLConnection connection = saveScore.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("uid=" + uid);
			out.write("&score=" + newScore); 
			out.write("&frequency=" + frequency);

			out.flush();
			out.close();
			connection.getInputStream();

			if (newScore > highestScore) {
				saveHighestScore(newScore);
			}

			if (frequency > highestFreq) {
				saveHighestFreq();
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * Override default action listener method for objects in this class
	 */
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == submit || e.getSource()==input) {
			// If user hit submit or enter to submit a word
			String entered = input.getText().toLowerCase();
			// Check if the word is in the solutions
			if (WordHash.containsKey(entered) == true) {
				// Get the location of where the solution should appear
				int number = (Integer) WordHash.get(input.getText());
				rowcount = number % 20 - 1;
				if (rowcount == -1) {
					rowcount = 19;
				}
				colcount = ((number - 1) / 20);
				// If they haven't found the word yet
				// add it to the table
				if (foundWords.isEmpty()||!foundWords.contains(entered)){
					table.setValueAt(input.getText(), rowcount, colcount);
					soltionLabel.setText(input.getText()+" is correct");
					score++;
					scoreLabel.setText("Your current Score: "+score);
					foundWords.add(entered);
				}
				else{
					// Otherwise, they've found the word already
					soltionLabel.setText("You've already entered "+entered);
				}

				// If found all words, call game over
				if (score ==100){
					gameOver();
				}

			}
			else{
				// Otherwise, the word isn't in the list
				soltionLabel.setText(input.getText()+" is not a common word");
			}
			input.setText("");
			input.requestFocus();
		}
		else if (e.getSource()==endGame)
		{
			// If user pressed end game, call game over
			gameOver();
		}

	}

	/**
	 * Set up the hash table with the 100 words
	 */
	public void prepairWordHash() {

		WordHash = new Hashtable<String, Integer>();
		WordHash.put("the", new Integer(1));
		WordHash.put("be", new Integer(2));
		WordHash.put("to", new Integer(3));
		WordHash.put("of", new Integer(4));
		WordHash.put("and", new Integer(5));
		WordHash.put("a", new Integer(6));
		WordHash.put("in", new Integer(7));
		WordHash.put("that", new Integer(8));
		WordHash.put("have", new Integer(9));
		WordHash.put("i", new Integer(10));
		WordHash.put("it", new Integer(11));
		WordHash.put("for", new Integer(12));
		WordHash.put("not", new Integer(13));
		WordHash.put("on", new Integer(14));
		WordHash.put("with", new Integer(15));
		WordHash.put("he", new Integer(16));
		WordHash.put("as", new Integer(17));
		WordHash.put("you", new Integer(18));
		WordHash.put("do", new Integer(19));
		WordHash.put("at", new Integer(20));
		WordHash.put("this", new Integer(21));
		WordHash.put("but", new Integer(22));
		WordHash.put("his", new Integer(23));
		WordHash.put("by", new Integer(24));
		WordHash.put("from", new Integer(25));
		WordHash.put("they", new Integer(26));
		WordHash.put("we", new Integer(27));
		WordHash.put("say", new Integer(28));
		WordHash.put("her", new Integer(29));
		WordHash.put("she", new Integer(30));
		WordHash.put("or", new Integer(31));
		WordHash.put("an", new Integer(32));
		WordHash.put("will", new Integer(33));
		WordHash.put("my", new Integer(34));
		WordHash.put("one", new Integer(35));
		WordHash.put("all", new Integer(36));
		WordHash.put("would", new Integer(37));
		WordHash.put("there", new Integer(38));
		WordHash.put("their", new Integer(39));
		WordHash.put("what", new Integer(40));
		WordHash.put("so", new Integer(41));
		WordHash.put("up", new Integer(42));
		WordHash.put("out", new Integer(43));
		WordHash.put("if", new Integer(44));
		WordHash.put("about", new Integer(45));
		WordHash.put("who", new Integer(46));
		WordHash.put("get", new Integer(47));
		WordHash.put("which", new Integer(48));
		WordHash.put("go", new Integer(49));
		WordHash.put("me", new Integer(50));
		WordHash.put("when", new Integer(51));
		WordHash.put("make", new Integer(52));
		WordHash.put("can", new Integer(53));
		WordHash.put("like", new Integer(54));
		WordHash.put("time", new Integer(55));
		WordHash.put("no", new Integer(56));
		WordHash.put("just", new Integer(57));
		WordHash.put("him", new Integer(58));
		WordHash.put("know", new Integer(59));
		WordHash.put("take", new Integer(60));
		WordHash.put("people", new Integer(61));
		WordHash.put("into", new Integer(62));
		WordHash.put("year", new Integer(63));
		WordHash.put("your", new Integer(64));
		WordHash.put("good", new Integer(65));
		WordHash.put("some", new Integer(66));
		WordHash.put("could", new Integer(67));
		WordHash.put("them", new Integer(68));
		WordHash.put("see", new Integer(69));
		WordHash.put("other", new Integer(70));
		WordHash.put("than", new Integer(71));
		WordHash.put("then", new Integer(72));
		WordHash.put("now", new Integer(73));
		WordHash.put("look", new Integer(74));
		WordHash.put("only", new Integer(75));
		WordHash.put("come", new Integer(76));
		WordHash.put("its", new Integer(77));
		WordHash.put("over", new Integer(78));
		WordHash.put("think", new Integer(79));
		WordHash.put("also", new Integer(80));
		WordHash.put("back", new Integer(81));
		WordHash.put("after", new Integer(82));
		WordHash.put("use", new Integer(83));
		WordHash.put("two", new Integer(84));
		WordHash.put("how", new Integer(85));
		WordHash.put("our", new Integer(86));
		WordHash.put("work", new Integer(87));
		WordHash.put("first", new Integer(88));
		WordHash.put("well", new Integer(89));
		WordHash.put("way", new Integer(90));
		WordHash.put("even", new Integer(91));
		WordHash.put("new", new Integer(92));
		WordHash.put("want", new Integer(93));
		WordHash.put("because", new Integer(94));
		WordHash.put("any", new Integer(95));
		WordHash.put("these", new Integer(96));
		WordHash.put("give", new Integer(97));
		WordHash.put("day", new Integer(98));
		WordHash.put("most", new Integer(99));
		WordHash.put("us", new Integer(100));
	}

}
