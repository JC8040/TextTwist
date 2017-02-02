/**
 * Class: GameStatistics
 * Purpose:  This class represents the statistics for a played preset puzzle.
 * 
 * @author Mark
 *
 */
public class GameStatistics {


	private Player _player;
	private String _puzzle;
	private int _score;			// total score
	private int _timeFound;		// time found 6 letter word
	/**
	 * Constructor to set the player, puzzle, score, and time
	 * @param player
	 * @param puzzle
	 * @param score
	 * @param timeFound
	 */
	public GameStatistics(Player player, String puzzle, int score, int timeFound) {
		_player=player;
		_puzzle=puzzle;
		_score=score;
		_timeFound=timeFound;
	}

	/**
	 * Get the puzzle
	 * @return puzzle
	 */
	public String getPuzzle() {
		return _puzzle;
	}

	/**
	 * Get the score
	 * @return score
	 */
	public int getScore() {
		return _score;
	}

	/**
	 * Get the time found 
	 * @return time
	 */
	public int getTimeFound() {
		return _timeFound;
	}
}
