/**
 * Temporary multiplier upgrade. Boosts payouts for a set number of spins.
 * Only consumed when a winning combination is hit.
 *
 * @author Zain Atif
 */
public class TempMultiplier extends Upgrade {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    private double multiplierValue;
    private int spinDuration;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a TempMultiplier upgrade.
     * Pre-condition: multiplierValue > 1.0, spinDuration > 0
     * Post-condition: upgrade is ready to be purchased and applied
     */
    public TempMultiplier(double multiplierValue, int spinDuration) {
        super("Temp Multiplier x" + multiplierValue,
              "Adds a " + multiplierValue + "x bonus on wins for " + spinDuration + " spins.",
              1);
        this.multiplierValue = multiplierValue;
        this.spinDuration    = spinDuration;
    }

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Applies the temporary multiplier to the player.
     * Pre-condition: player is not null
     * Post-condition: player's temp multiplier and spin count are set
     */
    @Override
    public void apply(Player player) {
        player.applyTempMultiplier(multiplierValue, spinDuration);
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

    /**
     * Returns how many spins this multiplier lasts.
     * Pre-condition: none
     * Post-condition: returns spinDuration as int
     */
    public int getSpinDuration() { return spinDuration; }
}