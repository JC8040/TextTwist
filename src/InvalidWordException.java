/**
 * Class: InvalidWordException
 * Purpose: Custom exception, for invalid words in dictionary
 * @author Mark
 *
 */
public class InvalidWordException extends RuntimeException {

	/**
	 * Sets up this exception with an appropriate message.
	 * @param message Word that can't be inserted
	 */
	public InvalidWordException (String message)
	{
		super ("Can't insert word: " + message);
	}
}
