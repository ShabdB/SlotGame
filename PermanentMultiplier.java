/**
 * Permanent multiplier upgrade. Boosts all payouts for current round
 * and the next 2 rounds after purchase.
 *
 * @author Zain Atif
 */
public class PermanentMultiplier extends Upgrade {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    private double multiplierValue;

    // -------------------------------------------------------
    // Cost Table
    // -------------------------------------------------------
    // 1.1x = 20 coins
    // 1.2x = 35 coins
    // 1.5x = 60 coins
    // 2.0x = 100 coins

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a PermanentMultiplier upgrade at the given multiplier tier.
     * Pre-condition: multiplierValue is one of 1.1, 1.2, 1.5, or 2.0
     * Post-condition: upgrade is initialized with correct cost and description
     */
    public PermanentMultiplier(double multiplierValue) {
        super("Permanent Multiplier x" + multiplierValue,
              "Applies a " + multiplierValue + "x bonus to all payouts for this round and the next 2.",
              getCostForMultiplier(multiplierValue));
        this.multiplierValue = multiplierValue;
    }

    // -------------------------------------------------------
    // Static Helper
    // -------------------------------------------------------

    /**
     * Returns the coin cost for a given multiplier tier.
     * Pre-condition: multiplierValue is one of 1.1, 1.2, 1.5, or 2.0
     * Post-condition: returns the correct int cost for that tier
     */
    private static int getCostForMultiplier(double multiplierValue) {
    if (multiplierValue == 1.1) return 2;
    if (multiplierValue == 1.2) return 3;
    if (multiplierValue == 1.5) return 4;
    if (multiplierValue == 2.0) return 6;
    return 2;
}

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Applies the permanent multiplier to the player.
     * Pre-condition: player is not null
     * Post-condition: player's permanent multiplier is set for 3 rounds
     */
    @Override
    public void apply(Player player) {
        player.applyPermMultiplier(multiplierValue);
        setPurchased(true);
    }

    // -------------------------------------------------------
    // Accessors
    // -------------------------------------------------------

    /**
     * Returns the multiplier value of this upgrade.
     * Pre-condition: none
     * Post-condition: returns multiplierValue as double
     */
    public double getMultiplierValue() { return multiplierValue; }
}