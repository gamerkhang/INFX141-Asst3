package infx141asst3;

import com.sun.org.apache.xpath.internal.SourceTree;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by VGDC_1 on 2/8/2016.
 */
public class Indexer {
    public HashMap<String, WordTFIDF> index;
    public int corpusSize;
    public HashMap<String, Integer> docSizes;

    public Indexer() {
        corpusSize = 0;
        index = new HashMap<String, WordTFIDF>();
        docSizes = new HashMap<String, Integer>();
    }

    public Indexer(String filename) {
        read(filename);
    }

    public void addFrequencies(String filename, List<Frequency> frequencies) {
        int numWords = 0;
        for (int i = 0; i < frequencies.size(); i++) {
            numWords += frequencies.get(i).getFrequency();
        }

        corpusSize += numWords;

        docSizes.put(filename, numWords);

        for (int i = 0; i < frequencies.size(); i++) {
            if (!index.containsKey(frequencies.get(i).getText()))
                index.put(frequencies.get(i).getText(), new WordTFIDF(frequencies.get(i).getText()));

            index.get(frequencies.get(i).getText()).add(new Pair<String, Pair<Integer, Double>>(filename,
                    new Pair<Integer, Double>(frequencies.get(i).getFrequency(),
                            (double) (frequencies.get(i).getFrequency()) / (double) numWords)));
        }
    }

    public boolean save() {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("pages/index.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(index);
            out.close();
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void read(String filename) {
        try {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            index = (HashMap<String, WordTFIDF>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println(index);
    }

    public String maxTFIDF(String word) {
        if(index.containsKey(word))
            return index.get(word).maxTFIDF(corpusSize);
        else
            return "";
    }

    public static void main(String[] args) {
        Indexer indexer = new Indexer();

        File folder = new File("pages");
        String[] files = folder.list();

        int wordCount = 0;
        long start = System.currentTimeMillis();

        for(int i = 0; i < files.length; i++)
        {
            String filename = "pages/"+ files[i];
            List<String> words = Utilities.tokenizeFile(new File(filename));
            wordCount += words.size();
            List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
            indexer.addFrequencies(filename, frequencies);
        }

        String runtime = ((Long)(System.currentTimeMillis()-start)).toString();
        System.out.println("Runtime: " + runtime);

        System.out.println("Doc Wordcount: " + (indexer.docSizes).toString());

        System.out.println("Num Unique Words: " + indexer.index.keySet().size());

        System.out.println("Word Count: " + ((Integer)indexer.corpusSize).toString());

        indexer.print();

        indexer.save();

        //test searching

        System.out.println(indexer.maxTFIDF("pollution"));
    }
}
