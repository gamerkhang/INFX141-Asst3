package infx141asst3;
import java.io.File;

import java.util.*;

//TO REMOVE

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */


	public static List<Frequency> computeWordFrequencies(List<String> words)
	{
		// Create arraylist to hold frequency objects
		List<Frequency> frequencies = new ArrayList<>();

		// Check if word list is null - return empty list
		if (words == null || words.isEmpty())
			return frequencies;

		// Loop through a set of words - eliminating duplicates
		for (String word : new HashSet<>(words))
		{
			// Get the count of word occurennces in the word list
			int count = Collections.frequency(words,word);

			// Add the frequency to the list
			frequencies.add(new Frequency(word,count));
		}

//		// Sort by frequency count then alphabetically
//		frequencies.sort(( Frequency a, Frequency b) -> {
//					if (Integer.compare(b.getFrequency(), a.getFrequency()) == 0)
//						return a.getText().compareTo(b.getText());
//					else
//						return Integer.compare(b.getFrequency(), a.getFrequency());
//				});

		return frequencies;
	}
	
	/**
	 * Runs the word frequency counter. The input should be the path to a text file.
	 * 
	 * @param args The first element should contain the path to a text file.
	 */
	public static void main(String[] args)
	{
		File file = new File(args[0]);
		List<String> words = Utilities.tokenizeFile(file);
		List<Frequency> frequencies = computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);
	}
}
