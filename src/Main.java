import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static ArrayList<Document> corpus = new ArrayList<>(); // The corpus of documents
    public static HashTable glossary = new HashTable(); // The glossary of all words in the corpus

    public static void main(String[] args) throws IOException {
        // Gets the file with the corpus links
        File file = new File("links.txt");
        Scanner scanner = new Scanner(file);

        // Parses the links into documents and adds them to the corpus
        while (scanner.hasNextLine()) {
            String link = scanner.nextLine();
            corpus.add(new Document(link));
        }

        // Calculates the idf values for each word in the glossary
        glossary.calculateIDF();

        // Shows the user the GUI
        GUI.initialize();
        scanner.close();
    }
}
