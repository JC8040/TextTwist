import java.util.ArrayList;

/**
 * Class: TrieNode
 * Purpose: TrieNode represents a node in a Trie with English alphabet elements (also apostrophe "'" and dash "-")
 * 
 * @author Mark
 */
public class TrieNode {
	
	private String _element;				// element (single character) stored at the node
	private boolean _marker;				// special marker used to specify which nodes contain a word in the dictionary
	private TrieNode[] _descendents;		// descendents of this TrieNode
	private TrieNode _ancestor;				// ancestor of this TrieNode
	private final int ALPHABET_SIZE = 26;	// the size of the alphabet
	private final int SPECIALCHAR_SIZE = 2;	// the size of other characters allowed
	private int _level;						// the height of the this node in the trie
	/* CONSTRUCTORS */ 
	/**
	 * the base constructor that initializes all instance variables and will the root of the Trie
	 */
	public TrieNode() {
		_element = "";
		_ancestor = null;
		_marker = false;
		_descendents = new TrieNode[ALPHABET_SIZE + SPECIALCHAR_SIZE];
		//_wordList = null;
		_level=-1;
	}
	/**
	 * the constructor that initializes the all instance variables. This is used for internal and leaf nodes
	 * 
	 * @param element the element stored at this TrieNode
	 * @param ancestor the ancestor/parent of the this TrieNode
	 */
	public TrieNode(String element, TrieNode ancestor) {
		_element = element;
		_ancestor = ancestor;
		_marker = false;
		_descendents = new TrieNode[ALPHABET_SIZE + SPECIALCHAR_SIZE];
		//_wordList = new ArrayList<String>();
		_level = 1+ancestor.getLevel();
	}
	
	/* MUTATOR METHODS */
	/**
	 * sets the descendent at the index'th position of this TrieNode
	 * 
	 * @param index the descendent to alter
	 * @param node the TrieNode that the index'th descendent will point to
	 * @throws IndexOutOfBoundsException if the index is larger than the number of descendents
	 */
	public void setDescendent(int index, TrieNode node) {
		if (index >= ALPHABET_SIZE + SPECIALCHAR_SIZE || index < 0)
			throw new ArrayIndexOutOfBoundsException();
		_descendents[index] = node;
	}
	/**
	 * sets the element stored at this TrieNode
	 * 
	 * @param ch the string to store at this TrieNode
	 */
	public void setElement(String ch) {
		this._element = ch;
	}
	/**
	 * sets the marker at this TrieNode
	 * 
	 * @param bool the logical value to set the marker at this TrieNode to
	 */
	public void setMarker(boolean bool) {
		_marker = bool;
	}
	/**
	 * sets the ancestor at this TrieNode
	 * 
	 * @param node the node to set as this TrieNode's ancestor
	 */
	public void setAncestor(TrieNode node) {
		this._ancestor = node;
	}
	
	/**
	 * this method adds the word 'word' into the list of words that the path from the root 
	 * 	to this node can construct
	 *  
	 * @param word the valid word in the dictionary to store at thid node
	 */
	public void addWord(String word) {
		//_wordList.add(word);
	}
	
	/* ACCESSOR METHODS */
	/**
	 * returns the descendent at the specified index of this TrieNode
	 * 
	 * @param index the index of the descendent TrieNode
	 * @throws IndexOutOfBoundsException if the index is larger than the number of descendents
	 * @return the TrieNode that is the index'th descendent of this TrieNode
	 */
	public TrieNode DescendentAt(int index) {
		//System.out.println("decendent at: " +index+ " is " + _descendents[index]);
		if (index >= ALPHABET_SIZE + SPECIALCHAR_SIZE || index < 0)
			throw new ArrayIndexOutOfBoundsException();
		return this._descendents[index];
	}
	/**
	 * returns the element stored at this TrieNode
	 * 
	 * @return the element stored at thie TrieNode
	 */
	public String getElement() {
		return this._element;
	}
	/**
	 * gets the marker at this TrieNode
	 * 
	 * @return the marker at this TrieNode
	 */
	public boolean getMarker() {
		return _marker;
	}
	/**
	 * gets the ancestor at this TrieNode
	 * 
	 * @return this TrieNode's ancestor
	 */
	public TrieNode getAncestor() {
		return _ancestor;
	}
	
	
	/**
	 * This function returns this node's level/height in the Trie
	 * 
	 * @return the height/level of this node
	 */
	public int getLevel() {
		return _level;
	}
}
