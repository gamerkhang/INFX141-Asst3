

package infx141asst3;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities
{
    /**
     * Reads the input text file and splits it into alphanumeric tokens.
     * Returns an ArrayList of these tokens, ordered according to their
     * occurrence in the original text file.
     * <p>
     * Non-alphanumeric characters delineate tokens, and are discarded.
     * <p>
     * Words are also normalized to lower case.
     * <p>
     * Example:
     * <p>
     * Given this input string
     * "An input string, this is! (or is it?)"
     * <p>
     * The output list of strings should be
     * ["an", "input", "string", "this", "is", "or", "is", "it"]
     *
     * @param input The file to read in and tokenize.
     * @return The list of tokens (words) from the input file, ordered by occurrence.
     */

    public static ArrayList<String> tokenizeFile(File input)
    {
        // List of tokens
        ArrayList<String> tokens = new ArrayList<String>();
        Scanner file;

        try
        {
            // Scan the file for text
            file = new Scanner(input);

            // Loop through file
            while (file.hasNext())
            {
                // For each word, modify it to make lower case and alphanumeric
                String[] word = file.next().split("[^a-zA-Z0-9]");

                // Place tokens into token list
                for (String words: new ArrayList<String>(Arrays.asList(word)))
                {
                    if (!words.equals(""))
                        tokens.add(words.toLowerCase());
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }
        return tokens;
    }

    /**
     * Takes a list of {@link Frequency}s and prints it to standard out. It also
     * prints out the total number of items, and the total number of unique items.
     * <p>
     * Example one:
     * <p>
     * Given the input list of word frequencies
     * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
     * <p>
     * The following should be printed to standard out
     * <p>
     * Total item count: 6
     * Unique item count: 5
     * <p>
     * sentence	2
     * the		1
     * this		1
     * repeats	1
     * word		1
     * <p>
     * <p>
     * Example two:
     * <p>
     * Given the input list of 2-gram frequencies
     * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
     * <p>
     * The following should be printed to standard out
     * <p>
     * Total 2-gram count: 6
     * Unique 2-gram count: 5
     * <p>
     * you think	2
     * how you		1
     * know how		1
     * think you	1
     * you know		1
     *
     * @param frequencies A list of frequencies.
     */
    public static void printFrequencies(List<Frequency> frequencies)
    {
        // TODO Write body!

        // Instantiate total to 0
        int total = 0;

        // Loop through the list and count the frequency counts
        for (Frequency frequency: frequencies)
            total += frequency.getFrequency();

        // Output the totals
        System.out.println("Total item count: " + total);
        System.out.println("Unique item count: " + frequencies.size());
        System.out.println();

        // Output the results
        for (Frequency frequency: frequencies)
            System.out.format("%-7s  %d\n", frequency.getText(),frequency.getFrequency());
    }
}
