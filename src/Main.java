import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    // Language-specific alphabets and regex patterns
    private static final String ENGLISH_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final String TELUGU_ALPHABET = "అఆఇఈఉఊఋౠఎఏఐఒఓఔకఖగఘఙచఛజఝఞటఠడఢణతథదధనపఫబభమయరలవశషసహళక్షఱంఃాిీుూృౄెేైొోౌ్ఁ";
    private static final String ENGLISH_WORD_REGEX = "[a-zA-Z]+";
    private static final String TELUGU_WORD_REGEX = "[\u0C00-\u0C7F]+";

    public static void main(String[] args) {
        // Validate command-line argument
        if (args.length != 1) {
            System.err.println("Usage: java Main <language>");
            System.err.println("Supported languages: 'english', 'telugu'");
            return;
        }

        String language = args[0].toLowerCase();
        String dictionaryPath, alphabet, wordRegex;
        Path inputPath, outputPath;

        // Choose settings based on selected language
        switch (language) {
            case "english":
                dictionaryPath = "../dictionary.ser";
                alphabet = ENGLISH_ALPHABET;
                wordRegex = ENGLISH_WORD_REGEX;
                inputPath = Paths.get("../input.txt");
                outputPath = Paths.get("../output.txt");
                break;
            case "telugu":
                dictionaryPath = "../dictionary-telugu.ser";
                alphabet = TELUGU_ALPHABET;
                wordRegex = TELUGU_WORD_REGEX;
                inputPath = Paths.get("../input-telugu.txt");
                outputPath = Paths.get("../output-telugu.txt");
                break;
            default:
                System.err.println("Unsupported language: " + language);
                return;
        }

        try {
            // Initialize spell checker
            SpellChecker spellChecker = new SpellChecker(dictionaryPath, alphabet, wordRegex);
            System.out.println("Spell Checker for " + language + " initialized successfully.");

            // Read input text
            System.out.println("Reading text from " + inputPath.getFileName() + "...");
            String originalText = Files.readString(inputPath);

            // Run spell correction
            String correctedText = spellChecker.correctText(originalText);

            // Print results to console
            System.out.println("\n--- Spell Check Results ---");
            System.out.println("Original:  " + originalText);
            System.out.println("Corrected: " + correctedText);
            System.out.println("---------------------------\n");

            // Save corrected text to output file
            Files.writeString(outputPath, correctedText);
            System.out.println("Correction complete. Output saved to " + outputPath.getFileName() + " 👍");

        } catch (IOException e) {
            System.err.println("File I/O error: " + e.getMessage());
            System.err.printf("Please ensure '%s' exists in the project root directory.\n", inputPath.getFileName());
        } catch (RuntimeException e) {
            System.err.println("Startup error: " + e.getMessage());
            System.err.println("Did you build the dictionary for '" + language + "' first?");
            System.err.println("Try: java DictionaryBuilder " + language);
        }
    }
}
