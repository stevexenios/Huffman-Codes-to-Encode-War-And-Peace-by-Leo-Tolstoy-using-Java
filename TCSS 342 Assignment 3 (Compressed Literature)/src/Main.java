import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*
 * TCSS 342 - Data Structures
 * Assignment 3 - Compressed Literature 
 */

/**
 * You are also responsible for implementing a Main controller that 
 * uses the CodingTree class to  compress a file.  
 * 
 * The main must:  
 * ● Read the contents of a text file into a String.  
 * 
 * ● Pass the String into the CodingTree in order to 
 * initiate Huffman’s encoding procedure  and  generate
 * a map of huffmanCodes.  
 * 
 * ● Output the huffmanCodes to a text file. 
 * 
 * ● Output the compressed message to a binary file.  
 * 
 * ● Display compression and run time statistics.  
 * 
 * @author Steve Mwangi
 * @version Spring 2019
 *
 */
public class Main {
	
	/**
	 * You will also create a Main class that is capable of compressing
	 * a number of files and includes  methods used to test components
	 * of your program.   
	 * 
	 * ● void main(String[] args) ­ this method will carry out compression
	 *  of a file using the  CodingTree class.  
	 * 		○ Read in from a textfile.  You may hardcode the filename
	 * 		  into your program but  make sure you test with more than
	 * 		  one file.  
	 * 
	 * 		○ Output to two files.  Again feel free to hardcode the names
	 * 		  of these files into your  program.  These are the huffmanCodes text
	 * 		  file and the compressed binary file.  
	 * 
	 * 		○ Display statistics.  You must output the original size 
	 * 		 (in kilobytes), the  compressed size (in kilobytes), the 
	 * 		 compression ratio (as a percentage) and the  elapsed time
	 * 		 for compression.  
	 * 
	 * ● void testCodingTree() ­ this method tests the coding tree on a 
	 *   short simple phrase so  you can verify its correctness.  
	 *   
	 * ● Test files ­ include at least one test file that is a piece of 
	 *   literature of considerable size.  
	 *   	○ Check out Project Gutenberg  an online database of literature in text format.    
	 */

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		File initialFile = new File("WarAndPeace.txt");
		BufferedReader myBr = new BufferedReader(new FileReader(initialFile));
		StringBuilder text = new StringBuilder();
		long start = System.currentTimeMillis();
		while(myBr.ready()) {
			String line = myBr.readLine();
			text.append(line + "\n");
		}
		
		CodingTree codingTree = new CodingTree(text.toString());
		
		File codes = new File("codes.txt");
		if(!codes.exists()) {
			codes.createNewFile();
		}
		BufferedWriter huffmanCodeDirectory = new BufferedWriter(new FileWriter(codes.getAbsoluteFile()));
		huffmanCodeDirectory.write(codingTree.huffmanCodeDirectory.toString());
		
		File compressedFile = new File("compressed.txt");
		if(!compressedFile.exists()) {
			compressedFile.createNewFile();
		}
		FileOutputStream compressed = new FileOutputStream(compressedFile.getAbsoluteFile());
		compressed.write(codingTree.bytes);
		
		myBr.close();
		huffmanCodeDirectory.close();
		compressed.close();
		
		long end = System.currentTimeMillis();
		long runtime = end - start;
		System.out.println("Runtime: " + runtime/1000 + " Seconds." );
		System.out.println("Original File Size: " + initialFile.length()/1000 + " kilobytes.");
		System.out.println("Final File Size: " + compressedFile.length()/1000 + " kilobytes.");
		
		/**
		 * Decoding.
		 */
		File decodedFile = new File("decoded.txt");
		if(!decodedFile.exists()) {
			decodedFile.createNewFile();
		}
		BufferedWriter decoded = new BufferedWriter(new FileWriter(decodedFile.getAbsoluteFile()));
		decoded.write(codingTree.decoded);
		decoded.close();
		
		System.out.println(System.lineSeparator() + "Testing the coding tree class. ");
		testCodingTree("How far that little candle throws his beams! So shines a good deed in a weary world.");
	}
	
	/**
	 * The test method.
	 * @param text
	 */
	public static void testCodingTree(String text) {
		CodingTree c = new CodingTree(text);
		System.out.println("Character Map: " + c.frequencyMap);
		System.out.println("Directory: " + c.huffmanCodes);
		System.out.println("Compressed1: " + c.encoded);
		System.out.println("Decoded message: " + c.decoded);
	}

}