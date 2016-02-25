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
    public HashMap<String, Pair<Integer, Double>> TFs;

    public WordTFIDF(String word)
    {
        this.word = word;
        IDF = 0;
        TFs = new HashMap<String, Pair<Integer, Double>>();
    }

    public void add(Pair<String, Pair<Integer, Double>> data)
    {
        TFs.put(data.getKey(), data.getValue());
        IDF += data.getValue().getKey();
    }

    public double TFIDF(String filename, int corpusSize)
    {
        if (TFs.containsKey((filename)))
        {
            return (1 + Math.log(TFs.get(filename).getKey())) * Math.log((double) corpusSize / (double) TFs.get(filename).getKey());
        }
        else
            return 0;
    }

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

    @Override
    public String toString()
    {
        return TFs.toString();
    }
}