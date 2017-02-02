import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

/**
 * Class: Trie
 * Purpose: Trie class represents a reTRIEval structure used to determine if a given word 
 * is in the dictionary at optimal speed  
 * 
 * @author Mark
 *
 */
public class Trie {

	protected TrieNode _root;					// root of the tree
	private final int ASCII_CONVERSION = 97;	// ASCII conversion of 'a'
	private final int MAX_WORD_LENGTH = 99;		// maximum number of characters a word can have	
	private int[] lengthArray;					// store what length of valid words are present in the Trie
	private int[] specialChars;					// extra characters that may be part of a word
	private final int ALPHABET_SIZE = 26;		// the size of the alphabet
	private ArrayList<String> invalidKidWords;	// stores inserted 4letter words with NO 3 letter anagrams/permutable subword

	/* CONSTRUCTORS */
	/**
	 * constructor that initializes the root node
	 */
	public Trie() {
		_root = new TrieNode();
		lengthArray = new int[MAX_WORD_LENGTH+1];
		for (int i=0; i < lengthArray.length; ++i)		// initialize array
			lengthArray[i] = 0;
		specialChars = new int[2];
		specialChars[0] = ((int) "'".charAt(0)) - ASCII_CONVERSION;
		specialChars[1] = ((int) "-".charAt(0)) - ASCII_CONVERSION;

		invalidKidWords = new ArrayList<String>();
	}

	/* MUTATOR METHODS */

	/**
	 * This method inserts a specified word in the dictionary into the TRIE structure
	 * 
	 * @throws InvalidWordException if the word is null or contains invalid characters
	 * 
	 * @param word the word in the dictionary to insert
	 */
	public void insert(String word) {
		if (word == null) {
			throw new InvalidWordException("null");
		} else if (!word.matches("^[a-zA-Z'-]+$")) {			// word must consist of only alphanumeric characters	
			throw new InvalidWordException("invalid characters");
		}
		else {
			word = word.toLowerCase();
			TrieNode previous = _root;
			TrieNode current = _root;
			char ch;
			int index=-1;
			int ascii;
			while (index < word.length()-1) {
				++index;
				ch=word.charAt(index);
				ascii = ((int) ch) - ASCII_CONVERSION;
				ascii = checkIfSpecialChar(ascii);
				if (ascii < 0 || ascii > ALPHABET_SIZE + specialChars.length)
					throw new InvalidWordException("invalid character " + Character.toString(ch));
				previous = current;
				current = current.DescendentAt(ascii);				
				if (current == null) {
					TrieNode node = new TrieNode(Character.toString(ch), previous);
					previous.setDescendent(ascii, node);
					current = previous.DescendentAt(ascii);
				}
			}
			current.setMarker(true);

			if (word.length() == 3)
				updateInvalidKidWords(word);			// need to update current invalid 4letter words
			if (word.length() == 4) {
				ArrayList<String> temp = getSubwords(word, 3);
				if (temp.size() == 0) {
					invalidKidWords.add(word);
				} else lengthArray[word.length()] += 1;
			} else lengthArray[word.length()] += 1;
		}
	}

	/* ACCESSOR METHODS */

	/**
	 * determines if the specified word is in the dictionary
	 * 
	 * takes O(dm) where d is the size of the alphabet(26) & m is the size of the word
	 * but since ascii conversions are used, the order of complexity is just O(m)
	 * 
	 * THIS METHOD IS USED TO CHECK IF A SUBMITTED WORD IS IN THE CURRENT DICTIONARY
	 * 
	 * @param word the word to search the dictionary for
	 * @return true if the word is in the dictionary
	 * 			false, otherwise
	 */
	public boolean searchDictionary(String word) {
		if (word.length() < 1)
			return false;

		TrieNode node = search(word);
		if (node != null) {
			// determines if the actual word has been stored in the trie (multiple words could have the same lexicographic sort)
			if (node.getMarker())
				return true;
		}		
		return false;
	}

	/**
	 * Gets a random word of specified length from the dictionary
	 * 
	 * THIS METHOD IS USED TO RANDOMLY GENERATE A WORD IN THE DICTIONARY
	 * 
	 * @param length the length of a random word to get from the dictionary
	 * @return a random word in the dictionary of length 'length'
	 */
	public String getRandomWord(int length) {
		if (lengthArray[length] == 0)
			throw new InvalidWordException("There are no valid words of length " +length+" in the Trie");
		int storedLength = length;
		int tries = 0;		// counter so that it can take multiple paths instead of possibly finding an infinit loop in a path with no valid words
		int rand;
		TrieNode node =_root;
		String randword = "";
		while (length > 0) {
			if (tries > 20) {
				tries = 0;
				length = storedLength;
				node = _root;
				randword="";
			}
			rand = 0 + (int)(Math.random() * (((ALPHABET_SIZE + specialChars.length-1) - 0) + 1));				// generate random number between 0 and 27 (valid array index)
			if (node.DescendentAt(rand) != null) {
				node = node.DescendentAt(rand);
				randword += node.getElement();
				--length;
				tries = 0;
			} else ++tries;
			if (length == 0) {
				if (!node.getMarker() || !isValidRandomWord(randword)) {	// makes sure the random node reached actually stored a valid word in the dictionary
					length = storedLength;
					node = _root;
					randword="";
				}
			}
		}
		return randword;
	}

