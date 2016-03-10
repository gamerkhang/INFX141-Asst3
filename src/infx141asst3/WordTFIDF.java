/**
 * Brett Lenz 76382638
 * Carl Pacheco 47911659
 * Derek Edrich 34363846
 * Khang Tran 47508988
 */

package infx141asst3;

import javafx.util.Pair;
import java.io.Serializable;
import java.util.HashMap;

public final class WordTFIDF implements Serializable
{
    public String word;
    public int IDF;
    // HashMap TF Format: (key: Filename, value: (occurrences, occurrences/TotalNumOfWords))
    public HashMap<String, Pair<Integer, Double>> TFs;

    // Constructor
    public WordTFIDF(String word)
    {
        this.word = word;
        IDF = 0;
        TFs = new HashMap<>();
    }

    //Adds a value to the map of TFs
    public void add(String filename, int occurrences, int totalWordCount)
    {
        // Round TF
        double roundingFactor = 10000.0;
        // Term frequency Per Doc
        double termFrequencyPerDoc = Math.round(((double) occurrences / totalWordCount) * roundingFactor) / roundingFactor;

        Pair<Integer, Double> tfInfo = new Pair<>(occurrences, termFrequencyPerDoc);

        // Put filename and TF into Map
        TFs.put(filename, tfInfo);

        // Increment IDF
        IDF += occurrences;
    }

    //Finds the TFIDF of the word for the file specified
    public double TFIDF(String filename, int corpusSize)
    {
        // If TF is in Map, Calculate TFIDF, return 0
        if (TFs.containsKey((filename)))
        {
            return (1 + Math.log(TFs.get(filename).getKey())) * Math.log((double) corpusSize / (double) TFs.size());
        }
        else
        {
            return 0;
        }
    }

    //Returns the filename with the highest TFIDF for the word
    public String maxTFIDF(int corpusSize)
    {
        double max = -1;
        String maxFilename = "";
        for (String filename : TFs.keySet())
        {
            double testTFIDF = TFIDF(filename, corpusSize);
            if (testTFIDF > max)
            {
                max = testTFIDF;
                maxFilename = filename;
            }
        }
        return maxFilename;
    }


    // Return TFIDF of file
    public double getTFIDF(String filename)
    {
        double result = 0;
        if (TFs.containsKey(filename))
        {
            return TFs.get(filename).getValue();
        }
        return result;
    }

    @Override
    public String toString()
    {
        return TFs.toString();
    }
}