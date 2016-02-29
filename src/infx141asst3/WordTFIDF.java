package infx141asst3;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by VGDC_1 on 2/18/2016.
 * Brett Lenz 76382638
 * Carl Pacheco 47911659
 * Derek Edrich 34363846
 * Khang Tran 47508988
 */
public final class WordTFIDF implements Serializable
{
    public String word;
    public int IDF;
    // (Filename, (occurrences, occurrences/TotalNumOfWords))
    public HashMap<String, Pair<Integer, Double>> TFs;

    public WordTFIDF(String word)
    {
        this.word = word;
        IDF = 0;
        TFs = new HashMap<String, Pair<Integer, Double>>();
    }

    //adds a value to the map of TFs
    public void add(String filename, int occurrences, int totalWordCount)
    {
        double roundingFactor = 10000.0;
        double termFrequencyPerDoc = Math.round(((double) occurrences / totalWordCount)*roundingFactor)/roundingFactor;

        Pair<Integer, Double> tfInfo = new Pair<>(occurrences, termFrequencyPerDoc);

        TFs.put(filename, tfInfo);
        IDF += occurrences;
    }

    //finds the TFIDF of the word for the file specified
    public double TFIDF(String filename, int corpusSize)
    {
        if (TFs.containsKey((filename)))
        {
            return (1 + Math.log(TFs.get(filename).getKey())) * Math.log((double) corpusSize / (double) TFs.get(filename).getKey());
        }
        else
            return 0;
    }

    //returns the filename with the highest TFIDF for the word
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

    public double getTFIDF(String filename) {
        double result = 0;
        if (TFs.containsKey(filename))
            return TFs.get(filename).getValue();
        return result;
    }

    @Override
    public String toString()
    {
        return TFs.toString();
    }
}