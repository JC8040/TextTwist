import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.MouseInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.Toolkit;

/**
 * Class: StatisticsGUI
 * Purpose: Creates the panel for displaying the game statistics
 * Extends JPanel
 * @author Mark, Christina modified/integrated with the GUI
 *
 */
public class StatisticsGUI extends javax.swing.JPanel {

	private URL codeBase;

	// current user who is playing the app
	private Player currentPlayer;
	private Date date;

	private DefaultTableModel model;
	private String[] columnNames;
	private String accessToken;					// allows to write to user's wall

	private javax.swing.JButton postWallButton;
	private javax.swing.JLabel labelFastestPlayer;
	private javax.swing.JPanel statsPanel;
	private javax.swing.JScrollPane scrollTable;
	private javax.swing.JLabel labelPuzzlesPlayed;
	private javax.swing.JLabel labelFriendsPuzzles;
	private javax.swing.JLabel labelFriends;
	private javax.swing.JLabel labelFastestName;
	private javax.swing.JLabel labelYourStats;
	private javax.swing.JLabel labelProfilePic;
	private javax.swing.JList listFriendPuzzles;
	private javax.swing.JList listFriends;
	private javax.swing.JList listPuzzles;
	private javax.swing.JLabel labelPlayerScore;
	private javax.swing.JLabel labelPlayerTime;
	private javax.swing.JScrollPane scrollPaneFriendPuzzles;
	private javax.swing.JScrollPane scrollPaneFriends;
	private javax.swing.JScrollPane scrollPanePuzzles;
	private javax.swing.JTable table;

	/**
	 * Constructor to set up the access token (for posting to wall), and the current player
	 * @param codeBase
	 * @param accessToken
	 * @param currentPlayer
	 */
	StatisticsGUI(URL codeBase, String accessToken, Player currentPlayer){
		this.accessToken=accessToken;
		this.currentPlayer=currentPlayer;
		this.codeBase=codeBase;

		initComponents();
		add();
		initiate();
	}

	/**
	 * return this panel
	 * @return panel
	 */
	public JPanel getPanel(){
		return statsPanel;
	}

