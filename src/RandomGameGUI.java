import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class: RandomGameGUI
 * Purpose: Subclasses GameGUI to make use of its methods.
 * Only difference with other game modes are it has 4 columns for the solutions, 
 * and it creates a new random puzzle each time it's called
 * It extends GameGUI
 * @author Christina
 *
 */
public class RandomGameGUI extends GameGUI{

	private JPanel panel;
	/**
	 * Constructor that adds panels created by the super class's methods to this panel
	 * @param codeBase
	 */
	RandomGameGUI(URL codeBase) {
		super(codeBase);
		panel=new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		// Creates a random puzzle
		this.puzzle = new Puzzle( super.dict);

		panel.add(super.puzzlePanel(puzzle));
		panel.add(super.timeScorePanel());
		// Creates 4 columns for the solutions
		String[] columnNames = { "3-Letter", "4-Letter", "5-Letter", "6-Letter" };

		panel.add(super.enterLettersPanel(4, columnNames));
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