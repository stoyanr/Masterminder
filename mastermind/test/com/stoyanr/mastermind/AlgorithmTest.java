/*
 * $Id: $
 *
 * Copyright 2012 Stoyan Rachev (stoyanr@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stoyanr.mastermind;

import static com.stoyanr.mastermind.Constants.*;
import static com.stoyanr.mastermind.Score.ZERO_SCORE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
    
    private final transient Mastermind mastermind;
    private final transient AlgorithmFactory factory;
    private final transient String firstGuess;
    private final transient String secondGuess;
    
    private transient Algorithm algorithm;

    public AlgorithmTest(final Mastermind mastermind, final AlgorithmFactory factory, 
        final String firstGuess, final String secondGuess)
    {
        this.mastermind = mastermind;
        this.factory = factory;
        this.firstGuess = firstGuess;
        this.secondGuess = secondGuess;
    }

    @Parameters
    public static Collection<Object[]> data()
    {
        // @formatter:off, @checkstyle:off
        final Object[][] data = new Object[][]
        {
            { MM2, new SimpleAlgorithmFactory(MM2), MM2_FIRST_GUESS_SIMPLE, MM2_SECOND_GUESS_SIMPLE },
            { MM2, new KnuthAlgorithmFactory(MM2), MM2_FIRST_GUESS_KNUTH, MM2_SECOND_GUESS_KNUTH },
            { MM2, new PharaoxAlgorithmFactory(MM2, 0.0), MM2_FIRST_GUESS_KNUTH, MM2_SECOND_GUESS_KNUTH },
            { MM2, new ExpectedSizeAlgorithmFactory(MM2), MM2_FIRST_GUESS_ESIZE, MM2_SECOND_GUESS_ESIZE },
            { MM2, new DumbAlgorithmFactory(MM2), MM2_FIRST_GUESS_DUMB, MM2_SECOND_GUESS_DUMB }
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
    public final void testSecondGuess()
    {
        algorithm.putGuessScore(firstGuess, ZERO_SCORE);
        final String guess = algorithm.makeGuess();
        assertEquals(M_WRONG_GUESS, secondGuess, guess);
    }
}
