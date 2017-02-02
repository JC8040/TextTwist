import java.awt.Dimension;
import java.util.*;

import javax.swing.*;

/**
 * Class: Main
 * Purpose: Creates main screen and buttons to be displayed
 * @author Christina
 *
 */
public class Main extends JPanel{

	private JPanel panel;
	private JButton[] buttons;
	/**
	 * Constructor that takes the button title and player name. 
	 * Creates button and other components for this screen
	 * @param buttonTitle array list of the buttons' titles
	 * @param name of current player
	 */
	Main(ArrayList<String> buttonTitle, String name){
		panel = new JPanel();
		panel.add(Box.createRigidArea(new Dimension(20,20)));

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		JLabel welcome = new JLabel("Welcome to Text Twist, "+name);
		welcome.setAlignmentX(CENTER_ALIGNMENT);

		panel.add(welcome);

		buttons = new JButton[8];
		for (int i=0; i<buttons.length; i++){
			buttons[i]= new JButton(buttonTitle.get(i));
			if (i==0){
				// Don't want the Main Screen button on the main screen
				buttons[i].setVisible(false);
			}
			else{
				panel.add(Box.createRigidArea(new Dimension(20,20)));
				buttons[i].setAlignmentX(CENTER_ALIGNMENT);
				buttons[i].setMaximumSize(new Dimension(200,30));
				buttons[i].setMinimumSize(new Dimension(200,30));
				buttons[i].setPreferredSize(new Dimension(200,30));
				buttons[i].setSize(new Dimension(200,30));

				panel.add(buttons[i]);
			}
		}

	}

	/**
	 * Return this panel
	 * @return panel
	 */
	public JPanel getPanel(){
		return panel;
	}

	/**
	 * Return the buttons created
	 * @return array of created buttons
	 */
	public JButton[] getButtons(){
		return buttons;
	}
}