	/**
	 * Add components to this panel
	 * Sets up the layout/display of this panel
	 */
	public void add(){
		statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.PAGE_AXIS));
		statsPanel.add(Box.createRigidArea(new Dimension(10,10)));

		// Friends part
		TitledBorder friendsBorder = BorderFactory.createTitledBorder("Your Friends' Puzzles");
		JPanel panelHorizontal = new JPanel();
		panelHorizontal.setBorder(friendsBorder);

		panelHorizontal.setLayout(new GridLayout(1,3));
		JPanel panelFriends = new JPanel();
		panelFriends.setLayout(new BorderLayout());
		panelFriends.add(labelFriends, BorderLayout.PAGE_START);
		panelFriends.add(scrollPaneFriends, BorderLayout.LINE_START);

		JPanel panelFriendsPuzzles  = new JPanel();
		panelFriendsPuzzles.setLayout(new BorderLayout());
		panelFriendsPuzzles.add(labelFriendsPuzzles, BorderLayout.PAGE_START);
		panelFriendsPuzzles.add(scrollPaneFriendPuzzles, BorderLayout.LINE_START);

		panelHorizontal.add(panelFriends);
		panelHorizontal.add(panelFriendsPuzzles);
		labelProfilePic.setText(" ");
		panelHorizontal.add(labelProfilePic);

		statsPanel.add(panelHorizontal);

		// Your stats
		JPanel panelVertical = new JPanel();
		panelVertical.setLayout(new BoxLayout(panelVertical, BoxLayout.PAGE_AXIS));
		TitledBorder yourBorder = BorderFactory.createTitledBorder("Puzzles & Statistics");
		panelVertical.setBorder(yourBorder);

		JPanel panelHorizontalYou =  new JPanel();
		panelHorizontalYou.setLayout(new GridLayout(1,3));
		JPanel panelYourPuzzles = new JPanel();
		panelYourPuzzles.setLayout(new BorderLayout());
		panelYourPuzzles.add(labelPuzzlesPlayed, BorderLayout.PAGE_START);
		panelYourPuzzles.add(scrollPanePuzzles, BorderLayout.LINE_START);

		JPanel panelYourStats = new JPanel();
		panelYourStats.setLayout(new BoxLayout(panelYourStats, BoxLayout.PAGE_AXIS));

		panelYourStats.add(labelYourStats);
		panelYourStats.add(labelPlayerScore);
		panelYourStats.add(labelPlayerTime);
		panelYourStats.add(postWallButton);

		JPanel panelFastestPlayer = new JPanel();
		panelFastestPlayer.setLayout(new BoxLayout(panelFastestPlayer, BoxLayout.PAGE_AXIS));
		panelFastestPlayer.add(labelFastestPlayer);
		panelFastestPlayer.add(labelFastestName);

		panelHorizontalYou.add(panelYourPuzzles);
		panelHorizontalYou.add(panelYourStats);
		panelHorizontalYou.add(panelFastestPlayer);

		panelVertical.add(panelHorizontalYou);

		panelVertical.add(scrollTable);
		statsPanel.add(panelVertical);

	}

	/**
	 * Initializes the various components used in this panel
	 */
	private void initComponents() {

		statsPanel = new javax.swing.JPanel();

		listFriends = new javax.swing.JList();

		scrollPaneFriends = new javax.swing.JScrollPane(listFriends);
		scrollPaneFriends.setSize(new Dimension(150,150));
		scrollPaneFriends.setMinimumSize(new Dimension(150,150));
		scrollPaneFriends.setPreferredSize(new Dimension(150,150));
		scrollPaneFriends.setMaximumSize(new Dimension(150,150));

		listPuzzles = new javax.swing.JList();

		scrollPanePuzzles = new javax.swing.JScrollPane(listPuzzles);
		scrollPanePuzzles.setSize(new Dimension(150,150));
		scrollPanePuzzles.setMinimumSize(new Dimension(150,150));
		scrollPanePuzzles.setPreferredSize(new Dimension(150,150));

		labelFriends = new javax.swing.JLabel();
		labelPlayerTime = new javax.swing.JLabel();
		labelYourStats = new javax.swing.JLabel();
		listFriendPuzzles = new javax.swing.JList();

		scrollPaneFriendPuzzles = new javax.swing.JScrollPane(listFriendPuzzles);
		scrollPaneFriendPuzzles.setSize(new Dimension(150,150));
		scrollPaneFriendPuzzles.setMinimumSize(new Dimension(150,150));
		scrollPaneFriendPuzzles.setPreferredSize(new Dimension(150,150));
		scrollPaneFriendPuzzles.setMaximumSize(new Dimension(150,150));

		labelPlayerScore = new javax.swing.JLabel();
		labelPuzzlesPlayed = new javax.swing.JLabel();
		labelFriendsPuzzles = new javax.swing.JLabel();
		labelProfilePic = new javax.swing.JLabel();
		labelProfilePic.setSize(new Dimension(150,150));
		labelProfilePic.setMinimumSize(new Dimension(150,150));
		labelProfilePic.setPreferredSize(new Dimension(150,150));
		labelProfilePic.setMaximumSize(new Dimension(150,150));

		labelFastestPlayer = new javax.swing.JLabel();
		labelFastestName = new javax.swing.JLabel();
		table = new javax.swing.JTable();
		scrollTable = new javax.swing.JScrollPane(table);
		postWallButton = new javax.swing.JButton();

		listFriends.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listFriends.addMouseListener(mouseListenerFriends);

		listFriends.addMouseListener(mouseListenerFriendPuzzles);

		listPuzzles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listPuzzles.addMouseListener(mouseListenerPuzzles);
		labelFriends.setText("Your Friends");

		labelPlayerTime.setText(" ");

		labelYourStats.setText("Your Stats: ");

		listFriendPuzzles.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		labelPlayerScore.setText(" ");
		labelPuzzlesPlayed.setText("Puzzles Played by you");
		labelFriendsPuzzles.setText("Friend's Puzzles");

		labelProfilePic.setText(" ");
		labelFastestPlayer.setText("Fastest player...");

		labelFastestName.setText(" ");

		model = new javax.swing.table.DefaultTableModel(
				new Object [][] {
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null},
						{null, null, null}
				},
				new String [] {
						"Friend's Name", "Score", "Time Taken"
				}
				) {
			boolean[] canEdit = new boolean [] {
					false, false, false
			};

			Class<?>[] types = { String.class, Integer.class, String.class};

			public Class<?> getColumnClass(int columnIndex) {
				return this.types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit [columnIndex];
			}
		};
		model.getDataVector().removeAllElements();
		table.setModel(model);
		table.setRowHeight(18);
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(2).setResizable(false);

		postWallButton.setText("Post To Wall");
		postWallButton.setEnabled(false);
		postWallButton.addActionListener(new postToWallHandler());
	}

	/**
	 * This gets all the current player's all ready played preset puzzles and displays them in a list AND stores them
	 * in the player's gamestats list
	 */
	public void initiate() {

		date = new Date();
		Vector<String> puzzles = new Vector<String>();
		try {
			System.out.println("1");
			URL oracle = new URL(codeBase+"files/players/"+currentPlayer.getfbID()+".txt?"+date.toString());
			System.out.println("2");
			URLConnection connection = oracle.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			System.out.println("3");
			String inputLine;
			String filecontent="";
			ArrayList<GameStatistics> stats = new ArrayList<GameStatistics>();
			String puzzle;
			int score;
			int time;

			StringTokenizer tokens;
			System.out.println("4");
			while ((inputLine = in.readLine())!= null) {
				filecontent += inputLine;
				tokens = new StringTokenizer(inputLine, "||");
				if (tokens.countTokens() == 3) {	// if countTokens < 3 then the line is a \n
					puzzle = tokens.nextToken();
					score = Integer.parseInt(tokens.nextToken());
					time = Integer.parseInt(tokens.nextToken());
					stats.add(new GameStatistics(currentPlayer, puzzle, score, time));
					System.out.println("adding puzzle" + puzzle);
					puzzles.add(puzzle);
				}
			}
			System.out.println("5");
			currentPlayer.setStats(stats);
			listPuzzles.removeAll();
			listPuzzles.setListData(puzzles);
			listPuzzles.revalidate();
			listPuzzles.revalidate();
			in.close();
			System.out.println("6done");
		} catch (Exception e) {
			System.out.println("error");
			puzzles.add("You have not");
			puzzles.add("played any");
			puzzles.add("preset puzzles");
			puzzles.add(":(");
			listPuzzles.setListData(puzzles);
			listPuzzles.setEnabled(false);
		}


		listFriends.setListData(currentPlayer.getFriends().toArray());

	}

	/**
	 * This allows a user to click on a friend to show the puzzles that friend has played
	 */
	MouseListener mouseListenerFriendPuzzles = new MouseAdapter() {
		int lastIndex=999;
		int index;
		Player player;
		/**
		 * When user clicks on a friend, display puzzles they've played
		 */
		public void mousePressed(MouseEvent mouseEvent) {
			index = listFriends.locationToIndex(mouseEvent.getPoint());
			listFriends.setSelectedIndex(index);

			index = listFriends.getSelectedIndex();
			ArrayList<GameStatistics> gameList;
			if (lastIndex != index) {
				player = (Player) listFriends.getSelectedValue();
				// get all puzzles the selected friend has played

				gameList = player.getStats();
				Iterator<GameStatistics> itStats = gameList.iterator();
				GameStatistics gameStats;
				Vector<String> puzzlesPlayed = new Vector<String>();
				while (itStats.hasNext()) {
					gameStats = itStats.next();
					puzzlesPlayed.add(gameStats.getPuzzle());
				}
				lastIndex = index;

				listFriendPuzzles.setListData(puzzlesPlayed);
				listFriendPuzzles.revalidate();
			}
		}
	};

	/**
	 * This allows the user click on their friends to display their profile picture
	 */
	MouseListener mouseListenerFriends = new MouseAdapter() {
		int lastIndex=999;
		int index;
		Image image;
		ImageIcon icon;
		String friendUID, file;
		Dimension boundary = new Dimension(150,150);
		// Modified from: http://stackoverflow.com/questions/10245220/java-image-resize-maintain-aspect-ratio
		public Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

			int original_width = imgSize.width;
			int original_height = imgSize.height;
			int bound_width = boundary.width;
			int bound_height = boundary.height;
			int new_width = original_width;
			int new_height = original_height;

			// first check if we need to scale width
			if (original_width > bound_width) {
				//scale width to fit
				new_width = bound_width;
				//scale height to maintain aspect ratio
				new_height = (new_width*original_height)/original_width;
			}


			// then check if we need to scale even with the new height
			if (new_height > bound_height) {
				//scale height to fit instead
				new_height = bound_height;
				//scale width to maintain aspect ratio
				new_width = (new_height*original_width)/original_height;
			}

			return new Dimension(new_width, new_height);
		}

		/**
		 * When user clicks on a friend, display their picture
		 */
		public void mousePressed(MouseEvent mouseEvent) {

			index = listFriends.locationToIndex(mouseEvent.getPoint());
			listFriends.setSelectedIndex(index);
			index = listFriends.getSelectedIndex();
			if (lastIndex != index) {
				Player pr = (Player) listFriends.getSelectedValue();
				URL pictureURL=null;
				if (!pr.hasPhoto()) {
					friendUID = ((Player) listFriends.getSelectedValue()).getfbID();
					URL getphotoURL;
					URLConnection photoConnection;

					try {
						getphotoURL = new URL(codeBase+ "files/getpictures.php?uid="+friendUID);
						photoConnection = getphotoURL.openConnection();
						photoConnection.setUseCaches(false);
						photoConnection.getInputStream();
						file = friendUID.concat(".jpg");
						pictureURL = new URL(codeBase ,"files/pictureFolder/"+file);
						BufferedImage bufImage = ImageIO.read(pictureURL);
						int imgWidth = bufImage.getWidth();
						int imgHeight= bufImage.getHeight();
						Dimension imgSize = new Dimension(imgWidth, imgHeight);
						Dimension newSize = getScaledDimension(imgSize,boundary);
						image=bufImage.getScaledInstance((int)newSize.getWidth(), (int)newSize.getHeight(), Image.SCALE_DEFAULT);

					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

					icon = new ImageIcon(image);
					pr.setPhoto(icon);
				}
				labelProfilePic.setIcon(pr.getPhoto());
				labelProfilePic.setVerticalAlignment(JLabel.CENTER);
				labelProfilePic.setHorizontalAlignment(JLabel.CENTER);
				int width = pr.getPhoto().getIconWidth()+10;
				int height = pr.getPhoto().getIconHeight()+10;

				// save which friend you are on so their profile picture doesn't constantly refresh
				lastIndex = index;
			}

		}
	};


	/**
	 * This allows a user to click on a puzzle he/she has played and all friends and their stats who have also played the 
	 * 	selected puzzle and display the current player's score 
	 */
	MouseListener mouseListenerPuzzles = new MouseAdapter() {
		int lastIndex=999;
		int index;
		String puzzle;
		/**
		 * When click on a puzzle, display their stats and friend's stats
		 */
		public void mousePressed(MouseEvent mouseEvent) {
			postWallButton.setEnabled(true);
			System.out.println("#rows = " + table.getRowCount());
			index = listPuzzles.locationToIndex(mouseEvent.getPoint());
			listPuzzles.setSelectedIndex(index);
			index = listPuzzles.getSelectedIndex();
			ArrayList<GameStatistics> gameList;
			if (lastIndex != index) {
				table.setRowSorter(null);
				puzzle = (String) listPuzzles.getSelectedValue();

				int leastTime=-1;
				String fastestName = currentPlayer.toString();
				System.out.println("before loading personal stats "+ fastestName);

				// get the current player's stats for the selected puzzle
				gameList = currentPlayer.getStats();
				Iterator<GameStatistics> itStats = gameList.iterator();
				GameStatistics gameStats;
				boolean cont = true;
				while (itStats.hasNext() && cont) {
					gameStats = itStats.next();
					if (gameStats.getPuzzle().equalsIgnoreCase(puzzle)) {
						int score = gameStats.getScore();
						String sc = Integer.toString(score);
						labelPlayerScore.setText("Score: "+sc);
						//int time = gameStats.getTimeFound();
						leastTime = gameStats.getTimeFound();
						System.out.println("personal stats "+ leastTime);
						String ti="";
						if (leastTime==1000){
							ti="---";
						}else{
							ti = Integer.toString(leastTime);
						}
						labelPlayerTime.setText("Time: "+ti+" seconds");
						cont = false;
					}
				}
				lastIndex = index;

				System.out.println("after loading personal stats "+ fastestName);


				// get all friends that have played the selected puzzle and populate the table with their stats
				ArrayList<Player> friends = currentPlayer.getFriends();
				Iterator<Player> itFriends = friends.iterator();
				Player friend;
				ArrayList<GameStatistics> statList;
				GameStatistics stats;
				boolean foundPuzzle = false;
				int time;
				// clear the current table
				model=(DefaultTableModel) table.getModel();
				model.getDataVector().removeAllElements();
				table.repaint();
				table.revalidate();
				while (itFriends.hasNext()) {
					foundPuzzle = false;
					friend = itFriends.next();
					statList = friend.getStats();
					itStats = statList.iterator();
					while (itStats.hasNext() && !foundPuzzle) {
						stats = itStats.next();
						if (stats.getPuzzle().equalsIgnoreCase(puzzle)) {
							foundPuzzle = true;
							// add this friends and their stats to the table
							Object[] row = new Object[3];
							System.out.println(friend.toString());
							row[0] = friend.toString();
							row[1] = stats.getScore();
							time = stats.getTimeFound();
							if (time < leastTime) {
								leastTime = time;
								fastestName = friend.toString();
								System.out.println("in leastTime: "+ fastestName);
							}
							if (time==1000){
								row[2] = "---";
							}else{
								row[2] = time;
							}
							//System.out.println(row[0] + row[1] + row[2]);
							System.out.println(model.getRowCount());
							model.addRow(row);
							System.out.println("adding row at row ");
						}
					}
				}
				table.setModel(model);
				table.setAutoCreateRowSorter(true);


				System.out.println("before setting label "+ fastestName);
				labelFastestName.setText(fastestName);
			}
		}
	};


	/**
	 * Class: postToWallHandler
	 * Purpose: Post the user's stats of the currently selected puzzle to their wall
	 * @author Mark
	 *
	 */
	private class postToWallHandler implements ActionListener
	{
		/**
		 * When user clicks post to wall, post their score to wall
		 */
		public void actionPerformed(ActionEvent arg0){
			Date date = new Date(); // to get around caching issues
			ArrayList<GameStatistics> gameList = currentPlayer.getStats();;
			String puzzle = (String) listPuzzles.getSelectedValue();
			int score=0;
			String time="";
			Iterator<GameStatistics> itStats = gameList.iterator();
			GameStatistics gameStats;
			boolean cont = true;
			while (itStats.hasNext() && cont) {
				gameStats = itStats.next();
				if (gameStats.getPuzzle().equalsIgnoreCase(puzzle)) {
					score = gameStats.getScore();
					time = String.valueOf(gameStats.getTimeFound());
					if (time.equals("1000")){
						time="---";
					}
					cont = false;
				}
			}
			try {
				JOptionPane
				.showMessageDialog(
						new JFrame(),
						"Sucessfully posted score to wall",
						"Success", JOptionPane.DEFAULT_OPTION);
				URL postWall = new URL(codeBase + "files/postToWall.php");
				URLConnection connection = postWall.openConnection();
				connection.setDoOutput(true); // Open output connection
				connection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				// Get the output stream writer, so information can be written to
				// the URL

				OutputStreamWriter out = new OutputStreamWriter(
						connection.getOutputStream());
				out.write("accessToken=" + accessToken);
				out.write("&puzzle=" + puzzle);
				out.write("&score=" + score);
				out.write("&time=" + time);

				out.flush();
				out.close();
				connection.getInputStream();

			} catch (Exception e) {
				JOptionPane
				.showMessageDialog(
						new JFrame(),
						"Failed to post score to wall",
						"Error", JOptionPane.DEFAULT_OPTION);
				e.getMessage();
			}		
		}

	}




}
