import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Class: KidsGameGUI
 * Purpose: Subclasses GameGUI to make use of its methods.
 * Only difference with other game modes are it only have 2 column of solutions, and puzzles are random 4-letter words
 * It extends GameGUI and implements ActionListener
 * @author Christina
 *
 */
public class KidsGameGUI extends GameGUI implements ActionListener {

	private JPanel panel;

	/**
	 * Constructor that adds panels created by the super class's methods to this panel
	 * @param codeBase
	 */
	KidsGameGUI(URL codeBase) {
		super(codeBase);
		this.codeBase=codeBase;
		
		panel= new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		// Create a kids puzzle
		this.puzzle = new Puzzle(true, super.dict);

		panel.add(super.puzzlePanel(puzzle));
		panel.add(super.timeScorePanel());
		
		// Create 2 columns
		String[] columnNames = { "3-Letter", "4-Letter" };
		panel.add(super.enterLettersPanel(2, columnNames));
		panel.add(super.correctOrNot());

		panel.add(super.foundWordsPanel());
	}

	/**
	 * Return panel that contains all the components for kids mode
	 * @return panel
	 */
	public JPanel getPanel(){
		return panel;
	}
	
		
	
		

}
