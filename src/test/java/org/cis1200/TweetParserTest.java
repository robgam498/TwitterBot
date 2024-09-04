package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/** Tests for TweetParser */
public class TweetParserTest {

    // A helper function to create a singleton list from a word
    private static List<String> singleton(String word) {
        List<String> l = new LinkedList<String>();
        l.add(word);
        return l;
    }

    // A helper function for creating lists of strings
    private static List<String> listOfArray(String[] words) {
        List<String> l = new LinkedList<String>();
        for (String s : words) {
            l.add(s);
        }
        return l;
    }

    // Cleaning and filtering tests -------------------------------------------
    @Test
    public void removeURLsTest() {
        assertEquals("abc . def.", TweetParser.removeURLs("abc http://www.cis.upenn.edu. def."));
        assertEquals("abc", TweetParser.removeURLs("abc"));
        assertEquals("abc ", TweetParser.removeURLs("abc http://www.cis.upenn.edu"));
        assertEquals("abc .", TweetParser.removeURLs("abc http://www.cis.upenn.edu."));
        assertEquals(" abc ", TweetParser.removeURLs("http:// abc http:ala34?#?"));
        assertEquals(" abc  def", TweetParser.removeURLs("http:// abc http:ala34?#? def"));
        assertEquals(" abc  def", TweetParser.removeURLs("https:// abc https``\":ala34?#? def"));
        assertEquals("abchttp", TweetParser.removeURLs("abchttp"));
    }

    @Test
    public void testCleanWord() {
        assertEquals("abc", TweetParser.cleanWord("abc"));
        assertEquals("abc", TweetParser.cleanWord("ABC"));
        assertNull(TweetParser.cleanWord("@abc"));
        assertEquals("ab'c", TweetParser.cleanWord("ab'c"));
    }

    /* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

    /* **** ****** ***** **** EXTRACT COLUMN TESTS **** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testExtractColumnGetsCorrectColumn() {
        assertEquals(
                " This is a tweet.",
                TweetParser.extractColumn(
                        "wrongColumn, wrong column, wrong column!, This is a tweet.", 3
                )
        );
    }

    @Test
    public void testNullString() {
        assertNull(null, TweetParser.extractColumn(null, 0));
    }

    @Test
    public void testSuccessfullyExtractsColumn() {
        assertEquals("computer", TweetParser.extractColumn("computer,information", 0));
        assertEquals("information", TweetParser.extractColumn("computer,information", 1));
        assertEquals("information", TweetParser.extractColumn("computer,information,science", 1));
    }

    @Test
    public void testAboveLimits() {
        assertNull(TweetParser.extractColumn("above limit", 200));
        assertNull(TweetParser.extractColumn("above, limit", 100));
    }

    @Test
    public void testBelowLimits() {
        assertNull(TweetParser.extractColumn("below limit", -1));
        assertNull(TweetParser.extractColumn("below, limit", -20));
    }

    @Test
    public void testBlankWordColumn() {
        assertEquals("", TweetParser.extractColumn("", 0));
        assertEquals("computer", TweetParser.extractColumn(",computer", 1));
    }

    /* **** ****** ***** ***** CSV DATA TO TWEETS ***** **** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTweetsSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 1);
        List<String> expected = new LinkedList<String>();
        expected.add(" The end should come here.");
        expected.add(" This comes from data with no duplicate words!");
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataEmptyLine() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 0);
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataSingleLine() {
        StringReader sr = new StringReader("CIS");
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 0);
        List<String> expected = new LinkedList<String>();
        expected.add("CIS");
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToUnrecognizedColumn() {
        StringReader sr = new StringReader("CIS");
        BufferedReader br = new BufferedReader(sr);
        List<String> tweets = TweetParser.csvDataToTweets(br, 100);
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, tweets);
    }

    /* **** ****** ***** ** PARSE AND CLEAN SENTENCE ** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void parseAndCleanSentenceNonEmptyFiltered() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanSentenceEmptySentence() {
        List<String> sentence = TweetParser.parseAndCleanSentence("");
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanCombinedWordSentence() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc#@#F");
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanFilthySentence() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc #@#F");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanCleanedSentence() {
        List<String> sentence = TweetParser.parseAndCleanSentence("abc def");
        List<String> expected = new LinkedList<String>();
        expected.add("abc");
        expected.add("def");
        assertEquals(expected, sentence);
    }

    @Test
    public void parseAndCleanMultiFilthySentence() {
        List<String> sentence = TweetParser.parseAndCleanSentence(",,, #@#F");
        List<String> expected = new LinkedList<String>();
        assertEquals(expected, sentence);
    }

    /* **** ****** ***** **** PARSE AND CLEAN TWEET *** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testParseAndCleanTweetRemovesURLS1() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc http://www.cis.upenn.edu");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanEmptyURL() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("");
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanCleanStringTweet() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc. def");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        expected.add(singleton("def"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanFilthyStringTweet() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc. #@#F");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        assertEquals(expected, sentences);
    }

    @Test
    public void testParseAndCleanTweetURLBetween() {
        List<List<String>> sentences = TweetParser
                .parseAndCleanTweet("abc. http://www.cis.upenn.edu. def.");
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(singleton("abc"));
        expected.add(singleton("def"));
        assertEquals(expected, sentences);
    }

    /* **** ****** ***** ** CSV DATA TO TRAINING DATA ** ***** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testCsvDataToTrainingDataSimpleCSV() {
        StringReader sr = new StringReader(
                "0, The end should come here.\n" +
                        "1, This comes from data with no duplicate words!"
        );
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 1);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("the end should come here".split(" ")));
        expected.add(listOfArray("this comes from data with no duplicate words".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataEmpty() {
        StringReader sr = new StringReader("");
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 0);
        List<List<String>> expected = new LinkedList<List<String>>();
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataWWithAnEmptyLine() {
        StringReader sr = new StringReader("CIS \n" + "\n 120");
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 0);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("cis".split(" ")));
        expected.add(listOfArray("120".split(" ")));
        assertEquals(expected, tweets);
    }

    @Test
    public void testCsvDataToTrainingDataRemovesCaps() {
        StringReader sr = new StringReader("CIS");
        BufferedReader br = new BufferedReader(sr);
        List<List<String>> tweets = TweetParser.csvDataToTrainingData(br, 0);
        List<List<String>> expected = new LinkedList<List<String>>();
        expected.add(listOfArray("cis".split(" ")));
        assertEquals(expected, tweets);
    }

}
