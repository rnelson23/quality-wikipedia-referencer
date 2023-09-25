import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import java.io.IOException;

public class Document {
    public HashTable frequencyTable = new HashTable(); // The frequency table of words in the document
    public double similarityScore; // The similarity score for the document
    public String link; // The link that the document was parsed from

    public Document(String link) {
        // Stores the link
        this.link = link;

        // Splits the link into an array of words
        for (String word : getWords(link)) {
            // Adds the word to the frequency table and the glossary
            frequencyTable.add(word);
            Main.glossary.add(word);
        }

        // Once the frequency table is filled, calculate the tf values
        frequencyTable.calculateTF();
    }

    /**
     * Gets the words from the link
     * @param link the link to get the words from
     * @return the words from the link
     */
    private String[] getWords(String link) {
        try {
            // Gets the html from the link
            org.jsoup.nodes.Document html = Jsoup.connect(link).get();

            // Gets the headers and paragraphs from the html
            Elements h2 = html.select("div.mw-parser-output > h2");
            Elements h3 = html.select("div.mw-parser-output > h3");
            Elements p = html.select("div.mw-parser-output > p");

            // Combines the headers and paragraphs into one string
            String text = h2.text() + h3.text() + p.text();

            // Splits the string into an array of words and returns it
            return text.toLowerCase().split("[^A-Za-z_'-]+");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates the similarity score for each document in the corpus
     * @return the similarity scores of all documents in the corpus
     */
    public Document getSimilarDocument() {
        // Resets the similarity score of each document
        for (Document document : Main.corpus) {
            document.similarityScore = 0;
        }

        // Calculates the similarity scores for each document
        frequencyTable.calculateSimilarityScores();
        Document mostSimilar = Main.corpus.get(0);

        // Finds the document with the highest similarity score
        for (Document document : Main.corpus) {
            if (document.similarityScore <= mostSimilar.similarityScore) { continue; }
            mostSimilar = document;
        }

        // Returns the document with the highest similarity score
        return mostSimilar;
    }
}
