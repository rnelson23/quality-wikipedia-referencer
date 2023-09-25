public class HashTable {
    public static class Node {
        public String word; // The word of the node
        public Node next; // The next node in the linked list
        public int count; // The number of times the word appears in the document
        public double tf; // The term frequency of the word
        public double idf; // The inverse document frequency of the word

        Node(String word, Node next) {
            this.word = word;
            this.next = next;
            this.count = 1;
        }
    }

    public Node[] table = new Node[8]; // The table of words
    private int wordCount = 0; // The total number of words in the document
    private int tableSize = 0; // The number of unique words in the document

    /**
     * Calculates the tf value for each word in the table
     */
    public void calculateTF() {
        // Loops through each word in the table
        for (Node head : table) {
            for (Node node = head; node != null; node = node.next) {
                // Calculates the tf value for the word
                node.tf = (double) node.count / (double) wordCount;
            }
        }
    }

    /**
     * Calculates the idf value for each word in the table
     * Note: only the glossary hash table should call this method
     */
    public void calculateIDF() {
        // Loops through each word in the glossary
        for (Node head : table) {
            for (Node node = head; node != null; node = node.next) {
                int documentCount = 0;

                // Counts the number of documents that contain the word
                for (Document document : Main.corpus) {
                    if (document.frequencyTable.get(node.word) == null) { continue; }
                    documentCount++;
                }

                // Calculates the idf value for the word
                node.idf = Math.log((double) Main.corpus.size() / (double) documentCount);
            }
        }
    }

    /**
     * Calculates the similarity score for each document in the corpus
     * Note: only the input document should call this method
     */
    public void calculateSimilarityScores() {
        // Loops through each word in the table
        for (Node head : table) {
            for (Node node = head; node != null; node = node.next) {
                // Checks each document to see if it contains the word
                for (Document document : Main.corpus) {
                    if (document.frequencyTable.get(node.word) == null) { continue; }

                    // If the document contains the word, get the tf value and idf value of the word
                    double tf = document.frequencyTable.get(node.word).tf;
                    double idf = Main.glossary.get(node.word).idf;

                    // Adds the tf-idf value to the similarity score of the document
                    document.similarityScore += tf * idf;
                }
            }
        }
    }

    /**
     * Gets the node with the specified word
     * @param word the word to search for
     * @return the node with the specified word
     */
    public Node get(String word) {
        // Gets the hash code for the word and finds the index in the table
        int hash = word.hashCode();
        int i = hash & (table.length - 1);

        // Checks if the linked list at the index contains the word
        for (Node node = table[i]; node != null; node = node.next) {
            if (!word.equals(node.word)) { continue; }

            // Returns the node if the word is found
            return node;
        }

        // Returns null if the word is not found
        return null;
    }

    /**
     * Adds a word to the table
     * @param word the word to add
     */
    public void add(String word) {
        // Gets the hash code for the word and finds the index in the table
        int hash = word.hashCode();
        int i = hash & (table.length - 1);

        // Increases the total word count
        wordCount++;

        // Checks if the linked list at the index contains the word
        for (Node node = table[i]; node != null; node = node.next) {
            if (!word.equals(node.word)) { continue; }

            // Increases the word count if the word is found
            node.count++;
            return;
        }

        // Adds the word to the table if it is not found
        table[i] = new Node(word, table[i]);
        tableSize++;

        // Resizes the table if the load factor is greater than 0.75
        if ((float) tableSize / table.length >= 0.75f) {
            resize();
        }
    }

    /**
     * Resizes the table
     */
    private void resize() {
        // Temporarily stores the old table
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;

        // Creates a new table with increased capacity
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];

        // Loops through each word in the old table
        for (Node head : oldTable) {
            for (Node node = head; node != null; node = node.next) {
                // Gets the hash code for the word and finds the index in the new table
                int hash = node.word.hashCode();
                int i = hash & (newTable.length - 1);

                // Adds the word to the new table
                newTable[i] = new Node(node.word, newTable[i]);
            }
        }

        // Sets the table to the new table
        table = newTable;
    }
}
