import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/*
 * TCSS 342 - Data Structures
 * Assignment 3 - Compressed Literature
 */

/**
 * Implement Huffman’s coding algorithm in a CodingTree class.  
 * This  class will carry out all stages of Huffman’s encoding algorithm:  
 * 
 * ● counting the frequency of characters in a text file.  
 * 
 * ● creating one tree for each character with a non­zero count.  
 * 		○ the tree has one node in it and a weight equal to the character’s count.  
 * 
 * ● repeating the following step until there is only a single tree:  
 * 		○ merge the two trees with minimum weight into a single tree with weight 
 * 		  equal to  the sum of the two tree weights by creating a new root and 
 *        adding the two trees  as left and right subtrees.  
 * 
 * ● labelling the single tree’s left branches with a 0 and 
 *   right branches with a 1 and reading  the code for the 
 *   characters stored in leaf nodes from the path from root to leaf.  
 * 
 * ● using the code for each character to create a compressed encoding
 *   of the message.  
 * 
 * ● (Optional)  provide a method to decode the compressed message.  
 * 
 * 
 * Sources: www.techiedelight.com/huffman-coding/
 * @author Steve Mwangi
 * @version Spring 2019
 *
 */
public class CodingTree {
	
	/** Messages. */
	public StringBuilder huffmanCodeDirectory;
	
	// Byte(value) representation as string of each character(key).
	public final Map<Character, String> huffmanCodes;
	// Count frequency of appearance of each character and store in map..
	public final Map<Character, Integer> frequencyMap;
	// Priority Queue for nodes with Min Heap invariant. Helps create Huffman Tree.
	public final PriorityQueue<HuffmanNode> huffmanTree;
	// Array of Bytes for compressed message.
	public byte[] bytes;
	/** List data member for encoded message. */
	public final List<String> bits;
	/** String data member for decoded message. */
	public String decoded, encoded = "";
	
	
	
	/**
	 *  void CodingTree(String message) ­ a constructor that takes the
	 *  text of a message to be  compressed. 
	 *  
	 *  The constructor is responsible for calling all private methods 
	 *  that carry out  the Huffman coding algorithm.  
	 */
	public CodingTree(String message) {
		frequencyMap = getMap(message);
		huffmanTree = getHuffmanTree(frequencyMap);
		huffmanCodes = getHuffmanCodes(huffmanTree);
		huffmanCodeDirectory = getCodeDirectory(huffmanCodes, new StringBuilder("Huffman Codes are : "));
		bits = getBits(huffmanCodes, message);
		bytes = getCompressed(huffmanCodes, message, bytes);
		decoded = decode(bits, huffmanCodes);
	}
	
	/**
	 * This method is for traversing the huffman tree and storing the
	 * huffman huffmanCodes in a map.
	 *
	 * @param root
	 * @param string
	 * @param huffmanCode
	 */
	public void encode(HuffmanNode root, String string, Map<Character, String> huffmanCode){
		if(root == null) {
			return;
		}
		
		if(root.hasNoChild() == true) {
			huffmanCode.put(root.character, string);
		}
		
		encode(root.left, string + "0", huffmanCode);
		encode(root.right, string + "1", huffmanCode);
	}
	
	/**
	 * Method to determine each character's frequency in input text
	 * and return result as Map.
	 * 
	 * @param text
	 * @return Map
	 */
	public Map<Character, Integer> getMap(String text){
		Map<Character, Integer> frequencyMap = new HashMap<>();
		for(int i = 0; i < text.length(); i++) {
			if(!frequencyMap.containsKey(text.charAt(i))) {
				frequencyMap.put(text.charAt(i), 0);
			} else {
				frequencyMap.put(text.charAt(i), frequencyMap.get(text.charAt(i)) + 1);
			}
		}
		return frequencyMap;
	}
	
