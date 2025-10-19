<<<<<<< HEAD
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpellChecker {

    private final Map<String, Integer> dictionary;  // word-frequency dictionary
    private final String alphabet;                  // valid characters for the language
    private final String wordRegex;                 // regex to detect words

    // Load serialized dictionary file
    @SuppressWarnings("unchecked")
    public SpellChecker(String dictionaryPath, String alphabet, String wordRegex) {
        this.alphabet = alphabet;
        this.wordRegex = wordRegex;
        this.dictionary = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(dictionaryPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.dictionary.putAll((Map<String, Integer>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Could not load dictionary: " + dictionaryPath, e);
        }
    }

    // Generate all words that are one edit away (delete, insert, replace, swap)
    private Set<String> edits1(String word) {
        Set<String> resultSet = new HashSet<>();
        List<Map.Entry<String, String>> splits = new ArrayList<>();

        for (int i = 0; i <= word.length(); i++) {
            splits.add(new AbstractMap.SimpleEntry<>(word.substring(0, i), word.substring(i)));
        }

        for (Map.Entry<String, String> split : splits) {
            String L = split.getKey();
            String R = split.getValue();

            if (!R.isEmpty()) resultSet.add(L + R.substring(1)); // delete
            if (R.length() > 1) resultSet.add(L + R.charAt(1) + R.charAt(0) + R.substring(2)); // swap
            if (!R.isEmpty()) for (char c : alphabet.toCharArray()) resultSet.add(L + c + R.substring(1)); // replace
            for (char c : alphabet.toCharArray()) resultSet.add(L + c + R); // insert
        }
        return resultSet;
    }

    // Generate words two edits away
    private Set<String> edits2(String word) {
        return edits1(word).stream().flatMap(e1 -> edits1(e1).stream()).collect(Collectors.toSet());
    }

    // Keep only valid words from dictionary
    private Set<String> known(Set<String> candidates) {
        return candidates.stream().filter(dictionary::containsKey).collect(Collectors.toSet());
    }

    // Return best correction for a word
    public String correct(String word) {
        if (dictionary.containsKey(word)) return word;

        Set<String> oneEdit = known(edits1(word));
        if (!oneEdit.isEmpty()) return Collections.max(oneEdit, Comparator.comparingInt(dictionary::get));

        Set<String> twoEdit = known(edits2(word));
        if (!twoEdit.isEmpty()) return Collections.max(twoEdit, Comparator.comparingInt(dictionary::get));

        return word; // no correction found
    }

    // Correct all words in a sentence or paragraph
    public String correctText(String text) {
        StringBuffer correctedText = new StringBuffer();
        Pattern pattern = Pattern.compile(this.wordRegex);
        Matcher matcher = pattern.matcher(text);

        int lastEnd = 0;
        while (matcher.find()) {
            correctedText.append(text, lastEnd, matcher.start());
            String word = matcher.group();
            String corrected = correct(word.toLowerCase());

            // Preserve original capitalization
            if (Character.isUpperCase(word.charAt(0))) {
                corrected = Character.toUpperCase(corrected.charAt(0)) + corrected.substring(1);
            }

            correctedText.append(corrected);
            lastEnd = matcher.end();
        }

        correctedText.append(text.substring(lastEnd));
        return correctedText.toString();
    }
}
=======
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpellChecker {

    private final Map<String, Integer> dictionary;  // word-frequency dictionary
    private final String alphabet;                  // valid characters for the language
    private final String wordRegex;                 // regex to detect words

    // Load serialized dictionary file
    @SuppressWarnings("unchecked")
    public SpellChecker(String dictionaryPath, String alphabet, String wordRegex) {
        this.alphabet = alphabet;
        this.wordRegex = wordRegex;
        this.dictionary = new HashMap<>();

        try (FileInputStream fis = new FileInputStream(dictionaryPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.dictionary.putAll((Map<String, Integer>) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Could not load dictionary: " + dictionaryPath, e);
        }
    }

    // Generate all words that are one edit away (delete, insert, replace, swap)
    private Set<String> edits1(String word) {
        Set<String> resultSet = new HashSet<>();
        List<Map.Entry<String, String>> splits = new ArrayList<>();

        for (int i = 0; i <= word.length(); i++) {
            splits.add(new AbstractMap.SimpleEntry<>(word.substring(0, i), word.substring(i)));
        }

        for (Map.Entry<String, String> split : splits) {
            String L = split.getKey();
            String R = split.getValue();

            if (!R.isEmpty()) resultSet.add(L + R.substring(1)); // delete
            if (R.length() > 1) resultSet.add(L + R.charAt(1) + R.charAt(0) + R.substring(2)); // swap
            if (!R.isEmpty()) for (char c : alphabet.toCharArray()) resultSet.add(L + c + R.substring(1)); // replace
            for (char c : alphabet.toCharArray()) resultSet.add(L + c + R); // insert
        }
        return resultSet;
    }

    // Generate words two edits away
    private Set<String> edits2(String word) {
        return edits1(word).stream().flatMap(e1 -> edits1(e1).stream()).collect(Collectors.toSet());
    }

    // Keep only valid words from dictionary
    private Set<String> known(Set<String> candidates) {
        return candidates.stream().filter(dictionary::containsKey).collect(Collectors.toSet());
    }

    // Return best correction for a word
    public String correct(String word) {
        if (dictionary.containsKey(word)) return word;

        Set<String> oneEdit = known(edits1(word));
        if (!oneEdit.isEmpty()) return Collections.max(oneEdit, Comparator.comparingInt(dictionary::get));

        Set<String> twoEdit = known(edits2(word));
        if (!twoEdit.isEmpty()) return Collections.max(twoEdit, Comparator.comparingInt(dictionary::get));

        return word; // no correction found
    }

    // Correct all words in a sentence or paragraph
    public String correctText(String text) {
        StringBuffer correctedText = new StringBuffer();
        Pattern pattern = Pattern.compile(this.wordRegex);
        Matcher matcher = pattern.matcher(text);

        int lastEnd = 0;
        while (matcher.find()) {
            correctedText.append(text, lastEnd, matcher.start());
            String word = matcher.group();
            String corrected = correct(word.toLowerCase());

            // Preserve original capitalization
            if (Character.isUpperCase(word.charAt(0))) {
                corrected = Character.toUpperCase(corrected.charAt(0)) + corrected.substring(1);
            }

            correctedText.append(corrected);
            lastEnd = matcher.end();
        }

        correctedText.append(text.substring(lastEnd));
        return correctedText.toString();
    }
}
>>>>>>> af0c969afa6fd8f0ee785b988dd0d3dce68e5712
