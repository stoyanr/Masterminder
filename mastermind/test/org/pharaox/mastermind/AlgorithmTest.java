package org.pharaox.mastermind;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.pharaox.mastermind.Constants.*;
import static org.pharaox.mastermind.Score.ZERO_SCORE;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class AlgorithmTest
{
    private static final String M_INVALID_GUESS = "Invalid guess";
    private static final String M_WRONG_GUESS = "Wrong guess:";
    private static final String M_WRONG_SCORE = "Wrong score:";
    private static final String M_WRONG_ALGORITHM_NAME = "Wrong algorithm name:";
    
    private final transient Mastermind mastermind;
    private final transient AlgorithmFactory factory;
    private final transient String name;
    private final transient String firstGuess;
    private final transient String secondGuess;
    
    private transient Algorithm algorithm;

    public AlgorithmTest(final Mastermind mastermind, final AlgorithmFactory factory, 
        final String name, final String firstGuess, final String secondGuess)
    {
        this.mastermind = mastermind;
        this.factory = factory;
        this.name = name;
        this.firstGuess = firstGuess;
        this.secondGuess = secondGuess;
    }

    @Parameters
    public static Collection<Object[]> data()
    {
        // @formatter:off, @checkstyle:off
        final Object[][] data = new Object[][]
        {
            { MM2, new SimpleAlgorithmFactory(MM2), SimpleAlgorithm.NAME, MM2_FIRST_GUESS_SIMPLE, MM2_SECOND_GUESS_SIMPLE },
            { MM2, new KnuthAlgorithmFactory(MM2), KnuthAlgorithm.NAME, MM2_FIRST_GUESS_KNUTH, MM2_SECOND_GUESS_KNUTH },
            { MM2, new PharaoxAlgorithmFactory(MM2, 0.0), PharaoxAlgorithm.NAME, MM2_FIRST_GUESS_KNUTH, MM2_SECOND_GUESS_KNUTH },
            { MM2, new ExpectedSizeAlgorithmFactory(MM2), ExpectedSizeAlgorithm.NAME, MM2_FIRST_GUESS_ESIZE, MM2_SECOND_GUESS_ESIZE },
            { MM2, new DumbAlgorithmFactory(MM2), DumbAlgorithm.NAME, MM2_FIRST_GUESS_DUMB, MM2_SECOND_GUESS_DUMB }
        };
        // @formatter:on, @checkstyle:on
        return Arrays.asList(data);
    }

    @Before
    public final void setUp()
    {
        algorithm = factory.getAlgorithm();
    }

    @Test
    public final void testMakeGuess()
    {
        final String guess = algorithm.makeGuess();
        assertTrue(M_INVALID_GUESS, mastermind.isValidCode(guess));
        assertEquals(M_WRONG_GUESS, firstGuess, guess);
    }

    @Test
    public final void testMakeGuessRepeatedly()
    {
        final String guess1 = algorithm.makeGuess();
        final String guess2 = algorithm.makeGuess();
        assertEquals(M_WRONG_GUESS, guess1, guess2);
    }

    @Test
    public final void testPutGuessScore()
    {
        algorithm.putGuessScore(firstGuess, ZERO_SCORE);
        assertEquals(M_WRONG_SCORE, ZERO_SCORE, algorithm.getGuessScore(firstGuess));
    }

    @Test
    public final void testPutGuessScoreRepeatedly()
    {
        final Score score = mastermind.getWinningScore();
        algorithm.putGuessScore(firstGuess, ZERO_SCORE);
        algorithm.putGuessScore(firstGuess, score);
        assertEquals(M_WRONG_SCORE, score, algorithm.getGuessScore(firstGuess));
    }

    @Test
    public final void testSecondGuess()
    {
        algorithm.putGuessScore(firstGuess, ZERO_SCORE);
        final String guess = algorithm.makeGuess();
        assertEquals(M_WRONG_GUESS, secondGuess, guess);
    }

    @Test
    public final void testToString()
    {
        assertEquals(M_WRONG_ALGORITHM_NAME, name, algorithm.toString());
    }
}
