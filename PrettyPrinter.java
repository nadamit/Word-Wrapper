/*
 * @Author: Amit Nadkarni
 * */

/*Import statements*/
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PrettyPrinter {
	// Stores the words in the file to be printed
	static String wordArray[];

	/*
	 * Method Name: Main Parameters: arguments Return type: Void Description:
	 * Prints the text, in a way that, the penalty of slack is minimized
	 */
	public static void main(String args[]) throws IOException {

		// Creating Scanner Object
		Scanner kbd = new Scanner(System.in);
		// Specifying number of characters on each line
		int numberOfCharactersOnLine;
		// List to store words
		ArrayList<String> wordlist = new ArrayList<String>();
		// Creating buffered Reader object
		BufferedReader br = null;
		String fileName = null;
		System.out.println("Enter the path of the file you want to read");
		fileName = kbd.nextLine();
		System.out.println("Enter maximum number of characters on the line");
		// Can accommodate maximum of n Characters in the line
		numberOfCharactersOnLine = kbd.nextInt();
		int numberOfWords;
		// Reading text from file
		String text = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			while ((text = br.readLine()) != null) {
				// Separating words in the sentence by the space delimiter
				String words[] = text.split(" ");
				for (int i = 0; i < words.length; i++) {
					// Adding word to the list
					wordlist.add(words[i]);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Close the connection

			br.close();
		}

		// Remove spaces if any in the input.
		// Parse through the entire list
		for (int i = 0; i < wordlist.size(); i++) {
			if (wordlist.get(i).equals("")) {
				wordlist.remove(i);
			}

		}

		// Converting word list into an array consisting of words
		wordArray = new String[wordlist.size()];
		// Putting elements of the list in an word array
		for (int j = 0; j < wordlist.size(); j++) {
			wordArray[j] = wordlist.get(j);

		}

		int lengthOfeachWord[] = new int[wordArray.length];
		// This array consist length of each word in the word Array.
		for (int i = 0; i < wordArray.length; i++) {
			if (wordArray[i].length() > numberOfCharactersOnLine) {
				System.out.println("Length of one of the word exceeded the length of the line");
				System.exit(0);
			} else {
				lengthOfeachWord[i] = wordArray[i].length();
			}
		}
		// Calculating the number of words in the paragraph
		numberOfWords = lengthOfeachWord.length;

		// Stores the slack
		int spaceCounterintheLine[][] = new int[numberOfWords + 1][numberOfWords + 1];
		// Stores the cost by squaring the slack calculated
		int costmatrix[][] = new int[numberOfWords + 1][numberOfWords + 1];

		// Calculating the spaces left on the line after considering particular
		// word
		int p = 1;
		while (p <= numberOfWords) {
			// Number of characters allowed on the line minus the length of word
			// considered will give the slack
			spaceCounterintheLine[p][p] = numberOfCharactersOnLine
					- lengthOfeachWord[p - 1];

			int q = p + 1;
			while (q <= numberOfWords) {
				// Considering the words on the line and calculating the
				// remaining extra spaces left.
				spaceCounterintheLine[p][q] = spaceCounterintheLine[p][q - 1]
						- lengthOfeachWord[q - 1] - 1;
				q++;
			}
			p++;
		}
		// calculating the cost matrix
		for (int i = 1; i <= numberOfWords; i++) {
			for (int j = i; j <= numberOfWords; j++) {
				if (spaceCounterintheLine[i][j] < 0)
					// If the words is not considered on that particular line,
					// because the number of spaces left
					// goes in negative
					costmatrix[i][j] = 999999999;
				// Considering 999999999 as infinity

				else
					// Calculating the slack cost (Square of slack calculated)
					// after considering each word.
					costmatrix[i][j] = (int) Math
							.sqrt(spaceCounterintheLine[i][j]);

			}
		}

		// Stores the optimum cost required to print the word
		int finalCostMatrix[] = new int[numberOfWords + 1];
		// Stores the indexes of the word and index of an array determines the
		// word
		// and value-1 determines the word on the ending of previous line
		int backtracker[] = new int[numberOfWords + 1];

		for (int m = 1; m <= numberOfWords; m++)

			while (m <= numberOfWords) {
				// Initializing with Infinity initially, as we have to compute
				// minimum of the cost required to place i th item in the line
				finalCostMatrix[m] = 999999999;
				for (int n = 1; n <= m; n++) {
					// Calculates the minimum cost required to place that word
					// on
					// the line
					if (costmatrix[n][m] != 999999999
					// It uses the previously computed optimal value of
					// the
					// word required to
					// place in the line
							&& (finalCostMatrix[n - 1] + costmatrix[n][m] < 									finalCostMatrix[m])) {
						// Assigning the optimal value required to store the
						// word
						finalCostMatrix[m] = finalCostMatrix[n - 1]
								+ costmatrix[n][m];
						// Stores the index at of the word at which new line
						// starts
						backtracker[m] = n;
					}
				}
				m++;
			}
		// Calling the function to print the words on the respective lines.
		backtracksolution(backtracker, numberOfWords);

	}

	/*
	 * Method Name: backtrack solution Method Parameters: array[](line
	 * breakers), numberOfWords Description: Displays the words placed on
	 * respective lines.
	 */
	public static int backtracksolution(int backtracker[], int numberOfWords) {
		int word;
		// If the word is first word
		if (backtracker[numberOfWords] == 1)
			word = 1;
		else
			word = backtracksolution(backtracker,
					backtracker[numberOfWords] - 1) + 1;
		// Print the words on the line respectively
		int i = backtracker[numberOfWords] - 1;
		while (i <= numberOfWords - 1) {
			System.out.print(wordArray[i] + " ");
			i++;
		}
		// Display on new Line
		System.out.println(" ");
		return word;
	}

}