	/**
	 * returns ALL subwords of the specified length of the given word
	 * 
	 * @param word the word to get all subwords of
	 * @param length the length of each subword
	 * @return	ALL subwords of specified length in 'word'
	 */
	public ArrayList<String> getSubwords(String word, int length) {
		ArrayList<String> list = new ArrayList<String>();
		if (length > word.length() || word.equals(""))
			return list;
		word = word.toLowerCase();
		return subwords(_root, word, "", length, 0, 0, 0, list, new int[word.length()], new int[word.length()], new Stack<Integer>());
	}

	public boolean hasValidWords() {
		if (lengthArray[4] > 0 && lengthArray[6] > 0)
			return true;
		
		return false;
	}

	/* HELPER METHODS */
	/**
	 * Determines the node containing the position of the specified word
	 * 
	 * THIS FUNCTION SHOULD BE PRIVATE, ONLY DECLARE IT AS PUBLIC WHEN USING IT FOR TESTING
	 * 
	 * @param word the word to search the dictionary for
	 * 
	 * @return the node from when travelling from the root to this node gives the path that represents the specified word
	 * 			or null if the word is not in the dictionary
	 */
	public TrieNode search(String word) {
		word = word.toLowerCase();
		TrieNode current = _root;
		char ch;
		int index=-1;
		int ascii;
		//boolean found = false;
		while (true) {
			++index;
			ch=word.charAt(index);
			ascii = ((int) ch) - ASCII_CONVERSION;
			ascii = checkIfSpecialChar(ascii);
			current = current.DescendentAt(ascii);
			if (current == null) {
				return null;				// if any prefix is not in the trie, then the word is not in the trie
			}
			else {
				if (index == word.length()-1) {
					return current;			// if whole word is in the trie
				}
			}
		}
		//return current;
	}
	
	/**
	 * Updates all the invalid 4letter words(have no 3letter anagrams) in the Trie. This method is called whenever a 
	 * 	3letter word is inserted into the trie
	 *  
	 * @param word	the newly inserted 3letter word
	 */
	private void updateInvalidKidWords(String word) {
		Iterator<String> it = invalidKidWords.iterator();
		ArrayList<String> tempList;
		String string="";
		int count=0;
		while (it.hasNext()) {
			string = it.next();
			tempList = getSubwords(string, 3);
			if (tempList.contains(word)) {
				it.remove();			// remove the now valid 4 letter word
				lengthArray[4] += 1;	// increase the number of valid 4 letter words
			}
			++count;
		}
	}

	/**
	 * This method determines if the current special (non-alphabet) character (with given ascii) 
	 *  is allowed to be part of the word. If it is, the appropriate index will be returned, otherwise its original 
	 *  ascii value will return and a calling method will throw an exception
	 *  
	 * @param ascii the ascii value of the special character
	 * @return the integer mapping if the special character is allowed to be apart of the word
	 * 			otherwise, return it's original ascii value
	 */
	private int checkIfSpecialChar(int ascii) {
		for (int i=0; i<specialChars.length; ++i) {
			if (ascii == specialChars[i])
				ascii = ALPHABET_SIZE + i;
		}
		return ascii;
	}
	

	/**
	 * determines if the given word can construct at least one x-letter word 
	 * 		where x is 6 in the regular game mode
	 * 		and   x is 3 in the kid's game mode
	 * 
	 * THIS METHOD IS USED FOR CONSTRUCTING A RANDOM PUZZLE WORD
	 * 
	 * @param word the word to check if it is a valid letter sequence for a puzzle
	 * @return true if the word can construct at least one x-letter word
	 * 			false, otherwise
	 */
	private boolean isValidRandomWord(String word) {
		ArrayList<String> list;

		if (word.length() == 4) {		
			// only word lengths of size 4 have the criteria that they must have at least one subword of length 3
			// determines if there is at least one 4 letter word, and that the given 4 letter word is not in proper sequence all ready
			// don't think i need search AND searchDict, searchDict calls search...
			if (searchDictionary(word)) {
				// determines if there is at least one 3 letter word
				list = subwords(_root, word.toLowerCase(), "", 3, 0, 0, 0, new ArrayList<String>(), new int[word.length()], new int[word.length()], new Stack<Integer>());
				if (list.size() > 0)
					return true;
				else return false;
			} else return false;
		} else {
			list = subwords(_root, word.toLowerCase(), "", word.length(), 0, 0, 0, new ArrayList<String>(), new int[word.length()], new int[word.length()], new Stack<Integer>());
			if (list.size() > 0)
				return true; 			// will return true as long as the word (of length 1,2,3,5,6) is in the trie
		}
		return false;	
	}
	
