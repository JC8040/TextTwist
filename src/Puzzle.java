import java.util.*;

/**
 * Class: Puzzle
 * Purpose: Creates a puzzle; can get solutions of the puzzle.
 * Overloaded constructor allows creating puzzle for kids, random, and preset games
 * @author Christina, Mark
 *
 */
public class Puzzle {
	private String letters = "";
	private boolean kidsMode = false;
	private Dictionary dictionary;
	private HashMap<String, Integer[]> solutions;
	private int countSolutions;

	/* CONSTRUCTORS */
	/**
	 * Create a random puzzle from the dictionary and generate solutions for it
	 * @param dictionary used to create puzzle
	 */
	Puzzle(Dictionary dictionary) {
		this.dictionary = dictionary;
		// Get a random word from the dictionary
		letters = this.getRandomWord(6);
		// Call reorderLetters to randomize its letters
		letters = this.reorderLetters();
		// Set that as letters var
		countSolutions=0;
		generateSolutions();
	}

	/**
	 * Create a preset puzzle and and generate solutions for it
	 * @param letters preset puzzle word
	 * @param dictionary used to create puzzle
	 */
	Puzzle(String letters, Dictionary dictionary) {
		this.dictionary = dictionary;
		this.letters = letters;
		generateSolutions();
	}

	/**
	 * Create a random kids puzzle based on the given dictionary, and generate its solutions
	 * @param kidsMode True if it's kids mode (this case yes)
	 * @param dictionary used to create puzzle
	 */
	Puzzle(boolean kidsMode, Dictionary dictionary) {
		this.dictionary = dictionary;
		kidsMode = true;
		// Get a random (4 letter) word from the dictionary
		letters = this.getRandomWord(4);
		// Call reorderLetters to randomize its letters
		letters = this.reorderLetters();
		// Set that as letters var
		generateSolutions();
	}

	/**
	 * Get the puzzle created
	 * @return puzzle letters
	 */
	public String getPuzzle(){
		return letters;
	}

	/* MUTATOR METHODS */
	/**
	 * Generates all the solutions for this puzzle
	 * Puzzle solutions range from words of length 3 to the puzzle's word's length
	 */
	private void generateSolutions() {
		solutions = new HashMap<String, Integer[]>();

		ArrayList<String> temp;
		for (int i=3; i<=letters.length(); ++i) {
			// Get the solutions of the puzzle from the dictionary
			temp = dictionary.getSubwords(letters, i);
			Iterator<String> it = temp.iterator();
			while (it.hasNext()){
				solutions.put(it.next(), new Integer[]{-1,-1});
				countSolutions++;
			}
		}
	}

	/**
	 * Set dictionary used
	 * @param dictionary
	 */
	public void setDict(Dictionary dictionary){
		this.dictionary=dictionary;
	}

	/* ACCESSOR METHODS */
	/**
	 * Method that randomizes/reorders the letters
	 * 
	 * @return a re-ordered version of the letters such that it's sequence does not produce a valid word
	 */
	public String reorderLetters() {

		List<Character> characters = new ArrayList<Character>();
		for (char c : letters.toCharArray()) {
			characters.add(c);
		}

		boolean isAWord = true;
		StringBuilder output = new StringBuilder(letters.length());
		// Reorder letter until it's not a real word
		while (isAWord) {
			// Reorder the letters
			// Randomly pick a letter from the word, and add it to the end of the word
			while (characters.size() != 0) {
				int randPicker = (int) (Math.random() * characters.size());
				output.append(characters.remove(randPicker));
			}
			// If the dictionary contains the word
			// Sets up the variables to go through this loop again
			if (dictionary.searchDictionary(output.toString())) {
				output = new StringBuilder(letters.length());
				characters = new ArrayList<Character>();
				for (char c : letters.toCharArray()) {
					characters.add(c);
				}
			} else // not a valid word, can quit this loop
				isAWord = false;
		}
		return output.toString();
	}
	
	/**
	 * Helper method that gets a random word from the dictionary.
	 * 
	 * @param wordSize
	 *            size of the word requested
	 * @return String of the word from Dictionary
	 */
	private String getRandomWord(int wordSize) {
		return dictionary.getRandomWord(wordSize);
	}
	
	/**
	 * toString method returns the puzzle's letters
	 */
	public String toString() {
		return letters;
	}

	/**
	 * Whether the puzzle is for kids mode
	 * 
	 * @return true if the puzzle represents a Kid's Mode puzzle
	 * 			false, otherwise
	 */
	public boolean isKidsMode() {
		return kidsMode;
	}
	/**
	 * returns the list of solutions for this puzzle
	 * 
	 * @return list of solutions for this puzzle
	 */
	public HashMap<String, Integer[]> getSolutions() {
		return solutions;
	}

	/**
	 * Return number of solutions generated 
	 * @return number of solutions
	 */
	public int getCountSolutions(){
		return countSolutions;
	}
}
