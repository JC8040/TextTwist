/**
 * Class: NullElementException
 * Purpose: Custom exception, for inserting null element
 * @author Mark
 *
 */
public class NullElementException extends RuntimeException {
	 /**
	  * Sets up this exception with an appropriate message.
	  */
	 public NullElementException ()
	 {
		 super ("Can't insert a null element into the trie");
	 }
 }
