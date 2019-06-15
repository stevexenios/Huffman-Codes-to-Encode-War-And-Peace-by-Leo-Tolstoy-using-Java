import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	// Messages.
	public StringBuilder huffmanCodeDirectory;
	// Char-String Map.
	public Map<Character, String> huffmanCodes = new HashMap<>();
	// Count frequency of appearance of each character and store in map..
	public final Map<Character, Integer> frequencyMap;
	// List of Nodes to Help create Huffman Tree.
	public ArrayList<HuffmanNode> huffmanNodeList = new ArrayList<HuffmanNode>();
	// Final huffman tree.
	public final HuffmanNode huffmanTree;
	// Array of Bytes for compressed message.
	public byte[] bytes;
	// List data member for encoded message.
	public final List<String> bits;
	// String data member for decoded message.
	public String encoded = "";
	// String to represent book
	public String book;
	
	
	
	/**
	 *  void CodingTree(String message) ­ a constructor that takes the
	 *  text of a message to be  compressed. 
	 *  
	 *  The constructor is responsible for calling all private methods 
	 *  that carry out  the Huffman coding algorithm.  
	 *
	 * @param message
	 * @throws Exception 
	 */
	public CodingTree(String message) throws Exception {
		book = message;
		frequencyMap = getMap(message);
		huffmanTree = getHuffmanTree(huffmanNodeList);
		encode(huffmanTree, "");
		huffmanCodeDirectory = getCodeDirectory(huffmanCodes, new StringBuilder("Huffman Codes are : "));
		intializeCompressedBytes();
		bits = getBits(huffmanCodes, message);
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
				frequencyMap.put(text.charAt(i), 1);
				huffmanNodeList.add(new HuffmanNode(text.charAt(i), 1, null, null));
			} else {
				frequencyMap.put(text.charAt(i), frequencyMap.get(text.charAt(i)) + 1);
			}
		}
		for (HuffmanNode n: huffmanNodeList) {
			n.frequency = frequencyMap.get(n.character);
		}
		return frequencyMap;
	}
	
	/**
	 * Method to return the root of our Huffman Tree.
	 * 
	 * @param nodeList
	 * @return huffmanNode
	 */
	private HuffmanNode getHuffmanTree(ArrayList<HuffmanNode> list) {
		ArrayList<HuffmanNode> temp = new ArrayList<>();
		for (HuffmanNode n: list) {
			temp.add(n);
		}
		while (temp.size() > 1) {
			Collections.sort(temp);
			HuffmanNode n = new HuffmanNode('\0', temp.get(0).frequency + temp.get(1).frequency, temp.get(0), temp.get(1));
			temp.remove(0);
			temp.remove(0);
			temp.add(0, n);
		}
		return temp.get(0);	
	}
	
	/**
	 * This method is for traversing the huffman tree and storing the
	 * huffman huffmanCodes in a map.
	 *
	 * @param root
	 * @param string
	 */
	public void encode(HuffmanNode root, String string){
		if(root.hasNoChild()) {
			huffmanCodes.put(root.character, string);
		} else {
			encode(root.left, string + "0");
			encode(root.right, string + "1");
		}
	}
	
	/**
	 * Returns the Directory as a stringbuilder.
	 * 
	 * @param hc
	 * @param sb
	 * @return sb
	 */
	public StringBuilder getCodeDirectory(Map<Character, String> hc, StringBuilder sb) {
		for(Map.Entry<Character, String> entry: hc.entrySet()) {
			sb.append("\n" + entry.getKey() + ":" + entry.getValue());
		}
		return sb;
	}
	
	/**
	 * Creates a list of string bits
	 * @param hc
	 * @param m
	 * @return theList
	 */
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
	 * @throws Exception 
	 * 
	 */
	private void intializeCompressedBytes() throws Exception {
		StringBuilder compressedBits = new StringBuilder();
		for(int i = 0; i<book.length(); i++) {
			compressedBits.append(huffmanCodes.get(book.charAt(i)));
		}
		int bits = 8;
		bytes = new byte[compressedBits.length()/bits];
		for(int i = 0; i<bytes.length; i++) {
			bytes[i] = (byte) Integer.parseUnsignedInt(compressedBits.substring(i*bits, (i*bits)+bits), 2);
		}
		encoded = compressedBits.toString();
		//bytes = compress(bytes);
	}
	
//	/**
//	 * This method helps compress the byte array above using the in built
//	 * deflater.
//	 * 
//	 * Sources: https://dzone.com/articles/how-compress-and-uncompress
//	 * @param data
//	 * @return
//	 * @throws Exception 
//	 */
//	private byte[] compress(byte[] data) throws Exception {
//		Deflater deflater = new Deflater();  
//	    deflater.setInput(data);  
//	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);   
//	    deflater.finish();  
//	    byte[] buffer = new byte[1024];   
//	    while (!deflater.finished()) {  
//	    	int count = deflater.deflate(buffer); // returns the generated code... index  
//	    	outputStream.write(buffer, 0, count);   
//	    }  
//	    outputStream.close();  
//	    byte[] output = outputStream.toByteArray();  
//	    return output;
//	}
	
	/**
	 *  (Optional)  String decode(List<byte[]>, Map<Character, String> huffmanCodes) ­ this
	 *   method will  take the output of Huffman’s encoding and produce the original text.
	 */
	public String decode() {
		StringBuilder decodedBits = new StringBuilder();
		for(int i = 0; i< bits.size(); i++ ) {
			for(Map.Entry<Character, String> entry: huffmanCodes.entrySet()) {
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
