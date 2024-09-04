# TwitterBot - CIS 1200 Homework

**Course**: CIS 1200 - Programming Languages and Techniques II  
**Project**: Building a Markov Chain-Based AI for Tweet Generation

---

## Project Overview

The **TwitterBot** project is a Java-based application designed to simulate a simple machine learning model using Markov Chains. The goal is to generate realistic tweets by analyzing and processing a collection of real-world tweets. This project combines data cleaning, probability distribution, and Markov Chain concepts to create an AI capable of generating new tweets based on training data.

This assignment taught me key concepts in machine learning, text processing, and working with structured data in CSV files.

---

## Key Concepts and What I Learned

1. **Machine Learning Basics with Markov Chains**:  
   I gained hands-on experience in building a basic machine learning model using a Markov Chain to generate text based on patterns in the training data. This introduced me to key ideas in AI, such as training data, probability distributions, and predictive modeling.

2. **Data Cleaning and Regular Expressions**:  
   Working with real-world Twitter data, I learned the importance of **data cleaning** in machine learning. By removing URLs, symbols, and other extraneous data using regular expressions, I was able to improve the performance of the TwitterBot and ensure higher-quality training data.

3. **File I/O and CSV Parsing**:  
   The project required reading large datasets from CSV files. I developed a utility (`FileLineIterator.java`) to efficiently read and process files line by line, extracting tweets and preparing them for analysis.

4. **Markov Chains for Predictive Text Generation**:  
   Using Markov Chains, I implemented an algorithm to track word pairs (bigrams) and their frequency of occurrence in the dataset. This allowed me to predict the next word in a tweet, creating coherent, albeit simple, sentences based on the training data.

5. **Testing and Iterative Development**:  
   I followed test-driven development (TDD) principles, creating test cases for each component before implementation. This helped ensure robustness and allowed for quicker debugging and improvements throughout the project.

---

## Project Structure

- **FileLineIterator.java**: This class handles the file reading, allowing the program to iterate over CSV files line-by-line.
- **TweetParser.java**: Responsible for extracting, cleaning, and preparing the tweet data from CSV files for training the model.
- **MarkovChain.java**: Implements the Markov Chain model, which trains on word pairs from tweets and generates new sentences by predicting the next word.
- **TwitterBot.java**: Integrates all components, using the Markov Chain to generate tweets and saving the results to a file.
- **Test Suite**: Includes unit tests for each component (`FileLineIteratorTest`, `TweetParserTest`, `MarkovChainTest`, `TwitterBotTest`), ensuring correctness and reliability.

---

## Major Features

- **Data Cleaning and Preparation**:  
  Using regular expressions, the `TweetParser` removes undesirable elements such as URLs and symbols, ensuring the training data is clean and ready for the Markov Chain.

- **Markov Chain Implementation**:  
  The core of the project is the `MarkovChain` class, which tracks word pairs and uses probability distributions to generate new tweets. It builds a dictionary of words and their possible successors, simulating natural language.

- **Realistic Tweet Generation**:  
  By training the model on a set of real-world tweets, the bot generates sentences that mimic the structure and content of the input data.

- **File Output**:  
  The generated tweets can be saved to a file, allowing easy integration with other systems or further analysis.

---

## Technical Challenges Overcome

1. **Implementing Markov Chains for Text Generation**:  
   One of the biggest challenges was understanding and implementing a Markov Chain to predict the next word in a sequence. This involved training the model on bigrams (pairs of adjacent words) and generating coherent sentences based on word probabilities.

2. **Efficient File I/O with Large Datasets**:  
   Parsing large CSV files efficiently was critical to the success of the project. I developed a `FileLineIterator` class that iterates over the file line by line, ensuring scalability and performance even with large datasets.

3. **Data Cleaning with Regular Expressions**:  
   Cleaning the raw tweet data to improve the model's accuracy required extensive use of regular expressions. Removing URLs, special characters, and irrelevant data ensured higher-quality input for the Markov Chain.

---

## What I Learned

This project reinforced my understanding of machine learning fundamentals, particularly how Markov Chains can be used for predictive text generation. Additionally, I deepened my experience with data cleaning techniques, file processing, and the importance of testing in software development. The project helped me understand how real-world data can be processed, cleaned, and modeled to create intelligent applications.

---

## Running the Project

### Input Data
The TwitterBot reads from CSV files containing real-world tweets. To customize the bot’s behavior, simply change the input file or column indices to experiment with different datasets.

### Running the TwitterBot
1. Modify the `pathToTweets` in `TwitterBot.java` to point to your desired CSV file.
2. Run the `TwitterBot` class in your IDE.
3. Generated tweets will be displayed in the console and can be saved to a file for future use.

---

## Conclusion

The **TwitterBot** project was an exciting introduction to building a machine learning model using Markov Chains. I developed critical skills in data processing, probability modeling, and machine learning implementation. This project helped me appreciate the complexity of real-world data and the importance of robust data cleaning and model training for effective AI development.

