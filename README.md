# Spell Checker in Java (Assignment 1)

## Project Overview

This project is a Java implementation of a spell checker based on the principles described by Peter Norvig. The system is designed to identify misspelled words in a given text and suggest corrections. The core logic relies on a statistical model built from a large text corpus.

The spell checker performs the following key functions:
1.  **Builds a Language Model**: It processes a large text corpus (`data/corpus.txt`) to create a frequency map of all words, which serves as our dictionary. This dictionary is serialized and stored on secondary memory for efficient reuse.
2.  **Identifies Misspelled Words**: It checks each word from the input text against the dictionary.
3.  **Generates Correction Candidates**: For a misspelled word, it generates a set of possible correct words by applying four types of edits: **insertion**, **deletion**, **substitution**, and **transposition**. It first looks for candidates at an edit distance of 1, and if none are found, it searches at an edit distance of 2.
4.  **Ranks Candidates**: The candidates are ranked based on their frequency in the original corpus. This is a probabilistic approach where we choose the most likely intended word. The underlying principle is Bayes' Theorem, where we try to find the word `c` that maximizes the probability `P(c|w)`, where `w` is the misspelled word. This is simplified to maximizing `P(c)`, the frequency of the candidate word in our language model.

**Note on Semantic Ranking**: The current implementation ranks candidates based on their probability of occurrence (`P(c)`), not their semantic context. True semantic ranking would require a much more complex model using techniques like word embeddings (e.g., Word2Vec, GloVe) to analyze the surrounding words and find a candidate that is not only orthographically close but also makes sense in the sentence. This implementation provides a foundational, highly effective probabilistic approach.

## Modules Description

The source code is organized into three main Java classes:

1.  `DictionaryBuilder.java`:
    * **Purpose**: This is a standalone utility program to build the spell checker's dictionary.
    * **Functionality**: It reads a large text file (`data/corpus.txt`), tokenizes it into words, converts them to lowercase, and counts the frequency of each word. The resulting `Map<String, Integer>` is then serialized to a file named `dictionary.ser` on the hard drive (secondary memory). This process only needs to be run once.

2.  `SpellChecker.java`:
    * **Purpose**: This is the core engine of the spell checker.
    * **Functionality**:
        * It loads the serialized dictionary (`dictionary.ser`) from secondary memory into its main memory upon instantiation.
        * The main method is `correct(String word)`, which returns the most probable correction for the given word.
        * It contains private methods to generate candidate words at an edit distance of 1 (`edits1`) and an edit distance of 2 (`edits2`).
        * It manages the logic of checking the word, generating candidates, and selecting the best one based on frequency.

3.  `Main.java`:
    * **Purpose**: A simple driver class to demonstrate the functionality of the `SpellChecker`.
    * **Functionality**: It initializes the `SpellChecker` object, takes a sample text with misspellings, splits it into words, and uses the `SpellChecker` to correct each misspelled word. It then prints the original and corrected text.

## How to Run the Code

### Prerequisites
* Java Development Kit (JDK) 1.8 or higher.

### Steps to Execute

1.  **Place the Corpus File**: Create a folder named `data` in the project's root directory. Download a large text corpus, such as Peter Norvig's text file from [here](https://norvig.com/big.txt), and save or rename it as `corpus.txt` inside the `data` folder.

2.  **Compile the Code**: Open a terminal, navigate into the `src` directory, and compile all Java files.
    ```sh
    cd src
    javac *.java
    ```

3.  **Step 1: Build the Dictionary**: While still in the `src` directory, run the `DictionaryBuilder`. This will read `../data/corpus.txt` and create the `dictionary.ser` file in the parent directory.
    ```sh
    java DictionaryBuilder
    ```
    *This might take a minute depending on the size of your corpus and the speed of your machine.*

4.  **Step 2: Run the Spell Checker**: Once the dictionary is built, run the `Main` program.
    ```sh
    java Main
    ```

## Data and Index Link

* The corpus used is `corpus.txt` and is expected to be in the `data` folder.
* The spell checker index (`dictionary.ser`) is generated locally by the `DictionaryBuilder` program and is not uploaded.
