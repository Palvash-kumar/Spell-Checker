import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DictionaryBuilder {

    public static void main(String[] args) {
        // Check if language argument is provided
        if (args.length != 1) {
            System.err.println("Usage: java DictionaryBuilder <language>");
            System.err.println("Supported languages: 'english', 'telugu'");
            return;
        }

        String language = args[0].toLowerCase();
        Path corpusPath;
        String dictionaryPath;
        String wordRegex;

        // Select paths and regex based on language
        switch (language) {
            case "english":
                corpusPath = Paths.get("../data/corpus.txt");
                dictionaryPath = "../dictionary.ser";
                wordRegex = "[a-z]+";
                break;
            case "telugu":
                corpusPath = Paths.get("../data/telugu-ds.xml");
                dictionaryPath = "../dictionary-telugu.ser";
                wordRegex = "[\u0C00-\u0C7F]+"; // Telugu Unicode range
                break;
            default:
                System.err.println("Unsupported language: " + language);
                return;
        }

        System.out.println("Starting dictionary build for " + language + "...");
        System.out.println("Reading from: " + corpusPath);
        System.out.println("This may take a few minutes for large files...");

        Map<String, Integer> dictionary = new HashMap<>();
        Pattern pattern = Pattern.compile(wordRegex);

        // Read corpus line-by-line (efficient for large files)
        try (Stream<String> lines = Files.lines(corpusPath)) {

            if (language.equals("english")) {
                // Process plain text corpus
                lines.forEach(line -> {
                    Matcher matcher = pattern.matcher(line.toLowerCase());
                    while (matcher.find()) {
                        String word = matcher.group();
                        dictionary.put(word, dictionary.getOrDefault(word, 0) + 1);
                    }
                });

            } else if (language.equals("telugu")) {
                // Process Telugu TSV/XML corpus
                lines.forEach(line -> {
                    String[] parts = line.split("\t");
                    if (parts.length > 1) {
                        String title = parts[1].replace("_", " ").replace("\"", "");
                        Matcher matcher = pattern.matcher(title.toLowerCase());
                        while (matcher.find()) {
                            String word = matcher.group();
                            dictionary.put(word, dictionary.getOrDefault(word, 0) + 1);
                        }
                    }
                });
            }

            System.out.println("Corpus processed. Unique words found: " + dictionary.size());

            // Save dictionary to .ser file
            try (FileOutputStream fos = new FileOutputStream(dictionaryPath);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(dictionary);
            }

            System.out.println("Dictionary saved successfully to " + dictionaryPath + " 👍");

        } catch (IOException e) {
            System.err.println("Error building dictionary: " + e.getMessage());
            System.err.println("Make sure the corpus file exists at: " + corpusPath);
        }
    }
}
