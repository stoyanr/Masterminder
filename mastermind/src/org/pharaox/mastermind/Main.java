package org.pharaox.mastermind;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

public class Main
{
    private static final String KNUTH_ALG = "knuth";
    private static final String ESIZE_ALG = "esize";
    
    private static final String DEFAULT_ALPHABET = "ABCD";
    private static final int DEFAULT_LENGTH = 2;
    private static final boolean DEFAULT_UNIQUE_CHARS = false;
    private static final int DEFAULT_MAX_ROUNDS = 10;
    private static final String DEFAULT_ALG = KNUTH_ALG;
    private static final int DEFAULT_PRECALC_LEVELS = 3;
    
    @SuppressWarnings("unused")
    private final transient String[] args; // NOPMD
    private final transient Reader reader;
    private final transient Writer writer;
    
    private transient String alphabet = DEFAULT_ALPHABET; // NOPMD
    private transient int length = DEFAULT_LENGTH; // NOPMD
    private transient boolean uniqueChars = DEFAULT_UNIQUE_CHARS; // NOPMD
    private transient int maxRounds = DEFAULT_MAX_ROUNDS; // NOPMD
    private transient String alg = DEFAULT_ALG; // NOPMD
    private transient int precalcLevels = DEFAULT_PRECALC_LEVELS; // NOPMD
    
    Main(final String[] args, final Reader reader, final Writer writer)
    {
        super();
        this.args = Arrays.copyOf(args, args.length);
        this.reader = reader;
        this.writer = writer;
        parseArgs();
    }

    private void parseArgs()
    {
        // TODO
    }

    public final void run()
    {
        try
        {
            playGame();
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

    private AlgorithmFactory createFactory(final Mastermind mastermind)
    {
        AlgorithmFactory factory; 
        if (alg.equals(KNUTH_ALG))
        {
            factory = new KnuthAlgorithmFactory(mastermind); 
        }
        else if (alg.equals(ESIZE_ALG))
        {
            factory = new ExpectedSizeAlgorithmFactory(mastermind); 
        }
        else
        {
            factory = new SimpleAlgorithmFactory(mastermind);
        }
        return factory;
    }

    private void reportError(final MastermindException exc)
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

    public static void main(final String[] args)
    {
        while (true)
        {
            final InputStreamReader reader = new InputStreamReader(System.in);
            final OutputStreamWriter writer = new OutputStreamWriter(System.out);
            new Main(args, reader, writer).run();
        }
    }

}
