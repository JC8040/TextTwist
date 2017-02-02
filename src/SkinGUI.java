import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
/**
 * Class: SkinGUI
 * Purpose: Creates panel for user to pick the skin for applet
 * Extends JPanel and implements ActionListener
 * @author Christina; changed from Jackie's initial version
 *
 */
public class SkinGUI extends JPanel implements ActionListener {
	private URL codeBase;

	private LinkedList<String> lookAndFeelDisplayName;
	private LinkedList<String> lookAndFeel;

	private ButtonGroup buttonGroup;
	private JRadioButton btnNimbus, btnMotif, btnWindows;
	private JLabel setLookAndFeel;
	private JPanel panel;

	private String[] listSkins;
	private ArrayList<JRadioButton> radioButtons;
	private JApplet applet;
	private ButtonGroup group;

	/**
	 * Constructor that gets the applet to be modified (so that this class can modify it's skin) 
	 * and the url of the server for read/write purposes
	 * @param applet 
	 * @param codeBase
	 */
	SkinGUI(JApplet applet, URL codeBase) {
		this.panel = new JPanel();
		this.codeBase = codeBase;
		this.applet = applet;
		createRadioButtons();
		// Set the skin
		if (!getSkin().isEmpty()){
			setSkin(getSkin());
		}
	}

	/**
	 * Method creates radio buttons for changing the skin
	 */
	public void createRadioButtons() {
		JLabel skinLabel = new JLabel("Choose a Skin");
		panel.add(skinLabel);
		listSkins = new String[3];
		listSkins[0] = "javax.swing.plaf.metal.MetalLookAndFeel";
		listSkins[1] = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
		listSkins[2] = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
		radioButtons = new ArrayList<JRadioButton>();
		radioButtons.add(new JRadioButton("Metal"));
		radioButtons.add(new JRadioButton("Nimbus"));
		radioButtons.add(new JRadioButton("Motif"));
		group = new ButtonGroup();
		for (int i = 0; i < radioButtons.size(); i++) {
			// Add each button to group
			group.add(radioButtons.get(i));
			// Add action listener
			radioButtons.get(i).addActionListener(this);
			panel.add(radioButtons.get(i));
		}
	}

	/**
	 * return this panel
	 * @return panel
	 */
	public JPanel getPanel() {
		return panel;
	}

	/**
	 * Get the skin chosen from the server
	 * @return skin saved in server
	 */
	public String getSkin() {
		try {
			Date date = new Date(); // to get around caching issues
			URL skinFile = new URL(codeBase, "files/skin.txt?"
					+ date.toString());
			BufferedReader br = new BufferedReader(new InputStreamReader(
					skinFile.openStream()));
			String line = br.readLine();

			return line;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Set the skin for the applet
	 * @param skin
	 */
	public void setSkin(String skin) {
		try {
			UIManager.setLookAndFeel(skin);
			SwingUtilities.updateComponentTreeUI(applet);
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}
	
	/**
	 * Save changed skin to the server
	 * @param skin
	 * @return true if skin saved
	 */
	public boolean saveSkin(String skin){
		try {
			URL skinURL = new URL(codeBase + "files/writeskin.php");
			URLConnection connection = skinURL.openConnection();
			connection.setDoOutput(true); // Open output connection
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			// Get the output stream writer, so information can be written to
			// the URL

			OutputStreamWriter out = new OutputStreamWriter(
					connection.getOutputStream());
			out.write("skin=" + skin);
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
	 * When user clicks on a radio button, change and save the skin
	 */
	public void actionPerformed(ActionEvent e) {
		int selectedButton = radioButtons.indexOf(e.getSource());
		String skin = listSkins[selectedButton];
		setSkin(skin);
		saveSkin(skin);
	}

}
