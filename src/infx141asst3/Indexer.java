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
    private HashMap<String, List<Pair<String, Integer>>> index;

    public Indexer() {
        index = new HashMap<String, List<Pair<String, Integer>>>();
    }

    public Indexer(String filename) {
        read(filename);
    }

    public void addFrequencies(String filename, List<Frequency> frequencies) {
        for (int i = 0; i < frequencies.size(); i++) {
            if (!index.containsKey(frequencies.get(i).getText()))
                index.put(frequencies.get(i).getText(), new ArrayList<Pair<String, Integer>>());

            index.get(frequencies.get(i).getText()).add(new Pair<String, Integer>(filename, frequencies.get(i).getFrequency()));
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
            index = (HashMap<String, List<Pair<String, Integer>>>) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println(index);
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
        System.out.println("Word Count: " + ((Integer)wordCount).toString());

        indexer.print();

        indexer.save();
    }
}
