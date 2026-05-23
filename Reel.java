import java.util.Random;

/**
 * Represents a single reel in the slot machine.
 * Handles symbol generation and probability management.
 *
 * @author Shabd Bhola
 */
public class Reel {

    // -------------------------------------------------------
    // Symbol Constants
    // -------------------------------------------------------
    public static final String CHERRY  = "Cherry";
    public static final String LEMON   = "Lemon";
    public static final String ORANGE  = "Orange";
    public static final String PLUM    = "Plum";
    public static final String BELL    = "Bell";
    public static final String DIAMOND = "Diamond";
    public static final String LUCKY7  = "Lucky7";
    public static final String WILD    = "Wild";

    // -------------------------------------------------------
    // Base Probabilities (must always add up to 100)
    // -------------------------------------------------------
    private int cherryOdds;
    private int lemonOdds;
    private int orangeOdds;
    private int plumOdds;
    private int bellOdds;
    private int diamondOdds;
    private int lucky7Odds;
    private int wildOdds;

    private Random random;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a Reel with default base probabilities.
     * Pre-condition: none
     * Post-condition: Reel is ready to spin with standard odds
     */
    public Reel() {
        random = new Random();
        resetOdds();
    }

    // -------------------------------------------------------
    // Odds Management
    // -------------------------------------------------------

    /**
     * Resets all symbol odds back to their base values.
     * Pre-condition: none
     * Post-condition: all odds restored to defaults, total = 100
     */
    public void resetOdds() {
        cherryOdds  = 35;
        lemonOdds   = 25;
        orangeOdds  = 15;
        plumOdds    = 12;
        bellOdds    = 7;
        diamondOdds = 4;
        lucky7Odds  = 1;
        wildOdds    = 1;
    }

    /**
     * Applies Lucky Drop odds boost.
     * Lucky 7 goes from 1% to 15%.
     * Cherry drops 8% and Lemon drops 7% to compensate.
     * Pre-condition: Lucky Drop has been purchased
     * Post-condition: lucky7Odds = 15, cherryOdds -= 8, lemonOdds -= 7
     */
    public void applyLuckyDrop() {
        lucky7Odds  = 15;
        cherryOdds -= 8;
        lemonOdds  -= 7;
    }

    /**
     * Applies Diamond Fever odds boost.
     * Diamond goes from 4% to 20%.
     * Cherry drops 8% and Lemon drops 8% to compensate.
     * Pre-condition: Diamond Fever has been purchased
     * Post-condition: diamondOdds = 20, cherryOdds -= 8, lemonOdds -= 8
     */
    public void applyDiamondFever() {
        diamondOdds = 20;
        cherryOdds -= 8;
        lemonOdds  -= 8;
    }

    // -------------------------------------------------------
    // Spinning
    // -------------------------------------------------------

    /**
     * Spins the reel and returns a random symbol based on current odds.
     * Pre-condition: all odds add up to 100
     * Post-condition: returns one symbol string based on weighted probability
     */
    public String spin() {
        int roll = random.nextInt(100);
        int cumulative = 0;

        cumulative += cherryOdds;
        if (roll < cumulative) return CHERRY;

        cumulative += lemonOdds;
        if (roll < cumulative) return LEMON;

        cumulative += orangeOdds;
        if (roll < cumulative) return ORANGE;

        cumulative += plumOdds;
        if (roll < cumulative) return PLUM;

        cumulative += bellOdds;
        if (roll < cumulative) return BELL;

        cumulative += diamondOdds;
        if (roll < cumulative) return DIAMOND;

        cumulative += lucky7Odds;
        if (roll < cumulative) return LUCKY7;

        return WILD;
    }

    // -------------------------------------------------------
    // Accessors
    // -------------------------------------------------------

    /**
     * Returns current cherry odds.
     * Pre-condition: none
     * Post-condition: returns cherryOdds as int
     */
    public int getCherryOdds()  { return cherryOdds; }

    /**
     * Returns current lemon odds.
     * Pre-condition: none
     * Post-condition: returns lemonOdds as int
     */
    public int getLemonOdds()   { return lemonOdds; }

    /**
     * Returns current diamond odds.
     * Pre-condition: none
     * Post-condition: returns diamondOdds as int
     */
    public int getDiamondOdds() { return diamondOdds; }

    /**
     * Returns current lucky 7 odds.
     * Pre-condition: none
     * Post-condition: returns lucky7Odds as int
     */
    public int getLucky7Odds()  { return lucky7Odds; }

    /**
     * Returns a string showing all current symbol odds.
     * Pre-condition: none
     * Post-condition: returns formatted odds summary as String
     */
    @Override
    public String toString() {
        return "Odds — Cherry: " + cherryOdds +
               "% | Lemon: "   + lemonOdds +
               "% | Orange: "  + orangeOdds +
               "% | Plum: "    + plumOdds +
               "% | Bell: "    + bellOdds +
               "% | Diamond: " + diamondOdds +
               "% | Lucky7: "  + lucky7Odds +
               "% | Wild: "    + wildOdds + "%";
    }
}