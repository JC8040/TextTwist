import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Class: Player
 * Purpose: Sets up a player of the game
 * @author Christina, Mark
 *
 */
public class Player {
	private String name;
	private String fbID;
	private ArrayList<GameStatistics> statistics; 
	private ArrayList<Player> friends;

	//Photo of the user
	private ImageIcon photo;

	/**
	 * Constructor to set up the player with their facebook id, name, their stats, and their friends
	 * @param fbID
	 * @param name
	 * @param stats
	 */
	public Player(String fbID, String name, ArrayList<GameStatistics> stats){
		this.fbID=fbID;
		this.name = name;
		statistics = stats;
		friends = new ArrayList<Player>();
		photo=null;
	}

	/**
	 * return player name
	 * @return player name
	 */
	public String getName(){
		return name;
	}
	/**
	 * return player fb id
	 * @return facebook id
	 */
	public String getfbID() {
		return fbID;
	}

	/**
	 * Add a friend to the player
	 * @param friend
	 */
	public void addFriend(Player friend) {
		friends.add(friend);
	}

	/**
	 * get the player's friends
	 * @return friends in array list
	 */
	public ArrayList<Player> getFriends() {
		return friends;
	}

	/**
	 * set the player's stats
	 * @param stats
	 */
	public void setStats(ArrayList<GameStatistics> stats) {
		statistics = stats;
	}
	/**
	 * get the player's stats
	 * @return stats in array list
	 */
	public ArrayList<GameStatistics> getStats() {
		return statistics;
	}

	/**
	 * set the player's photo
	 * @param photo
	 */
	public void setPhoto(ImageIcon photo)
	{
		this.photo = photo;
	}

	/**
	 * get the player's photo
	 * @return image
	 */
	public ImageIcon getPhoto()
	{
		return this.photo;
	}
	/**
	 * check if user has photo
	 * @return true if has photo
	 */
	public boolean hasPhoto()
	{
		if (photo == null)
			return false;
		return true;
	}
	/**
	 * toString method that returns the user's name
	 */
	public String toString() {
		return name;
	}

}
