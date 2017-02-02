import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
/**
 * Class: MainGUI
 * Purpose: MainGui that sets up the different screens and allows user to navigate between the different screens. 
 * It extends JApplet and implements ActionListener
 * @author Christina, Mark (a method); changed from Jackie's initial version
 *
 */
public class MainGUI extends JApplet implements ActionListener {

	private JMenuBar menuBar;
	private JMenu[] menus;
	private JMenuItem[][] menuItems;

	private Container container;

	private URL codeBase;

	// Button titles
	private String[][] itemTitle = {
			{ "Main Screen", "Admin Mode", "Change Skin" },
			{ "Preset Game", "Random Game", "Kids Game", "100 Words Game" },
			{ "Preset Game Statistics" } };
	private ArrayList<String> buttonTitle;

	private SkinGUI skin;

	private Dictionary dictionary;

	private Player currentPlayer;
	private String uid;
	private String name;

	/**
	 * Initiate the JApplet 
	 * Get uid and player's name
	 * Initialize the main screen with buttons
	 */
	public void init() {
		codeBase=this.getCodeBase();
		skin = new SkinGUI(this, codeBase);
		this.uid=getParameter("uid");
		this.name=getParameter("name");
		createMenu();
		container = getContentPane();

		Main main = new Main(buttonTitle, name);
		JButton[] buttons = main.getButtons();
		for (int i=0; i<buttons.length; i++){
			buttons[i].addActionListener(this);
		}
		container.add(main.getPanel());

		currentPlayer = new Player(uid, name, null);
		popFriend();
	}

	/**
	 * Create the top menu bar that exists on all screens
	 * Sets up the main menus and their submenus (drop down listings)
	 */
	private void createMenu() {
		buttonTitle = new ArrayList<String>();
		menuBar = new JMenuBar();
		int menuSize = 3;
		menus = new JMenu[menuSize];
		menuItems = new JMenuItem[menuSize][4];
		String[] menuTitle = { "Main", "Game", "Statistics" };

		menuItems[0][0] = new JMenuItem("Main Screen");
		menuItems[0][1] = new JMenuItem("Admin Mode");
		menuItems[0][2] = new JMenuItem("Change Skin");

		menuItems[1][0] = new JMenuItem("Preset Game");
		menuItems[1][1] = new JMenuItem("Random Game");
		menuItems[1][2] = new JMenuItem("Kids Game");
		menuItems[1][3] = new JMenuItem("100 Words Game");

		menuItems[2][0] = new JMenuItem("Preset Game Statistics");

		for (int i = 0; i < menus.length; i++) {
			menus[i] = new JMenu(menuTitle[i]);
			menuBar.add(menus[i]);
			for (int j = 0; j < itemTitle[i].length; j++) {
				buttonTitle.add(itemTitle[i][j]);
				menuItems[i][j] = new JMenuItem(itemTitle[i][j]);
				menuItems[i][j].addActionListener(this);
				menus[i].add(menuItems[i][j]);

			}
		}
		setJMenuBar(menuBar);
	}

	/**
	 * Populate user with their friends information
	 * (Mark, modified by Christina)
	 */
	public void popFriend(){
		Date date = new Date(); // to get around caching issues
		try {
			URL oracle = new URL(codeBase+ "files/friendList.txt?"+date.toString());
			URLConnection yc = oracle.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			StringTokenizer tokens;	//used to separate all player's names with their fbID
			Player friend;
			ArrayList<GameStatistics> statsList = null;
			String fbID, name;
			while ((inputLine = in.readLine()) != null) {
				tokens = new StringTokenizer(inputLine, "||");
				fbID = tokens.nextToken();		// remove the fbID
				name = tokens.nextToken();

				friend = new Player(fbID, name, null);
				try {
					// get each friend's puzzles they have played
					URL playerStats = new URL(codeBase+ "files/players/"+fbID+".txt?"+date.toString());
					URLConnection connection2 = playerStats.openConnection();
					BufferedReader in2 = new BufferedReader(new InputStreamReader(connection2.getInputStream()));
					String input;
					GameStatistics stats;
					statsList = new ArrayList<GameStatistics>();
					while ((input = in2.readLine()) != null) {
						//need to make sure the current puzzle is still valid (has not been deleted by admin)
						StringTokenizer friendStats = new StringTokenizer(input, "||");	// default slice is " "
						// constructor is:  String puzzleletters, int score, int timeFound6letterword
						stats = new GameStatistics(friend, friendStats.nextToken(), Integer.parseInt(friendStats.nextToken()), Integer.parseInt(friendStats.nextToken()));
						statsList.add(stats);
					}
					// update the friend's statistics after just receiving all their puzzle stats
					friend.setStats(statsList);
					currentPlayer.addFriend(friend);
					//}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}

			in.close();
		} catch (Exception e3) {
		}
	}

	/**
	 * Method performed when applet is started
	 * Gets the dictionary
	 * Avoids the dictionary taking too long to load and not have the game working
	 */
	public void start() {

		getDictionary();

	}

	/**
	 * Get the dictionary from server
	 */
	public void getDictionary(){
		try {
			this.dictionary = Dictionary
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
	 * Override default method to provide actions for the buttons/menu clicked on
	 * Make use of the titles of the buttons and the menu, as they both have the same
	 * Every time a button is created, a new instance of the desired class is created so that 
	 * user is playing a new game, or login into admin again
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("Main Screen")) {
			container.removeAll();
			Main main = new Main(buttonTitle, name);
			JButton[] buttons = main.getButtons();
			for (int i=0; i<buttons.length; i++){
				buttons[i].addActionListener(this);
			}
			container.add(main.getPanel());
			container.repaint();
			container.validate();

			this.repaint();
			this.validate();
		} else if (command.equals("Admin Mode")) {
			container.removeAll();
			AdminGUI admin = new AdminGUI(codeBase);
			container.add(admin.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();
		} else if (command.equals("Change Skin")) {
			container.removeAll();
			SkinGUI newSkin = new SkinGUI(this, codeBase);
			container.add(newSkin.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();
		} else if (command.equals("Preset Game")) {
			container.removeAll();
			PresetGameGUI preset = new PresetGameGUI(codeBase, currentPlayer);
			container.add(preset.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();
		} else if (command.equals("Random Game")) {
			container.removeAll();
			RandomGameGUI random = new RandomGameGUI(codeBase);
			container.add(random.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();
		} else if (command.equals("Kids Game")) {
			container.removeAll();
			KidsGameGUI kids = new KidsGameGUI(codeBase);
			container.add(kids.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();
		} else if (command.equals("100 Words Game")) {
			container.removeAll();
			HundWordGUI hundred = new HundWordGUI(codeBase, currentPlayer);
			container.add(hundred.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();
		} else if (command.equals("Preset Game Statistics")) {
			container.removeAll();
			StatisticsGUI stats = new StatisticsGUI(codeBase, getParameter("accessToken"), currentPlayer);
			container.add(stats.getPanel());
			container.repaint();
			container.validate();
			this.repaint();
			this.validate();

		}


	}

}
