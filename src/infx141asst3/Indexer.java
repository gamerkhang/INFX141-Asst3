/**
 * Brett Lenz 76382638
 * Carl Pacheco 47911659
 * Derek Edrich 34363846
 * Khang Tran 47508988
 */

package infx141asst3;

import javafx.util.Pair;
import java.io.*;
import java.util.*;

public class Indexer
{
    // Map for index
    public HashMap<String, WordTFIDF> index;
    public int corpusSize;
    // Map of document and its word size
    public HashMap<String, Integer> docSizes;

    // Constructor - default
    public Indexer()
    {
        corpusSize = 0;
        index = new HashMap<String, WordTFIDF>();
        docSizes = new HashMap<String, Integer>();
    }

    // Constructor - Passing in file
    public Indexer(String filename)
    {
        read(filename);
    }

    //Adds all the words in the file to the Index
    public void addFrequencies(String filename, List<Frequency> frequencies)
    {
        int numWords = 0;

        for (Frequency frequency : frequencies)
        {
            numWords += frequency.getFrequency();
        }

        corpusSize += numWords;

        docSizes.put(filename, numWords);

        for (Frequency frequency : frequencies)
        {
            String word = frequency.getText();

            if (!index.containsKey(word))
            {
                index.put(word, new WordTFIDF(word));
            }

            index.get(word).add(filename, frequency.getFrequency(), numWords);
        }
    }

    // Serialize data
    public boolean saveSerializable()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("index.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(index);
            out.writeObject(corpusSize);
            out.close();
            fileOut.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void read(String filename)
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            index = (HashMap<String, WordTFIDF>) in.readObject();
            corpusSize = (int) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    // Finds URL of the DocID
    public static String docIdToURL(String docID)
    {
        String result = "";
        try
        {
            String lineNumber = docID.split("\\.")[0];
            BufferedReader br = new BufferedReader(new FileReader(new File("urlToDocID.txt")));

            while (true)
            {
                String[] tmp = br.readLine().split(" ");
                if (tmp[0].equals(lineNumber))
                {
                    return tmp[1];
                }
            }
        }
        catch (Exception e)
        {
        }
        return result;
    }


    // Get the search results of the terms that are passed in
    public ArrayList searchResults(String[] terms)
    {
        //remove duplicates from array
        HashSet<String> termSet = new HashSet<>(Arrays.asList(terms));
        //pairs are (filename, tfIDF of all the words in the search term array combined)
        HashMap<String, Double> documentScores = new HashMap<>();

        for (String term : termSet)
        {
            if (index.containsKey(term))
            {
                //Use index.get(term) to get its WordTFIDF
                WordTFIDF currentWord = index.get(term);

                //loop through all files
                for (String file : currentWord.TFs.keySet())
                {
                    //if they're not in the map, add them to the map
                    if (!documentScores.containsKey(file))
                    {
                        documentScores.put(file, currentWord.TFIDF(file, corpusSize));
                    }
                    //else, increment the score of the file w/ the score for the current term
                    else
                    {
                        double newScore = documentScores.get(file) + currentWord.TFIDF(file, corpusSize);
                        documentScores.put(file, newScore);
                    }
                }
            }
        }

        // Holds the ordered results based on TFIDF
        ArrayList<Pair<String, Double>> resultsQueue = new ArrayList<>();
        //Build the pQueue with the objects in the hashmap
        for (Map.Entry<String, Double> entry : documentScores.entrySet())
        {
            resultsQueue.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        // Sort by frequency count then alphabetically
        resultsQueue.sort((Pair<String, Double> a, Pair<String, Double> b) -> {
            return Double.compare(b.getValue(), a.getValue());
        });

        // If resultQueue is empty then nothing was found else, build the result string with 5 URLS
        if (resultsQueue.isEmpty())
        {
            System.out.println("Nothing Found");
        }


        return resultsQueue;
    }

    public static void main(String[] args)
    {
        // Initialize index
        Indexer indexer = new Indexer();
        // Folder for corpus
        File folder = new File("pages");
        // List of files in corpus
        String[] files = folder.list();
        // Serialization
        File serializedIndex = new File("index.ser");

        // If serializable file exists read it, else build index
        if (serializedIndex.exists())
        {
            indexer.read("index.ser");
        }
        else
        {
            int wordCount = 0;
            int count = 0;
            // For each file, add info to index
            for (String file : files)
            {
                List<String> words = Utilities.tokenizeFile(new File("pages/" + file));
                wordCount += words.size();
                List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
                indexer.addFrequencies(file, frequencies);
                System.out.println(file + " Number: " + (count++));
            }

            // Serialize the index
            indexer.saveSerializable();
        }
        try
        {
            // Search input
            Scanner input = new Scanner(System.in);

            while (true)
            {
                System.out.print("Search: ");
                String inputString = input.nextLine().trim().toLowerCase();

                // Close search
                if (inputString.equals("quit"))
                {
                    break;
                }

                //Put terms into array
                String[] searchTerms = inputString.split(" ");

                // Display search results
                ArrayList<Pair> results = indexer.searchResults(searchTerms);

                int loop = 0;

                if (results.size() > 5)
                {
                    loop = 5;
                }
                else
                {
                    loop = results.size();
                }

                for (int i = 0; i < loop; i++)
                {
                    System.out.println((i + 1) + ". " + docIdToURL(results.get(i).getKey() + "\n"));
                    //+ ", Score: " + resultsQueue.get(i).getValue() + "\n";
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
