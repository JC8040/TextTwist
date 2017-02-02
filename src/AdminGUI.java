import javax.swing.*;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
/**
 * Class: AdminGUI
 * Purpose: Display Admin Screen and functionality. (See method headers for details)
 * It extends JPanel and implements ActionListener
 * @author Christina; Jackie-some of it 
 *
 */
public class AdminGUI extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel outerPanel;

	private JPasswordField newPasswordField;
	private JButton chgpwButton; // change password
	private JButton changeSecurityQButton; // change security question
	private JTextField addpresetField; // textbox to hold preset puzzle
	private JButton addpresetButton; // add preset puzzle

	private JList listPuzzle;
	private JScrollPane scrollList;

	private JButton deletepuzzleButton; // delete puzzle
	private JTextField bulkLoadField; // textbox for getting bulkload puzzle url
	private JButton bulkloadpuzzleButton; // bulkload puzzle file

	private SpinnerModel timeSpinnerModel;
	private JSpinner timeSpinner;
	private JButton settimerButton; // set timer button

	private JTextField dictionaryField; // text field for dictionary
	private JButton dictButton; // change dictionary file

	private JPanel prePasswordPanel;
	private JButton preSubmitButton, preForgotPasswordButton;
	private JTextField prePasswordEntered;
	private JLabel prePasswordLabel;
	private int prePasswordTries;

	private URL codeBase;

	private JPanel passwordPanel;
	private JPanel puzzlePanel;
	private JPanel bulkloadPanel;
	private JPanel timerPanel;
	private JPanel dictionaryPanel;

	private TitledBorder passwordBorder;
	private TitledBorder puzzleBorder;
	private TitledBorder bulkloadBorder;
	private TitledBorder timerBorder;
	private TitledBorder dictionaryBorder;

	private String[] securityQuestions = new String[] { "What is the course name?",
			"What is the group number?", "What is the instructor's name?" };
	private String[] answers = new String[] { "cs2212", "one", "laura" };

	private JComboBox securityQList;
	private JPanel panel;
	private Dictionary dictionary;

	/**
	 * Constructor to create and add components for this panel
	 * @param codeBase get the URL's code base from MainGUI, so it can be used for reading/saving files to the server
	 */
	AdminGUI(URL codeBase) {
		panel = new JPanel();
		this.codeBase = codeBase;
		getDictionary();

		createStuff();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.PAGE_AXIS));

		outerPanel.add(passwordPanel);

		passwordPanel.setLayout(new GridLayout(2, 2));
		passwordPanel.add(newPasswordField);
		passwordPanel.add(chgpwButton);
		securityQList = new JComboBox (securityQuestions);
		securityQList.addActionListener(this);
		passwordPanel.add(securityQList);
		passwordPanel.add(changeSecurityQButton);

		outerPanel.add(puzzlePanel);
		puzzlePanel.setLayout(new BoxLayout(puzzlePanel, BoxLayout.PAGE_AXIS));
		JPanel puzzleTopPabel = new JPanel();
		JPanel puzzleBottomPabel = new JPanel();
		puzzleTopPabel.setLayout(new GridLayout(1, 2));
		puzzleBottomPabel.setLayout(new GridLayout(1, 2));

		puzzleTopPabel.add(addpresetField);
		puzzleTopPabel.add(addpresetButton);
		puzzlePanel.add(puzzleTopPabel);
		puzzlePanel.add(scrollList);
		puzzleBottomPabel.add(new JLabel());
		puzzleBottomPabel.add(deletepuzzleButton);
		puzzlePanel.add(puzzleBottomPabel);

		outerPanel.add(bulkloadPanel);
		bulkloadPanel.setLayout(new GridLayout(1, 2));

		bulkloadPanel.add(bulkLoadField);
		bulkloadPanel.add(bulkloadpuzzleButton);

		outerPanel.add(timerPanel);
		timerPanel.setLayout(new GridLayout(1, 2));
		timerPanel.add(timeSpinner);
		timerPanel.add(settimerButton);

		outerPanel.add(dictionaryPanel);

		dictionaryPanel.setLayout(new GridLayout(1, 2));

		dictionaryPanel.add(dictionaryField);
		dictionaryPanel.add(dictButton);

		panel.setAlignmentX(LEFT_ALIGNMENT);
		panel.setAlignmentY(TOP_ALIGNMENT);
		outerPanel.setAlignmentX(LEFT_ALIGNMENT);
		outerPanel.setAlignmentY(TOP_ALIGNMENT);
		panel.add(outerPanel);
		prePasswordTries = 0;

	}

	/**
	 * Getter method to return this panel; used by MainGUI to switch to this panel
	 * @return this panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Get the instance of dictionary from the dictionary file
	 */
	public void getDictionary() {
		try {
			this.dictionary = Dictionary.instance(new URL(codeBase,
					"files/dictionary.txt"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Method used to change password. 
	 * @param password String
	 */
	public void changePassword(String password) {
		// Password must be between 3 and 6 characters
		if (password.length() < 3 || password.length() > 6) {
			JOptionPane
			.showMessageDialog(
					this,
					"Password must be between 3 and 6 characters. Password not saved. Try Again!",
					"Error", JOptionPane.DEFAULT_OPTION);

		} else if (!savePwd(password)) { // Call savePwd method to save the password to server
			JOptionPane.showMessageDialog(this,
					"Password not saved! Try Again!", "Error",
					JOptionPane.DEFAULT_OPTION);
		} else if (savePwd(password)) {
			JOptionPane.showMessageDialog(this,
					"Password saved as " + password, "Success",
					JOptionPane.DEFAULT_OPTION);

		}
	}
	/**
	 * Method used to reorder letters of loaded puzzles. 
	 * If puzzles form a valid word, the letters are randomized so they don't form valid word. 
	 * @param letters The word to reorder
	 * @return the reordered word
	 */
	public String reorderLetters(String letters) {
		// Add each character to a list
		List<Character> characters = new ArrayList<Character>();
		for (char c : letters.toCharArray()) {
			characters.add(c);
		}

		boolean isAWord = true;
		StringBuilder output = new StringBuilder(letters.length());
		// Reorder letter while it's not a real word
		while (isAWord) {
			// Reorder the letters
			while (characters.size() != 0) {
				int randPicker = (int) (Math.random() * characters.size());
				output.append(characters.remove(randPicker));
			}
			// If the dictionary contains this newly ordered letters, it's
			// a real word, so try again

			if (dictionary.searchDictionary(output.toString())) {
				output = new StringBuilder(letters.length());
				characters = new ArrayList<Character>();
				for (char c : letters.toCharArray()) {
					characters.add(c);
				}
			} else isAWord = false;
		}
		return output.toString();
	}

	/**
	 * Override default actionPerformed method to make action listener work for objects of this class
	 * Some of it by Jackie; modified/changed by Christina
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// Change Password 
		// (If user clicked change password, or had hit enter)
		if (e.getSource() == chgpwButton || e.getSource()==newPasswordField) {
			String password = newPasswordField.getText();
			// Call change password method to change the password
			changePassword(password);
		}else if (e.getSource()==changeSecurityQButton || e.getSource()==securityQList){
			// Change security question/answer 
			// (If user pressed change security question button, or selected a different security question from the dropdown) 
			int securityQIndex = securityQList.getSelectedIndex();
			// Have a popup that prompts user for the answer to the question
			String securityAnswer = JOptionPane.showInputDialog(this,
					"Answer: " +securityQuestions[securityQIndex]);
			// Answer must be between 1 and 30 characters
			if (securityAnswer.length()<1 || securityAnswer.length()>30){
				JOptionPane.showMessageDialog(this,
						"Answer to security question must be between 1 and 30 characters. Try Again!", "Error",
						JOptionPane.DEFAULT_OPTION);
			}
			else{
				// Save the security question and answer (in lower case)
				saveSecurityQA(securityQuestions[securityQIndex], securityAnswer.toLowerCase());
			}
		} else if (e.getSource() == addpresetButton || e.getSource()==addpresetField) {
			// Add new preset puzzle
			// If user clicked add preset button or hit enter on the preset text field
			String addPuzzle = addpresetField.getText().toLowerCase();
			ArrayList<String> currentPuzzles = readPuzzle();
			// Reorder letters if the dictionary contains that word
			if (dictionary.searchDictionary(addPuzzle)){
				addPuzzle=reorderLetters(addPuzzle);
			}
			// If list already has the exact same puzzle, won't allow user to add it
			if (currentPuzzles.contains(addPuzzle)) {
				JOptionPane.showMessageDialog(this, addPuzzle
						+ " is already in the list Try Again!", "Error",
						JOptionPane.DEFAULT_OPTION);
			} else if (validPuzzle(addPuzzle)) { // Check if it's a valid puzzle, based on given criteria
				// Save puzzle, if it is valid
				savePuzzle(addPuzzle);
				JOptionPane.showMessageDialog(this, addPuzzle + " added",
						"Success", JOptionPane.DEFAULT_OPTION);
				// Update list of puzzles
				listPuzzle.setListData(readPuzzle().toArray());
			} else {
			}
			addpresetField.setText("");//Reset field

			this.revalidate();
		} else if (e.getSource() == deletepuzzleButton) {
			// Delete puzzle (user pressed delete button)
			String selectedPuzzle = listPuzzle.getSelectedValue().toString();
			// Try to delete puzzle
			if (deletePuzzle(selectedPuzzle)) {
				// Update list of puzzles
				listPuzzle.setListData(readPuzzle().toArray());
				JOptionPane.showMessageDialog(this,
						selectedPuzzle + " deleted", "Success",
						JOptionPane.DEFAULT_OPTION);
			} else
				JOptionPane.showMessageDialog(this, "Unable to delete puzzle "
						+ listPuzzle.getSelectedValue() + " ", "Error",
						JOptionPane.DEFAULT_OPTION);
		} else if (e.getSource() == bulkloadpuzzleButton || e.getSource()==bulkLoadField) {
			// Bulk load
			// (User pressed button or hit enter)
			String bulkload = bulkLoadField.getText();
			// Call method to bulkload the puzzles
			if (bulkLoad(bulkload)) {
				JOptionPane.showMessageDialog(this, "Puzzles have been loaded",
						"Success", JOptionPane.DEFAULT_OPTION);
				// Update list of puzzles displayed
				listPuzzle.setListData(readPuzzle().toArray());
				this.revalidate();

			}
		} else if (e.getSource() == settimerButton) {
			// Set timer
			String timeEntered = timeSpinner.getValue().toString();
			int time = 0;

			try {
				time = Integer.parseInt(timeEntered);
				if (time < 10 || time > 120) {
					JOptionPane
					.showMessageDialog(
							this,
							"Please enter a time between 10 seconds and 2 minutes (120 seconds)",
							"Error", JOptionPane.DEFAULT_OPTION);

				} else if (setTime(time)) // Call method to save time
					JOptionPane.showMessageDialog(this, "Time changed to: "
							+ time, "Success", JOptionPane.DEFAULT_OPTION);
				else
					JOptionPane.showMessageDialog(this,
							"Time could not be written", "Error",
							JOptionPane.DEFAULT_OPTION);

			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(this, "Please enter a number",
						"Error", JOptionPane.DEFAULT_OPTION);
			}

		} else if (e.getSource() == dictButton || e.getSource()==dictionaryField) {
			// Change dictionary
			// (User pressed button or hit enter)
			String dictionaryURL = dictionaryField.getText();
			// Call method to save dictionary
			if (setDictionary(dictionaryURL)) {
				JOptionPane
				.showMessageDialog(
						this,
						"New dictionary have been loaded. Deleting invalid preset puzzles...",
						"Success", JOptionPane.DEFAULT_OPTION);
				try {
					dictionary.modifyDictionary(new URL(codeBase,
							"files/dictionary.txt"));
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Iterate through existing puzzles to find puzzles that are no longer valid
				ArrayList<String> currentPuzzles = readPuzzle();
				ArrayList<String> invalidPuzzles = new ArrayList<String>();
				for (int i = 0; i < currentPuzzles.size(); i++) {

					// Get each puzzle
					String aPuzzle = currentPuzzles.get(i);
					ArrayList<String> subwords;
					try {
						subwords = Dictionary.instance(
								new URL(codeBase, "files/dictionary.txt"))
								.getSubwords(aPuzzle, aPuzzle.length());
						// If puzzle can't make subwords, it's invalid
						if (subwords.isEmpty() || subwords.size() <= 0) {
							invalidPuzzles.add(aPuzzle);

						}

					} catch (MalformedURLException e2) {
						invalidPuzzles.add(aPuzzle);

						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch (Exception e2) {
						invalidPuzzles.add(aPuzzle);

						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				}
				// Iterate through each invalid puzzle and delete them
				for (int i = 0; i < invalidPuzzles.size(); i++) {
					if (deletePuzzle(invalidPuzzles.get(i)));

				}
				// Tell user which puzzles have been deleted, if any
				if (invalidPuzzles.size() > 0) {
					JOptionPane.showMessageDialog(this,
							"These puzzles were deleted: " + invalidPuzzles,
							"Success", JOptionPane.DEFAULT_OPTION);
				} else {
					JOptionPane.showMessageDialog(this,
							"All puzzles are still valid.", "Success",
							JOptionPane.DEFAULT_OPTION);
				}
				// Update list of puzzles displayed
				listPuzzle.setListData(readPuzzle().toArray());
				this.revalidate();
			} else {
			}
		} else if (e.getSource() == preSubmitButton || e.getSource()==prePasswordEntered) {
			// Pre Admin Mode - get password entered 
			// (User pressed button or hit enter)		
			String pwd = prePasswordEntered.getText();
			String storePwd = getPwd(); // get password from server
			// If entered password is the same as stored password, 
			if (storePwd != null && storePwd.equals(pwd)) {
				// Let user see admin panel
				outerPanel.setVisible(true);
				prePasswordPanel.setVisible(false);
			} else if (prePasswordTries > 2) {
				// If user tried more than 3 times, they need to answer security question
				answerSecurityQ();
				prePasswordTries = 0;
			} else {
				++prePasswordTries;
				JOptionPane.showMessageDialog(this,
						"Incorrect password! Try Again!", "Error",
						JOptionPane.DEFAULT_OPTION);
			}
		} else if (e.getSource() == preForgotPasswordButton) {
			// Pre Admin - Forgot password
			// (user pressed button)
			// Prompt them to answer security question
			answerSecurityQ();
		}

	}
	/**
	 * User can enter security question. If answered correctly, can save a new password. 
	 * If answered incorrectly, password will be set as default
	 */
	private void answerSecurityQ() {
		ArrayList<String> securityQA = getSecurityQA();
		// If there's no security question set yet
		if (securityQA.isEmpty()) {
			// Reset password to default
			savePwd("cs2212");
			JOptionPane
			.showMessageDialog(
					this,
					"You haven't answered a security question yet. \n"
							+ "Password has been reset to the default password.",
							"Error", JOptionPane.DEFAULT_OPTION);
		} else {
			// Prompt user to enter an answer to the security question
			String answerEntered = JOptionPane.showInputDialog(this,
					securityQA.get(0));
			// If they answered correctly
			if (securityQA.get(1).equals(answerEntered.toLowerCase())) {
				// Prompt to enter a new password, and save it
				String newPassword = JOptionPane.showInputDialog(this,
						"Enter a new password: ");
				changePassword(newPassword);
			} else {
				// If answered incorrectly
				// Reset password to default
				savePwd("cs2212");
				JOptionPane
				.showMessageDialog(
						this,
						"Incorrect security answer. Password has been reset to the default password.",
						"Error", JOptionPane.DEFAULT_OPTION);
			}
		}
	}

	/**
	 * Create all the different components used in this panel
	 */
	public void createStuff() {
		outerPanel = new JPanel();
		outerPanel.setVisible(false);
		intiprePasswordPanel();
		// Password
		prePasswordTries = 0;
		passwordPanel = new JPanel();
		passwordBorder = BorderFactory.createTitledBorder("Password");
		passwordPanel.setBorder(passwordBorder);
		newPasswordField = new JPasswordField(6);
		newPasswordField.addActionListener(this);
		chgpwButton = new JButton("Change Password");
		changeSecurityQButton = new JButton("Change Security Question");

		// Puzzle
		puzzlePanel = new JPanel();
		puzzleBorder = BorderFactory.createTitledBorder("Puzzle");
		puzzlePanel.setBorder(puzzleBorder);
		addpresetField = new JTextField(12);
		addpresetField.addActionListener(this);
		addpresetButton = new JButton("Add Preset Puzzle");
		// List puzzles
		listPuzzle = new JList();
		if (!readPuzzle().isEmpty())
			listPuzzle.setListData(readPuzzle().toArray());
		scrollList = new JScrollPane(listPuzzle);
		scrollList.setPreferredSize(new Dimension(100, 100));
		scrollList.setSize(100, 100);
		// Delete puzzle
		deletepuzzleButton = new JButton("Delete Selected Puzzle");

		// Bulk load
		bulkloadPanel = new JPanel();
		bulkloadBorder = BorderFactory.createTitledBorder("Bulk Load");
		bulkloadPanel.setBorder(bulkloadBorder);
		bulkLoadField = new JTextField(6);
		bulkLoadField.addActionListener(this);
		bulkLoadField.setText("http://");
		bulkloadpuzzleButton = new JButton("Bulkload Puzzle File");

		// Timer
		timerPanel = new JPanel();
		timerBorder = BorderFactory.createTitledBorder("Timer");
		timerPanel.setBorder(timerBorder);
		
		timeSpinnerModel = new SpinnerNumberModel(getTime(), // initial value
				10, // min
				120, // max
				5);
		timeSpinner = new JSpinner(timeSpinnerModel);
		settimerButton = new JButton("Set Timer");

		// Dictionary
		dictionaryPanel = new JPanel();
		dictionaryBorder = BorderFactory.createTitledBorder("Dictionary");
		dictionaryPanel.setBorder(dictionaryBorder);

		dictionaryField = new JTextField(10);
		dictionaryField.addActionListener(this);
		dictionaryField.setText("http://");
		dictButton = new JButton("Load Dictionary File");

		// Add action listener
		chgpwButton.addActionListener(this);
		changeSecurityQButton.addActionListener(this);
		addpresetButton.addActionListener(this);
		deletepuzzleButton.addActionListener(this);
		bulkloadpuzzleButton.addActionListener(this);
		settimerButton.addActionListener(this);
		dictButton.addActionListener(this);
	}

	/**
	 * Initial panel, where user must enter password to enter
	 * Create the panel
	 * (Jackie)
	 */
	private void intiprePasswordPanel() {
		prePasswordPanel = new JPanel();
		initPasswordPanel();
	}

	/**
	 * Initial panel, where user must enter password to enter
	 * Create all the components needed
	 * (Jackie)
	 */
	private void initPasswordPanel() {
		prePasswordLabel = new JLabel("Enter Password: ");
		prePasswordEntered = new JPasswordField(15);
		prePasswordEntered.addActionListener(this);
		preSubmitButton = new JButton("Submit");
		preForgotPasswordButton = new JButton("Forgot Password");

		prePasswordPanel = new JPanel(new FlowLayout());
		prePasswordPanel.add(prePasswordLabel);
		prePasswordPanel.add(prePasswordEntered);
		prePasswordPanel.add(preSubmitButton);
		prePasswordPanel.add(preForgotPasswordButton);

		preSubmitButton.addActionListener(this);
		preForgotPasswordButton.addActionListener(this);
		panel.add(prePasswordPanel);
	}

	/**
	 * Method to get password from server
	 * (Jackie)
	 */
	private String getPwd() {

		try {
			URL passwordFile = new URL(codeBase + "files/readpw.php");
			URLConnection connection = passwordFile.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					connection.getOutputStream()));
			out.write("fileName=password.txt");
			out.flush();
			out.close();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String r = br.readLine();
			br.close();
			return r;

		} catch (IOException e1) {
			return null;
		}

	}

	/**
	 * Method to save password entered to server
	 * @param newPwd the new password to be saved
	 * @return true if saved, false otherwise
	 */
	private boolean savePwd(String newPwd) {
		try {
			URL passwordFile = new URL(codeBase + "files/writepw.php");
			URLConnection connection = passwordFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("newpw=" + newPwd);
			out.flush();
			out.close();
			connection.getInputStream();

			return true;
		} catch (IOException e1) {
			return false;
		}
	}

	/**
	 * The the security question and answer from server
	 * @return the question and answer in an ArrayList
	 */
	private ArrayList<String> getSecurityQA() {
		try {
			Date date = new Date(); // to get around caching issues
			URL securityFile = new URL(codeBase, "files/securityQA.txt?"
					+ date.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					securityFile.openStream()));
			ArrayList<String> securityQA = new ArrayList<String>();
			String line = "";
			while ((line = br.readLine()) != null) {
				securityQA.add(line);
			}
			br.close();
			this.revalidate();

			return securityQA;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

	}

	/**
	 * Save the security question and answer to the server
	 * @param question the security question
	 * @param answer the security answert
	 * @return true if saved, false otherwise
	 */
	private boolean saveSecurityQA(String question, String answer) {
		try {
			URL passwordFile = new URL(codeBase + "files/writesecurityQA.php");
			URLConnection connection = passwordFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			// out.write("newpw=" + newPwd);
			out.write("question=" + question);
			out.write("&answer=" + answer);

			out.flush();
			out.close();
			connection.getInputStream();

			return true;
		} catch (IOException e1) {
			return false;
		}
	}

	/**
	 * Read the puzzles from the server
	 * @return puzzles stored in array list
	 */
	private ArrayList<String> readPuzzle() {
		try {
			Date date = new Date(); // to get around caching issues
			URL puzzleFile = new URL(codeBase, "files/puzzle.txt?"
					+ date.toString());

			BufferedReader br = new BufferedReader(new InputStreamReader(
					puzzleFile.openStream()));
			ArrayList<String> readPuzzles = new ArrayList<String>();
			String line = "";
			while ((line = br.readLine()) != null) {
				readPuzzles.add(line);
			}
			br.close();
			this.revalidate();

			return readPuzzles;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Save a puzzle to the list of puzzles
	 * @param puzzle the puzzle to be saved
	 * @return true if saved, false otherwise
	 */
	private boolean savePuzzle(String puzzle) {
		try {
			URL puzzleFile = new URL(codeBase + "files/writepuzzle.php");
			URLConnection connection = puzzleFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("newpuzzle=" + puzzle);
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
	 * Get the time set for the games
	 * @return time, in seconds as an int
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

	/**
	 * Save the time to the server
	 * @param time 
	 * @return true if saved, false otherwise
	 */
	private boolean setTime(int time) {
		try {
			URL timerFile = new URL(codeBase + "files/writetime.php");
			URLConnection connection = timerFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("newtime=" + time);
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
	 * Delete a puzzle from the server
	 * @param selectedPuzzle the puzzle to be deleted
	 * @return true if deleted, false otherwise
	 */
	private boolean deletePuzzle(String selectedPuzzle) {
		try {
			URL deletePuzzleURL = new URL(codeBase + "files/deletepuzzle.php");
			URLConnection connection = deletePuzzleURL.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("deletepuzzle=" + selectedPuzzle);
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
	 * Save bulk load puzzles to the server 
	 * @param bulkloadURL url to be saved 
	 * @return true if saved, false otherwise
	 */
	private boolean saveBulkloadURL(String bulkloadURL) {
		try {
			// Basic check of url validity
			if (!bulkloadURL.contains("http") || !bulkloadURL.contains(".")) {
				JOptionPane.showMessageDialog(this, bulkloadURL
						+ " URL cannot be loaded. Try Again!", "Error",
						JOptionPane.DEFAULT_OPTION);
				return false;
			}

			
			URL bulkFile = new URL(codeBase + "files/savebulkloadurl.php");
			URLConnection connection = bulkFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("bulkloadURL=" + bulkloadURL);
			out.flush();
			out.close();
			connection.getInputStream();
			return true;
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this, bulkloadURL
					+ " URL cannot be loaded. Try Again!", "Error",
					JOptionPane.DEFAULT_OPTION);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IO Exception. Try Again!",
					"Error", JOptionPane.DEFAULT_OPTION);
			// TODO Auto-generated catch block
			return false;
		}
	}

	/**
	 * Check validity of the bulk load puzzles, and save them as the preset puzzles if they're valid
	 * @param bulkLoad File to be bulkloaded
	 * @return true if saved, false otherwise
	 */
	private boolean bulkLoad(String bulkLoad) {
		try {
			if (!saveBulkloadURL(bulkLoad)) {
				return false;
			}

			// Read from the bulkload file
			Date date = new Date(); // to get around caching issues
			URL bulkURL = new URL(codeBase + "files/puzzlebulk.txt" + "?"
					+ date);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					bulkURL.openStream()));
			String line = "";
			if (!br.ready()) {
				JOptionPane.showMessageDialog(this, bulkLoad
						+ " cannot be read", "Error",
						JOptionPane.DEFAULT_OPTION);
				// replace puzzlebulk.txt with a blank file
				saveBulkloadURL(codeBase + "files/blankfile.txt");
				return false;
			}
			// Read each line and check if it's a valid line
			while ((line = br.readLine()) != null) {
				if (!validPuzzle(line)) {
					// replace puzzlebulk.txt with a blank file
					saveBulkloadURL(codeBase + "files/blankfile.txt");
					return false;
				}
			}
			br.close();

			URL saveBulkLoadURL = new URL(codeBase + "files/writebulkload.php");
			URLConnection connection = saveBulkLoadURL.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("bulkload=" + "blankfile.txt");
			out.flush();
			out.close();
			connection.getInputStream();

			BufferedReader br2 = new BufferedReader(new InputStreamReader(
					bulkURL.openStream()));
			// This time call method to save puzzles
			while ((line = br2.readLine()) != null) {
				String addPuzzle = line;
				// Check if puzzle is a dictionary word
				// Reorder letters if it is
				if (dictionary.searchDictionary(addPuzzle)){
					addPuzzle=reorderLetters(addPuzzle);
				}
				// Save each puzzle
				savePuzzle(addPuzzle);
			}
			br2.close();
			
			return true;

		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this, bulkLoad
					+ " URL cannot be loaded. Try Again!", "Error",
					JOptionPane.DEFAULT_OPTION);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IO Exception. Try Again!",
					"Error", JOptionPane.DEFAULT_OPTION);
			// TODO Auto-generated catch block
			return false;
		}
	}

	/**
	 * Save dictionary words to the server
	 * @param dictionaryURL The file to be saved 
	 * @return true if saved, false otherwise
	 */
	private boolean saveDictionaryURL(String dictionaryURL) {
		try {
			if (!dictionaryURL.contains("http") || !dictionaryURL.contains(".")) {
				JOptionPane.showMessageDialog(this, dictionaryURL
						+ " URL cannot be loaded. Try Again!", "Error",
						JOptionPane.DEFAULT_OPTION);
				return false;
			}

			URL timerFile = new URL(codeBase + "files/savedictionaryurl.php");
			URLConnection connection = timerFile.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("dictionaryURL=" + dictionaryURL);
			out.flush();
			out.close();
			connection.getInputStream();
			return true;
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this, dictionaryURL
					+ " URL cannot be loaded. Try Again!", "Error",
					JOptionPane.DEFAULT_OPTION);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IO Exception. Try Again!",
					"Error", JOptionPane.DEFAULT_OPTION);
			// TODO Auto-generated catch block
			return false;
		}
	}

	/**
	 * Check if dictionary is a valid dictionary
	 * Save it as the new dictionary if it is
	 * @param dictionaryURL Dictionary file to be saved 
	 * @return true if saved, false otherwise
	 */
	private boolean setDictionary(String dictionaryURL) {
		try {
			if (!saveDictionaryURL(dictionaryURL)) {
				return false;
			}
			// Read from the dictionaryURL file
			Date date = new Date(); // to get around caching issues
			URL bulkURL = new URL(codeBase + "files/dictionaryFile.txt" + "?"
					+ date);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					bulkURL.openStream()));
			String line = "";

			// Check if each word makes a valid dictionary word
			while ((line = br.readLine()) != null) {
				// If not valid, exit
				if (!validDictWord(line)) {
					JOptionPane.showMessageDialog(this, line
							+ " is an invalid word. Try Again!", "Error",
							JOptionPane.DEFAULT_OPTION);
					// replace dictionaryFile.txt with a blank file
					saveDictionaryURL(codeBase + "files/blankfile.txt");
					return false;
				}
			}
			br.close();

			// Try to set the new dictionary
			boolean modified = dictionary.modifyDictionary(new URL(codeBase,
					"files/dictionaryFile.txt"));
			// If dictionary changed (that is, new dictionary is valid)
			if (modified) {
				// Save the dictionary, so that next time game is loaded will use this
				URL saveBulkLoadURL = new URL(codeBase
						+ "files/writedictionary.php");
				URLConnection connection = saveBulkLoadURL.openConnection();
				connection.setDoOutput(true); // Open output connection
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// Get the output stream writer, so information can be written
				// to the URL

				OutputStreamWriter out = new OutputStreamWriter(
						connection.getOutputStream());
				out.write("dictionary=" + dictionaryURL.toString());
				out.flush();
				out.close();
				connection.getInputStream();
				return true;

			} else {
				// Otherwise, old dictionary is still being used
				JOptionPane
				.showMessageDialog(
						this,
						"dictionary doesn't have valid 6 or 4-letter words. Try Again!",
						"Error", JOptionPane.DEFAULT_OPTION);
				return false;
			}

		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(this, dictionaryURL
					+ " URL cannot be loaded. Try Again!", "Error",
					JOptionPane.DEFAULT_OPTION);
			return false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IO Exception. Try Again!",
					"Error", JOptionPane.DEFAULT_OPTION);
			// TODO Auto-generated catch block
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Regex to check if dictionary contains valid characters
	 * @param word dictionary word
	 * @return true if valid, false otherwise
	 */
	private boolean validDictWord(String word) {
		return word.matches("^[a-zA-Z'-]+$");
	}

	/**
	 * Check if puzzle is valid
	 * @param puzzle 
	 * @return true if valid, false otherwise
	 */
	private boolean validPuzzle(String puzzle) {
		boolean validPuzzle = false;
		// blank entered
		if (puzzle.isEmpty() || puzzle.trim().length() == 0) {
			JOptionPane.showMessageDialog(this, "Blank line found. ", "Error",
					JOptionPane.DEFAULT_OPTION);
			return false;
		} else if (puzzle.length() != 6) {
			// Puzzle isn't 6 letters
			JOptionPane.showMessageDialog(this, puzzle
					+ " is invalid. Only 6 letters allow. ", "Error",
					JOptionPane.DEFAULT_OPTION);
			return false;
		} else {
			// Iterate through each character and check if it's an alphabet
			for (int i = 0; i < puzzle.length(); i++) {
				if (!Character.isLetter(puzzle.charAt(i))) {
					JOptionPane.showMessageDialog(this, puzzle
							+ " is invalid. Only letters allow.", "Error",
							JOptionPane.DEFAULT_OPTION);
					validPuzzle = false;
					return false;
				} else
					validPuzzle = true;
			}
			ArrayList<String> subwords;
			// Check if word forms a valid puzzle based on the dictionary
			try {
				subwords = Dictionary.instance(
						new URL(codeBase, "files/dictionary.txt")).getSubwords(
								puzzle, puzzle.length());
				// Puzzle invalid if it can't make subwords
				if (subwords.isEmpty() || subwords.size() <= 0) {
					JOptionPane.showMessageDialog(this, puzzle
							+ " cannot make a valid puzzle", "Error",
							JOptionPane.DEFAULT_OPTION);
					return false;
				} else if (validPuzzle) {
					// otherwise, puzzle is valid
					return true;
				} else {
					JOptionPane.showMessageDialog(this, puzzle
							+ " is invalid for some other reason", "Error",
							JOptionPane.DEFAULT_OPTION);
					return false;
				}
			} catch (MalformedURLException e) {
				JOptionPane.showMessageDialog(this, puzzle
						+ " is not a valid preset puzzle", "Error",
						JOptionPane.DEFAULT_OPTION);
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, puzzle
						+ " is not a valid preset puzzle", "Error",
						JOptionPane.DEFAULT_OPTION);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}

}