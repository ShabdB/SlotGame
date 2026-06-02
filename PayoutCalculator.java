/**
 * Handles all payout calculations for the 3x3 slot machine game.
 * Checks all 9 winning lines and computes total payout amounts.
 *
 * @author Raj Patel
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
     * Calculates total payout for all 9 winning lines on the 3x3 grid.
     * Pre-condition: grid has exactly 9 valid symbol strings, bet >= 1
     * Post-condition: returns total payout as int, 0 if no winning lines
     */
    public int calculatePayout(String[] grid, int bet, Player player) {
        int totalPayout = 0;
        boolean anyWin  = false;

        // All 8 three-symbol lines
        int[][] lines = {
            {0, 1, 2}, // top row
            {3, 4, 5}, // middle row
            {6, 7, 8}, // bottom row
            {0, 3, 6}, // left column
            {1, 4, 7}, // center column
            {2, 5, 8}, // right column
            {0, 4, 8}, // diagonal top-left to bottom-right
            {2, 4, 6}  // diagonal top-right to bottom-left
        };

        for (int[] line : lines) {
            String s1   = grid[line[0]];
            String s2   = grid[line[1]];
            String s3   = grid[line[2]];
            int    mult = getWinMultiplier(s1, s2, s3);

            if (mult > 0) {
                anyWin        = true;
                double payout = bet * mult;

                // Apply temporary multiplier if active
                if (player.getTempMultiplierSpinsLeft() > 0) {
                    payout *= player.getTempMultiplier();
                }

                // Apply permanent multiplier if active
                if (player.getPermMultiplierRoundsLeft() > 0) {
                    payout *= player.getPermMultiplier();
                }

                // Apply frenzy tripling if active
                if (player.hasFrenzy()) {
                    payout *= 3;
                }

                totalPayout += (int) payout;
            }
        }

        // Check middle 4 line (positions 1,3,5,7)
        // All 4 must match — pays double the normal multiplier
        int middleMult = getMiddleFourMultiplier(
            grid[1], grid[3], grid[5], grid[7]
        );

        if (middleMult > 0) {
            anyWin        = true;
            double payout = bet * middleMult * 2;

            if (player.getTempMultiplierSpinsLeft() > 0) {
                payout *= player.getTempMultiplier();
            }
            if (player.getPermMultiplierRoundsLeft() > 0) {
                payout *= player.getPermMultiplier();
            }
            if (player.hasFrenzy()) {
                payout *= 3;
            }

            totalPayout += (int) payout;
        }

        return totalPayout;
    }

    // -------------------------------------------------------
    // Win Multiplier Logic
    // -------------------------------------------------------

    /**
     * Determines the base multiplier for three symbols on a line.
     * Pre-condition: s1, s2, s3 are valid non-null symbol strings
     * Post-condition: returns correct multiplier int, or 0 if no win
     */
    public int getWinMultiplier(String s1, String s2, String s3) {

        // Three of a kind
        if (s1.equals(s2) && s2.equals(s3) && !s1.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }

        // Three wilds
        if (s1.equals(Reel.WILD) && s2.equals(Reel.WILD) && s3.equals(Reel.WILD)) {
            return LUCKY7_MULT;
        }

        // Two matching + one wild
        if (s1.equals(s2) && !s1.equals(Reel.WILD) && s3.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }
        if (s1.equals(s3) && !s1.equals(Reel.WILD) && s2.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }
        if (s2.equals(s3) && !s2.equals(Reel.WILD) && s1.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s2);
        }

        // One symbol + two wilds
        if (s1.equals(Reel.WILD) && s2.equals(Reel.WILD) && !s3.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s3);
        }
        if (s1.equals(Reel.WILD) && s3.equals(Reel.WILD) && !s2.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s2);
        }
        if (s2.equals(Reel.WILD) && s3.equals(Reel.WILD) && !s1.equals(Reel.WILD)) {
            return getMultiplierForSymbol(s1);
        }

        return 0;
    }

    /**
     * Determines multiplier for the middle 4 line (all 4 must match).
     * Pre-condition: m1, m2, m3, m4 are valid non-null symbol strings
     * Post-condition: returns multiplier if all 4 match, 0 otherwise
     */
    public int getMiddleFourMultiplier(String m1, String m2,
                                        String m3, String m4) {
        int    wilds   = 0;
        String nonWild = null;

        String[] symbols = {m1, m2, m3, m4};
        for (String s : symbols) {
            if (s.equals(Reel.WILD)) {
                wilds++;
            } else {
                if (nonWild == null) {
                    nonWild = s;
                } else if (!nonWild.equals(s)) {
                    return 0;
                }
            }
        }

        if (wilds == 4)              return LUCKY7_MULT;
        if (nonWild != null)         return getMultiplierForSymbol(nonWild);
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
     * Returns a description of all winning lines hit on this spin.
     * Pre-condition: grid has 9 valid symbol strings
     * Post-condition: returns human readable result string
     */
    public String getResultDescription(String[] grid) {
        StringBuilder sb        = new StringBuilder();
        String[]      lineNames = {
            "Top Row", "Mid Row", "Bot Row",
            "Left Col", "Center Col", "Right Col",
            "Diagonal \\", "Diagonal /"
        };

        int[][] lines = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };

        for (int i = 0; i < lines.length; i++) {
            int mult = getWinMultiplier(
                grid[lines[i][0]],
                grid[lines[i][1]],
                grid[lines[i][2]]
            );
            if (mult > 0) {
                sb.append(lineNames[i])
                  .append(": ").append(mult).append("x  ");
            }
        }

        int m4 = getMiddleFourMultiplier(
            grid[1], grid[3], grid[5], grid[7]
        );
        if (m4 > 0) {
            sb.append("Middle 4: ").append(m4 * 2).append("x  ");
        }

        if (sb.length() == 0) return "No winning lines.";
        return sb.toString();
    }
}