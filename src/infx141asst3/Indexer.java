package infx141asst3;

import javafx.util.Pair;

import java.io.*;
import java.util.*;

/**
 * Created by VGDC_1 on 2/8/2016.
 * <p/>
 * Brett Lenz 76382638
 * Carl Pacheco 47911659
 * Derek Edrich 34363846
 * Khang Tran 47508988
 */
public class Indexer
{
    public HashMap<String, WordTFIDF> index;
    public int corpusSize;
    public HashMap<String, Integer> docSizes;

    public Indexer()
    {
        corpusSize = 0;
        index = new HashMap<String, WordTFIDF>();
        docSizes = new HashMap<String, Integer>();
    }

    public Indexer(String filename)
    {
        read(filename);
    }

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
            if (!index.containsKey(frequency.getText()))
                index.put(frequency.getText(), new WordTFIDF(frequency.getText()));

            double termFrequency = Math.round(((double) frequency.getFrequency() / numWords)*10000.0)/10000.0;

            index.get(frequency.getText()).add(new Pair<String, Pair<Integer, Double>>(filename, new Pair<Integer, Double>(frequency.getFrequency(), termFrequency)));
        }
    }

    public boolean saveSerializable()
    {
        try
        {
            FileOutputStream fileOut = new FileOutputStream("index.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(index);
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

    private void read(String filename)
    {
        try
        {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            index = (HashMap<String, WordTFIDF>) in.readObject();
            in.close();
            fileIn.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void saveToText()
    {
        try
        {
            PrintWriter writer = new PrintWriter("theIndex.txt", "UTF-8");

            for (String k : index.keySet())
                writer.println(k + " -> " + index.get(k).toString());

            writer.close();
        }
        catch (Exception e)
        {

        }
    }

    public String maxTFIDF(String word)
    {
        if (!index.containsKey(word))
            return "Not Found";
        else
            return index.get(word).maxTFIDF(corpusSize);
    }

    /*
    convert the array into a set (so that users can’t put the same word twice)
    for every term
        if (index.containsKey(term))

    How do we work with multiple terms? (set union)

    String results;
    Store the results (documents) in a priority queue, sorted by the doc’s TFIDF
    Loop through the queue for however many results you want
        if there are less results than the number asked for, return that many
        append the result to the results string
    return the results string
     */
    //TODO Return search results
    public String searchResults(String[] terms) {
        //remove duplicates from array
        String results = "";
        HashSet<String> termSet = new HashSet<>(Arrays.asList(terms));
        PriorityQueue<Pair<String, Double>> resultsQueue = new PriorityQueue<Pair<String, Double>>();

        for (String term : termSet)
        {
            if (index.containsKey(term)) {
                //Use index.get(term) to get its WordTFIDF
                
            }
        }


        return results;
    }

    public static void main(String[] args)
    {
        Indexer indexer = new Indexer();

        File folder = new File("pages");
        String[] files = folder.list();

        int wordCount = 0;
        long start = System.currentTimeMillis();

        for (String file : files)
        {
            List<String> words = Utilities.tokenizeFile(new File("pages/" + file));
            wordCount += words.size();
            List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
            indexer.addFrequencies(file, frequencies);
        }

//        String runtime = ((Long) (System.currentTimeMillis() - start)).toString();
//        System.out.println("Runtime: " + runtime);
//
//        System.out.println("Doc Wordcount: " + indexer.docSizes.toString());
//
//        System.out.println("Num Unique Words: " + indexer.index.keySet().size());
//
//        System.out.println("Word Count: " + ((Integer) indexer.corpusSize).toString());

        indexer.saveToText();

        //indexer.saveSerializable();

        try
        {
            Scanner input = new Scanner(System.in);

            while (true)
            {
                System.out.print("Search: ");
                String inputString = input.nextLine().trim();
                if (inputString.equals("quit"))
                    break;

                String[] searchTerms = inputString.split(" ");

                System.out.println(indexer.maxTFIDF(inputString));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
