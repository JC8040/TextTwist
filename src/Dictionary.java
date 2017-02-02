import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class: Dictionary
 * Purpose: The dictionary that is used for puzzle solutions
 * It extends Trie structure
 * @author Mark
 *
 */
public class Dictionary extends Trie {
	
	// class that references itself, represents the singleton Dictionary
	private static Dictionary dictionary; 

	/**
	 * Promotes the Singleton design pattern in order to only be able to use one
	 * dictionary at a time
	 * Other classes can call this method to get the instance of dictionary
	 * 
	 * @param dictFile
	 *            the location of the dictionary file to use for puzzles
	 * @return the Dictionary corresponding to the words in the file at dictFile
	 * @throws Exception
	 *             if there is a problem reading from the specified url dictFile
	 */
	public static Dictionary instance(URL dictFile) throws Exception {
		if (dictionary == null) {
			dictionary = new Dictionary(dictFile);
		}
		return dictionary;
	}

	/**
	 * protected to make this class a singleton class
	 * Gets the dictionary from url and sets it as the dictionary
	 * 
	 * @throws MalformedURLException
	 */
	private Dictionary(URL dictFile) {

		try {
			Date date = new Date(); // to get around caching issues
			URL dictionary = new URL(dictFile + "?" + date.toString());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					dictionary.openStream()));

			String line = "";

			while ((line = in.readLine()) != null) {
				// Loop through every line and add it to this Dictionary.
				this.insert(line);
				
				System.out.println("INSERTING " + line);
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// bw.close();
	}

	/**
	 * Allows an admin to change the current dictionary
	 * 
	 * @param dictFile
	 *            the location of the new dictionary file to use
	 * @throws Exception
	 *             if there is a problem reading from the file
	 */
	
	public boolean modifyDictionary(URL dictFile) {
		Dictionary dict = new Dictionary(dictFile);
		if (dict.hasValidWords()) {
			dictionary = dict;
			return true; // dictionary been modified
		}
		else
			return false; // dictionary not modified
		//return dictionary;
	}

	/**
	 * Check if the dictionary already contains a word
	 * @param word to check
	 * @return true if contains it; false otherwise
	 */
	boolean contains(String word) {
		if (this.contains(word))
			return true;
		else
			return false;
	}

}
