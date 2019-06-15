import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 */

/**
 * @author steve
 *
 */
public class Main2 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader myBr = new BufferedReader(new FileReader("WarAndPeace2.txt"));
		StringBuilder text = new StringBuilder();
		long start = System.currentTimeMillis();
		while(myBr.ready()) {
			String line = myBr.readLine();
			text.append(line + System.lineSeparator());
		}
		
		CodingTree codingTree = new CodingTree(text.toString());
		System.out.println(codingTree.frequencyMap);
		System.out.println(System.lineSeparator() + "Directory: " + codingTree.huffmanCodeDirectory);
		System.out.println(System.lineSeparator() + "Compressed1: " + codingTree.encoded);
		System.out.println(System.lineSeparator() + "Compressed2: " + codingTree.bits.get(10));
		System.out.println(System.lineSeparator() + "Decoded message: " + codingTree.decoded);
		
		myBr.close();
		long end = System.currentTimeMillis();
		long runtime = end - start;
		System.out.println("Runtime: " + runtime + " milliseconds." );

	}

}
