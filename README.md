# Dual-Language (English/Telugu) Spell Checker in Java

##  Project Overview

This project is a **high-performance, memory-efficient spell checker** implemented in **Java**, designed to support multiple languages.  
It is based on the principles described by **Peter Norvig** and currently provides full support for both **English** and **Telugu**.

The system identifies misspelled words in a given text and suggests corrections using a **statistical language model** built from large, language-specific corpus files.

A key feature of this project is the **memory-efficient dictionary builder**, which processes massive corpus files (like Telugu XML datasets) line-by-line to avoid `OutOfMemoryError`, enabling dictionary building even on standard consumer machines.

---

##  Core Functions

1. **Builds Language Models**  
   Processes corpus files (`data/corpus.txt` for English, `data/telugu-ds.xml` for Telugu) to create a frequency map (dictionary).  
   This map is serialized for later reuse.

2. **Identifies Misspelled Words**  
   Checks each word in the input text against the dictionary.

3. **Generates Correction Candidates**  
   For each misspelled word, generates candidates using four edit operations:  
   **insertion**, **deletion**, **substitution**, and **transposition**.  
   If no valid word is found at edit distance 1, it searches at edit distance 2.

4. **Ranks Candidates**  
   Candidates are ranked by their frequency in the corpus using a probabilistic model based on **Bayes’ Theorem** — maximizing the probability of the intended word \( P(c|w) \), simplified to \( P(c) \).

> **Note:**  
> The current version uses unigram frequency ranking (based on word probability).  
> Semantic ranking (using context models like n-grams, Word2Vec, or GloVe) is not implemented but can be added in future versions.

---

##  Modules Description

| File | Description |
|------|--------------|
| **DictionaryBuilder.java** | Builds the dictionary for a specific language by reading the corresponding corpus (supports `.txt` and `.xml`). Serializes the frequency map for reuse. |
| **SpellChecker.java** | Core spell-checking engine (language-agnostic). Implements edit-distance logic and ranking of correction candidates. |
| **Main.java** | Driver program that loads the correct dictionary, processes the input text, and outputs the corrected result. |

---

## ⚙️ How to Run the Code

All commands must be run from the `src/` directory.

###  Prerequisites

- Java Development Kit (JDK) **1.8 or higher**
- Project directory structure:

```
YourProjectFolder/
├── data/
│   ├── corpus.txt          (English corpus)
│   └── telugu-ds.xml       (Telugu corpus)
├── input.txt               (Input for English)
├── input-telugu.txt        (Input for Telugu)
└── src/
    ├── DictionaryBuilder.java
    ├── SpellChecker.java
    └── Main.java
```

---

### Step 1: Compile the Code

Open a terminal inside the `src` folder and run:

```bash
cd src
javac *.java
```

This compiles all Java source files.

---

###  Step 2: Build the Dictionaries

Run this step **once for each language**.  
It creates serialized dictionary files (`.ser`) in the main project directory.

#### For English  
Reads `data/corpus.txt` and creates `dictionary.ser`.

```bash
java DictionaryBuilder english
```

#### For Telugu  
Reads `data/telugu-ds.xml` and creates `dictionary-telugu.ser`.

```bash
java DictionaryBuilder telugu
```

---

###  Step 3: Run the Spell Checker

After building the dictionaries, you can run the main spell-checking program.

#### For English  
Reads `input.txt`, prints corrections to the console, and writes the output to `output.txt`.

```bash
java Main english
```

#### For Telugu  
Reads `input-telugu.txt`, prints corrections to the console, and writes the output to `output-telugu.txt`.

```bash
java Main telugu
```

---

## 🧾 Example Run

**Input (`input.txt`):**
```
Ths is an exmple of a spel cheker
```

**Output (`output.txt`):**
```
This is an example of a spell checker
```

---

## 📂 Example Telugu Input/Output

**Input (`input-telugu.txt`):**
```
ఇది ఒక స్పెల చెకర్ ఉదాహరన
```

**Output (`output-telugu.txt`):**
```
ఇది ఒక స్పెల్ చెకర్ ఉదాహరణ
```

---


## ⚠️ Troubleshooting

###  OutOfMemoryError
If you encounter this while building the Telugu dictionary, increase your Java heap size:
```bash
java -Xmx2G DictionaryBuilder telugu
```

###  Encoding Issues
Ensure all corpus files (`corpus.txt`, `telugu-ds.xml`) are **UTF-8 encoded** to handle special Telugu characters.

###  Java Version
Use **Java 8 or higher** for full Unicode and XML parsing compatibility.

---

##  Dataset and Sources

| Language | Dataset | Source |
|-----------|----------|--------|
| **English** | `data/corpus.txt` | Compiled from open English text datasets and corpus |
| **Telugu** | `data/telugu-ds.xml` | Compiled from open telugu text datasets and Wikipedia dumps and open-source language datasets |

---


##  Summary

This project demonstrates a **dual-language spell checker** using a **probabilistic model** based on Peter Norvig’s algorithm.  
It efficiently handles large corpus data and supports **both English and Telugu** with minimal memory usage.

---
