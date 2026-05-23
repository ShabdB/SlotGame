/**
 * Handles all payout calculations for the slot machine game.
 * Determines winning combinations and computes payout amounts.
 *
 * @author Shabd Bhola
 */
public class PayoutCalculator {

    // -------------------------------------------------------
    // Multiplier Constants
    // -------------------------------------------------------
    public static final int CHERRY_MULT  = 3;
    public static final int LEMON_MULT   = 4;
    public static final int ORANGE_MULT  = 6;
    public static final int PLUM_MULT    = 10;
    public static final int BELL_MULT    = 15;
    public static final int DIAMOND_MULT = 30;
    public static final int LUCKY7_MULT  = 100;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a PayoutCalculator instance.
     * Pre-condition: none
     * Post-condition: calculator is ready to evaluate spin results
     */
    public PayoutCalculator() {}

    // -------------------------------------------------------
    // Core Payout Logic
    // -------------------------------------------------------

    /**
     * Calculates the payout for a given set of three reel symbols.
     * Applies temp multiplier, perm multiplier, and frenzy if active.
     * Pre-condition: s1, s2, s3 are valid symbol strings, bet >= 1
     * Post-condition: returns total payout as int, 0 if no winning combo
     */
    public int calculatePayout(String s1, String s2, String s3,
                               int bet, Player player) {
        int baseMultiplier = getWinMultiplier(s1, s2, s3);

        if (baseMultiplier == 0) {
            player.recordMiss();
            return 0;
        }

        double payout = bet * baseMultiplier;

        // Apply temporary multiplier if active (only on wins)
        if (player.getTempMultiplierSpinsLeft() > 0) {
            payout *= player.getTempMultiplier();
        }

        // Apply permanent multiplier if active
        if (player.getPermMultiplierRoundsLeft() > 0) {
            payout *= player.getPermMultiplier();
        }

        // Apply frenzy mode tripling if active
        if (player.hasFrenzy()) {
            payout *= 3;
        }

        return (int) payout;
    }

    /**
     * Determines the base multiplier for three symbols.
     * Checks for three of a kind, two plus wild, one plus two wilds.
     * Pre-condition: s1, s2, s3 are valid non-null symbol strings
     * Post-condition: returns the correct multiplier int, or 0 if no win
     */
    public int getWinMultiplier(String s1, String s2, String s3) {

        // --- Three of a kind ---
        if (s1.equals(s2) && s2.equals(s3) && !s1.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }

        // --- Three wilds ---
        if (s1.equals(Reel.WILD) && s2.equals(Reel.WILD) && s3.equals(Reel.WILD)) {
            return LUCKY7_MULT;
        }

        // --- Two matching + one wild ---
        if (s1.equals(s2) && !s1.equals(Reel.WILD) && s3.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }
        if (s1.equals(s3) && !s1.equals(Reel.WILD) && s2.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }
        if (s2.equals(s3) && !s2.equals(Reel.WILD) && s1.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s2);
        }

        // --- One symbol + two wilds ---
        if (s1.equals(Reel.WILD) && s2.equals(Reel.WILD) && !s3.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s3);
        }
        if (s1.equals(Reel.WILD) && s3.equals(Reel.WILD) && !s2.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s2);
        }
        if (s2.equals(Reel.WILD) && s3.equals(Reel.WILD) && !s1.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }

        // --- No winning combination ---
        return 0;
    }

    /**
     * Returns the payout multiplier for a given symbol name.
     * Pre-condition: symbol is a valid non-null symbol string
     * Post-condition: returns the int multiplier, or 0 if unrecognized
     */
    public int getMultiplierForSymbol(String symbol) {
        switch (symbol) {
            case Reel.CHERRY:  return CHERRY_MULT;
            case Reel.LEMON:   return LEMON_MULT;
            case Reel.ORANGE:  return ORANGE_MULT;
            case Reel.PLUM:    return PLUM_MULT;
            case Reel.BELL:    return BELL_MULT;
            case Reel.DIAMOND: return DIAMOND_MULT;
            case Reel.LUCKY7:  return LUCKY7_MULT;
            default:           return 0;
        }
    }

    /**
     * Returns the insurance payout when triggered after 5 consecutive misses.
     * Pre-condition: player has insurance and consecutiveMisses >= 5
     * Post-condition: returns a minor payout based on current bet
     */
    public int getInsurancePayout(int bet) {
        return bet * 2;
    }

    /**
     * Checks if insurance should trigger on this spin.
     * Pre-condition: player object is valid
     * Post-condition: returns true if insurance triggers, false otherwise
     */
    public boolean shouldInsuranceTrigger(Player player) {
        return player.hasInsurance() && player.getConsecutiveMisses() >= 5;
    }

    /**
     * Returns a string description of a winning combination.
     * Pre-condition: s1, s2, s3 are valid symbol strings
     * Post-condition: returns human-readable result string
     */
    public String getResultDescription(String s1, String s2, String s3) {
        int mult = getWinMultiplier(s1, s2, s3);
        if (mult == 0) {
            return "No winning combination.";
        }
        return s1 + " | " + s2 + " | " + s3 +
               " — " + mult + "x multiplier!";
    }
}