	/**
	 * This function generates all words of length 'length' that are constructible from any sequence of letters of 'word'
	 * 	The algorithm for this method uses:
	 * 		since all words are sorted in lexicographical order, all valid words will be permutations of 'word' by taking
	 * 			a specific character and ONLY grabbing characters that are at later indices (lexicographically higher)
	 * 		 
	 * 
	 * @param node	the node currently at in the Trie
	 * @param word	the word to find all possible contructible words from
	 * @param subword	the current permutation of 'word'
	 * @param length	the desired length of words constructible from 'word'
	 * @param indexCount	the index counter into the String 'word'
	 * @param pivotIndex	the letter's index (into the String 'word') that is currently being used as the pivot
	 * @param startingIndex	the index to start from
	 * @param list	all words of length 'length' that are constructible using the letters of 'word' that are in the Trie
	 * @param usedIndices	all indices according to 'word' that are currently in the subword
	 * @param triedIndices	all indices according to 'word' that have been tried to added to subword as a permutation
	 * @param subwordIndex	stack containing the sequence of indices of the current subword. used to get back the correct index when
	 * 							popping characters off the subword
	 * @return list of all words constructible from 'word' of length 'length'
	 */
	private ArrayList<String> subwords(TrieNode node, String word, String subword, int length, int indexCount, int pivotIndex, int startingIndex, ArrayList<String> list, int[] usedIndices, int[] triedIndices, Stack<Integer> subwordIndex) {
		if (indexCount == startingIndex && startingIndex != 0)
			++indexCount;

		if (subword.length() == length) {
			TrieNode foundNode = search(subword);
			if (foundNode != null) {
				if (foundNode.getMarker()) {
					boolean inserted = false;
					if (list.contains(subword))						// only add distinct subwords
						inserted=true;
					if (!inserted)
						list.add(subword);
				}
			}
			int removeIndex = subwordIndex.pop().intValue();
			usedIndices[removeIndex]=0;		// reset the last index used
			triedIndices[removeIndex]+=1;		// put it as tried
			subword = subword.substring(0, subword.length()-1);
			// indexCount all ready incremented from last return
			return subwords(node.getAncestor(), word, subword, length, indexCount, pivotIndex, startingIndex, list, usedIndices, triedIndices, subwordIndex);
		} else if (indexCount >= word.length()) {
			subword = subword.substring(0, subword.length()-1);
			if (subword.isEmpty()) {		// need to increase the starting point
				int index;
				do {		// find next starting index which have not all ready tried starting with
					++startingIndex;
					// base return, occurs when attempted to start at all positions and the current subword has reached null again
					if (startingIndex >= word.length()) {
						return list;
					}
					index = (int) word.charAt(startingIndex) - ASCII_CONVERSION;
					index = checkIfSpecialChar(index);
				} while (_root.DescendentAt(index) == null);
				for (int i=0; i<word.length(); ++i) {			// re-start process with new starting point
					usedIndices[i]=0;
					triedIndices[i]=0;
				}
				usedIndices[startingIndex] = 1;
				triedIndices[startingIndex] = 1;
				node = _root.DescendentAt(index);
				subword = node.getElement();
				subwordIndex.pop();
				subwordIndex.push(startingIndex);
				return subwords(node, word, subword, length, 0, startingIndex, startingIndex, list, usedIndices, triedIndices, subwordIndex);
			}
			// get last index which we still need to try permutations for before
			int popped = subwordIndex.pop().intValue();

			// reset the tried indices according to the new subword
			for (int i=popped+1; i<word.length(); ++i)		// haven't tried anything above it's index
				triedIndices[i] = 0;
			for (int i=0; i<=popped; ++i)					// have tried everything below and equal to it's index
				triedIndices[i]=1;					

			usedIndices[pivotIndex] = 0;
			triedIndices[pivotIndex] = 1;

			return subwords(node.getAncestor(), word, subword, length, pivotIndex+1, subwordIndex.peek().intValue(), startingIndex, list, usedIndices, triedIndices, subwordIndex);
		} else {				// when index is in range of word
			if (usedIndices[indexCount] == 0 && triedIndices[indexCount] == 0) {
				// get the next character
				int index = (int) word.charAt(indexCount) - ASCII_CONVERSION;
				index = checkIfSpecialChar(index);
				if (node.DescendentAt(index) != null) {
					node = node.DescendentAt(index);
					subword += node.getElement();
					subwordIndex.push(indexCount);
					if (subword.length() < length) {
						//store the pivot and save the indexCount
						pivotIndex = indexCount;
					}
					usedIndices[indexCount] += 1;

					if (subword.length() < length) {					// added new character to the subword
						for (int i=0; i< word.length(); ++i) {			// new subword formed, need to permute all unused characters
							triedIndices[i]=0;
						}
					}
				} else {
					triedIndices[indexCount]+=1;
				}
				if (subword.length() < length) {
					// need to determine the first unused index in word
					for (int i=0; i<word.length(); ++i) {
						if (usedIndices[i] == 0 && triedIndices[i] == 0)
							return subwords(node, word, subword, length, i, pivotIndex, startingIndex, list, usedIndices, triedIndices, subwordIndex);
					}
				}
			}
			return subwords(node, word, subword, length, ++indexCount, pivotIndex, startingIndex, list, usedIndices, triedIndices, subwordIndex);
		}
	}
}