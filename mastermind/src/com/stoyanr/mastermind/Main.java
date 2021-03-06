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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;

import com.stoyanr.util.Arguments;
import com.stoyanr.util.ArgumentsException;

/**
 * A simple command line program to test and demonstrate the capabilities of this package. See <a
 * href="https://github.com/stoyanr/Masterminder/wiki">Masterminder Wiki</a> for more information.
 * 
 * @author Stoyan Rachev
 */
public class Main
{
    private static final String ARG_ALPHABET = "a";
    private static final String ARG_LENGTH = "l";
    private static final String ARG_UNIQUE_CHARS = "u";
    private static final String ARG_MAX_ROUNDS = "r";
    private static final String ARG_ALG = "s";
    private static final String ARG_PRECALC_LEVELS = "p";
    private static final String ARG_MODE = "m";
    private static final String ARGS_SCHEMA = ARG_ALPHABET + "*," + ARG_LENGTH + "#,"
        + ARG_UNIQUE_CHARS + "!," + ARG_MAX_ROUNDS + "#," + ARG_ALG + "*," + ARG_PRECALC_LEVELS
        + "#," + ARG_MODE + "*";

    private static final String ALG_SIMPLE = "simple";
    private static final String ALG_KNUTH = "knuth";
    private static final String ALG_EXP_SIZE = "exp_size";

    private static final String MODE_PLAY = "play";
    private static final String MODE_EVALUATE = "eval";

    private static final String DEFAULT_ALPHABET = "ABCDEF";
    private static final int DEFAULT_LENGTH = 4;
    private static final boolean DEFAULT_UNIQUE_CHARS = false;
    private static final int DEFAULT_MAX_ROUNDS = 7;
    private static final String DEFAULT_ALG = ALG_SIMPLE;
    private static final int DEFAULT_PRECALC_LEVELS = 1;
    private static final String DEFAULT_MODE = MODE_PLAY;

    private final transient String[] args;
    private final transient Reader reader;
    private final transient Writer writer;

    private transient String alphabet;
    private transient int length;
    private transient boolean uniqueChars;
    private transient int maxRounds;
    private transient String alg;
    private transient int precalcLevels;
    private transient String mode;

    Main(final String[] args, final Reader reader, final Writer writer)
    {
        assert (args != null && reader != null && writer != null);
        this.args = Arrays.copyOf(args, args.length);
        this.reader = reader;
        this.writer = writer;
        initialize();
    }

    private void initialize()
    {
        try
        {
            final Arguments arguments = new Arguments(ARGS_SCHEMA, args);
            alphabet = arguments.getString(ARG_ALPHABET, DEFAULT_ALPHABET);
            length = arguments.getInt(ARG_LENGTH, DEFAULT_LENGTH);
            uniqueChars = arguments.getBoolean(ARG_UNIQUE_CHARS, DEFAULT_UNIQUE_CHARS);
            maxRounds = arguments.getInt(ARG_MAX_ROUNDS, DEFAULT_MAX_ROUNDS);
            alg = arguments.getString(ARG_ALG, DEFAULT_ALG);
            precalcLevels = arguments.getInt(ARG_PRECALC_LEVELS, DEFAULT_PRECALC_LEVELS);
            mode = arguments.getString(ARG_MODE, DEFAULT_MODE);
        }
        catch (ArgumentsException e)
        {
            reportError(e);
        }
    }

    /**
     * Runs the program.
     */
    public final void run()
    {
        try
        {
            if (mode.equals(MODE_PLAY))
            {
                playGame();
            }
            else if (mode.equals(MODE_EVALUATE))
            {
                evaluateAlgorithm();
            }
        }
        catch (final MastermindException e)
        {
            reportError(e);
        }
    }

    private void playGame()
    {
        final Mastermind mastermind = new Mastermind(alphabet, length, uniqueChars);
        final AlgorithmFactory factory = createFactory(mastermind);
        final Algorithm algorithm = factory.getAlgorithm();
        final Player player = new ReaderWriterPlayer(mastermind, reader, writer);
        final GuessCalculator calc = new GuessCalculator(mastermind, factory, precalcLevels);
        final Game game = new Game(mastermind, algorithm, maxRounds, player, calc);
        game.play();
    }

    private void evaluateAlgorithm()
    {
        final Mastermind mastermind = new Mastermind(alphabet, length, uniqueChars);
        final AlgorithmFactory factory = createFactory(mastermind);
        final AlgorithmEvaluator eval = new AlgorithmEvaluator(mastermind, factory, precalcLevels);
        eval.evaluate();
    }

    private AlgorithmFactory createFactory(final Mastermind mastermind)
    {
        AlgorithmFactory factory;
        if (alg.equals(ALG_KNUTH))
        {
            factory = new KnuthAlgorithmFactory(mastermind);
        }
        else if (alg.equals(ALG_EXP_SIZE))
        {
            factory = new ExpectedSizeAlgorithmFactory(mastermind);
        }
        else if (alg.equals(ALG_SIMPLE))
        {
            factory = new SimpleAlgorithmFactory(mastermind);
        }
        else
        {
            throw new MastermindException();
        }
        return factory;
    }

    private void reportError(final RuntimeException exc)
    {
        try
        {
            writer.write(exc.getClass().toString() + "\n");
            writer.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace(); // NOPMD
        }
    }

    /**
     * Runs the command line program. Creates a new Main instance with the passed arguments and
     * console reader and writer and call its {@link #run()} method.
     * 
     * @param args The program arguments.
     * @throws UnsupportedEncodingException
     */
    public static void main(final String[] args) throws UnsupportedEncodingException
    {
        final InputStreamReader reader = new InputStreamReader(System.in, "UTF-8");
        final OutputStreamWriter writer = new OutputStreamWriter(System.out, "UTF-8");
        new Main(args, reader, writer).run();
    }

}