	/**
	 * Method for getting initial Priority Queue, that has
	 * minimum heap invariant.
	 */
	public PriorityQueue<HuffmanNode> getHuffmanTree(Map<Character, Integer> map){
		
		PriorityQueue<HuffmanNode> ht = new PriorityQueue<>((l,r) -> l.frequency - r.frequency);
		
		/** Creating a leaf node for each character and adding to pQ. */
		for(Map.Entry<Character, Integer> entry: map.entrySet()) {
			ht.add(new HuffmanNode(entry.getKey(), entry.getValue()));
		}	

		/** Repeat till there's only one node left in pQ. */
		while(ht.size() > 1) {
			/**
			 * Remove the two nodes of the highest priority.
			 * Or two nodes with lowest frequency.
			 */
			HuffmanNode left = ht.poll();
			HuffmanNode right = ht.poll();
		
			/**
			 * Create a new interval node with these two nodes as children, and
			 * with frequency equal to the sum of the two nodes' frequencies.
			 * Add the new node to the priority queue.
			 */
			int sum = left.frequency + right.frequency;
			ht.add(new HuffmanNode('\0', sum, left, right));
		}
		return ht;
	}
	
	
	public Map<Character, String> getHuffmanCodes(PriorityQueue<HuffmanNode> huffmanTree0){
		HuffmanNode root = huffmanTree0.peek();
		
		/** Traverse the Huffman Tree and store the Huffman Codes in a Map. */
		Map<Character, String> hc = new HashMap<>();
		encode(root, "", hc);
		return hc;
	}
	
	public StringBuilder getCodeDirectory(Map<Character, String> hc, StringBuilder sb) {
		for(Map.Entry<Character, String> entry: hc.entrySet()) {
			sb.append("\n" + entry.getKey() + ":" + entry.getValue());
		}
		return sb;
	}
	
	public List<String> getBits(Map<Character, String> hc, String m) {
		List<String> b = new ArrayList<String>();
		for(int i = 0; i < m.length(); i++) { 
			b.add(hc.get(m.charAt(i)));
		}
		return b;
	}
	
	/**
	 * Part of this method I saw on Github.
	 * @author David Foster & Zeeshan Karim
	 * @author Steve Mwangi
	 * @param hc
	 * @param m
	 * @param bytes
	 * @return String
	 */
	private byte[] getCompressed(Map<Character, String> hc, String m, byte[] bytes) {
		StringBuilder compressedBits = new StringBuilder();
		for(int i = 0; i<m.length(); i++) {
			compressedBits.append(hc.get(m.charAt(i)));
		}
		
		bytes = new byte[compressedBits.length()/8];
		for(int i = 0; i<bytes.length; i++) {
			bytes[i] = (byte) Integer.parseUnsignedInt(compressedBits.substring(i*8, (i*8)+8), 2);
		}
		encoded = compressedBits.toString();
		return bytes;
	}
	
	/**
	 *  (Optional)  String decode(List<byte[]>, Map<Character, String> huffmanCodes) ­ this
	 *   method will  take the output of Huffman’s encoding and produce the original text.
	 */
	public String decode(List<String> bits, Map<Character, String> codes) {
		StringBuilder decodedBits = new StringBuilder();
		for(int i = 0; i< bits.size(); i++ ) {
			for(Map.Entry<Character, String> entry: codes.entrySet()) {
				if(entry.getValue().equals(bits.get(i))) {
					decodedBits.append(entry.getKey());
				}
			}
		}
		return decodedBits.toString();
	}
	
	/**
	 * Inner class for Huffman tree nodes.
	 * @author Steve Mwangi
	 * 
	 * Sources: www.techiedelight.com/huffman-coding/
	 */
	public class HuffmanNode implements Comparable<HuffmanNode>{
		char character;
		int frequency;
		
		HuffmanNode left, right;
		
		HuffmanNode(char c, int hz){
			this.character = c;
			this.frequency = hz;
		}
		
		HuffmanNode(char c, int hz, HuffmanNode left, HuffmanNode right){
			this.character = c;
			this.frequency = hz;
			this.left = left;
			this.right = right;
		}
		
		@Override
		public int compareTo(HuffmanNode other) {
			return this.frequency - other.frequency;
		}
		
		public boolean hasNoChild() {
			return this.left == null && this.right == null;
		}
		
		public String toString() {
			return "Huffman node has character: " + character + ", which occurs: " + frequency + " times.";
		}
	}
	
}
