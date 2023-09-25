import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class GUI {
    public JPanel panel1; // The main panel
    private JTextField textField1; // The input text field
    private JTextField textField2; // The output text field
    private JButton searchButton; // The search button
    private JButton copyButton; // The copy button

    public GUI() {
        // Listens for when the search button is clicked
        searchButton.addActionListener(e -> {
            // Gets the link from the text field
            String link = textField1.getText();

            // Generates a document from the link and gets the most similar document
            Document document = new Document(link);
            Document similarDocument = document.getSimilarDocument();

            // Sets the text field to the most similar document's link
            textField2.setText(similarDocument.link);
        });

        // Listens for when the copy button is clicked
        copyButton.addActionListener(e -> {
            // Copies the link from the text field to the clipboard
            StringSelection stringSelection = new StringSelection(textField2.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        // Listens for when the text field is clicked
        textField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                // Selects all the text in the input text field
                textField1.selectAll();
                // Clears the output text field
                textField2.setText("");
            }
        });
    }

    /**
     * Initializes the GUI
     */
    public static void initialize() {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
