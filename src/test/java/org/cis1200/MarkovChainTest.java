package org.cis1200;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;

/** Tests for MarkovChain */
public class MarkovChainTest {

    /*
     * Writing tests for Markov Chain can be a little tricky.
     * We provide a few tests below to help you out, but you still need
     * to write your own.
     */

    /* **** ****** **** **** ADD BIGRAMS TESTS **** **** ****** **** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testAddBigram() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", "2");
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey("2"));
        assertEquals(1, pd.count("2"));
    }

    @Test
    public void testAddBigramContainingEnd() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("1", MarkovChain.END_TOKEN);
        assertTrue(mc.chain.containsKey("1"));
        ProbabilityDistribution<String> pd = mc.chain.get("1");
        assertTrue(pd.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testAddBigramContaingDiffWords() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("cis", "120");
        mc.addBigram("cis", "160");
        assertFalse(mc.chain.containsKey("word"));
        ProbabilityDistribution<String> pd = mc.chain.get("cis");
        assertTrue(pd.getRecords().containsKey("120"));
        assertTrue(pd.getRecords().containsKey("160"));
        assertEquals(1, pd.count("120"), 1);
        assertEquals(1, pd.count("160"), 1);
    }

    @Test
    public void testMultipleBigramsWithRepeatingValues() {
        MarkovChain mc = new MarkovChain();
        mc.addBigram("cis", "120");
        mc.addBigram("120", MarkovChain.END_TOKEN);
        assertTrue(mc.chain.containsKey("cis"));
        assertTrue(mc.chain.containsKey("120"));
        ProbabilityDistribution<String> pd1 = mc.chain.get("cis");
        assertTrue(pd1.getRecords().containsKey("120"));
        assertEquals(1, pd1.count("120"));
        ProbabilityDistribution<String> pd2 = mc.chain.get("120");
        assertTrue(pd2.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd2.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testAddBigramsWithNull() {
        MarkovChain mc1 = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc1.addBigram(null, "cis"));
        MarkovChain mc2 = new MarkovChain();
        assertThrows(IllegalArgumentException.class, () -> mc2.addBigram(null, "120"));
    }

    /* ***** ****** ***** ***** TRAIN TESTS ***** ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testTrain() {
        MarkovChain mc = new MarkovChain();
        String sentence = "1 2 3";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(3, mc.chain.size());
        ProbabilityDistribution<String> pd1 = mc.chain.get("1");
        assertTrue(pd1.getRecords().containsKey("2"));
        assertEquals(1, pd1.count("2"));
        ProbabilityDistribution<String> pd2 = mc.chain.get("2");
        assertTrue(pd2.getRecords().containsKey("3"));
        assertEquals(1, pd2.count("3"));
        ProbabilityDistribution<String> pd3 = mc.chain.get("3");
        assertTrue(pd3.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd3.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testTrainNull() {
        MarkovChain mc = new MarkovChain();
        String sentence = "";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(1, mc.chain.size());
        ProbabilityDistribution<String> pd1 = mc.chain.get("");
        assertTrue(pd1.getRecords().containsKey(MarkovChain.END_TOKEN));
        assertEquals(1, pd1.count(MarkovChain.END_TOKEN));
    }

    @Test
    public void testTrainBlank() {
        MarkovChain mc = new MarkovChain();
        String sentence = "   ";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        mc.reset();
        assertEquals(0, mc.chain.size());
        assertFalse(mc.hasNext());
    }

    @Test
    public void testTrainStartWordsFunction() {
        MarkovChain mc = new MarkovChain();
        String sentence = "i love cis 120";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());
        assertEquals(mc.startWords.count("i"), 1);
        assertEquals(mc.startWords.count("love"), 0);
        assertEquals(mc.startWords.count("cis"), 0);
        assertEquals(mc.startWords.count("120"), 0);
    }

    @Test
    public void testTrainTrainingFunction() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "i love";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        assertEquals(mc.startWords.count("i"), 1);
        assertEquals(mc.startWords.count("love"), 0);
        String sentence2 = "cis 120";
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());
        assertEquals(mc.startWords.count("cis"), 1);
        assertEquals(mc.startWords.count("120"), 0);
    }

    @Test
    public void testTrainRepeatingValues() {
        MarkovChain mc = new MarkovChain();
        String sentence1 = "i love";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        assertEquals(mc.startWords.count("i"), 1);
        assertEquals(mc.startWords.count("love"), 0);
        String sentence2 = "120 cis 120";
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());
        assertEquals(mc.startWords.count("120"), 1);
        assertEquals(mc.startWords.count("cis"), 0);
        assertEquals(mc.startWords.count("120"), 1);
    }

    /* **** ****** ****** MARKOV CHAIN CLASS TESTS ***** ****** ***** */

    /* Here's an example test case. Be sure to add your own as well */
    @Test
    public void testWalk() {
        String[] expectedWords = { "CIS", "120", "beats", "CIS", "120", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 120 rocks";
        String sentence2 = "CIS 120 beats CIS 160";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        mc.reset("CIS"); // we start with "CIS" since that's the word our desired walk starts with
        mc.fixDistribution(new ArrayList<>(Arrays.asList(expectedWords)));

        for (int i = 0; i < expectedWords.length; i++) {
            assertTrue(mc.hasNext());
            assertEquals(expectedWords[i], mc.next());
        }
    }

    @Test
    public void testNullWordStart() {
        String[] expectedWords = { "" };
        MarkovChain mc = new MarkovChain();

        String sentence = "";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());

        mc.fixDistribution(new ArrayList<>(Arrays.asList(expectedWords)));
        assertThrows(IllegalArgumentException.class, () -> mc.reset(null));
    }

    @Test
    public void testNoNextInWalk() {
        String[] expectedWords = { "CIS", "120", "rocks" };
        MarkovChain mc = new MarkovChain();

        String sentence = "CIS 120 rocks";
        mc.train(Arrays.stream(sentence.split(" ")).iterator());

        mc.reset("CIS");
        mc.fixDistribution((new ArrayList<>(Arrays.asList(expectedWords))));

        for (int i = 0; i < expectedWords.length; i++) {
            assertTrue(mc.hasNext());
            mc.next();
        }
        assertFalse(mc.hasNext());
        assertThrows(NoSuchElementException.class, () -> mc.next());
    }

    @Test
    public void testResetForWalk() {
        String[] expectedWords = { "CIS", "120", "rocks", "CIS", "120", "beats", "CIS", "160" };
        MarkovChain mc = new MarkovChain();

        String sentence1 = "CIS 120 rocks";
        String sentence2 = "CIS 120 beats CIS 160";
        mc.train(Arrays.stream(sentence1.split(" ")).iterator());
        mc.train(Arrays.stream(sentence2.split(" ")).iterator());

        try {
            mc.reset("CIS");
            assertTrue(mc.startWords.getRecords().containsKey("CIS"));
            assertEquals(2, mc.startWords.count("CIS"));
        } catch (IllegalArgumentException e) {
            fail("Illegal Argument Exception");
        }
    }

}
