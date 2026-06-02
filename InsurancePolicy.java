/**
 * Insurance Policy upgrade. Guarantees a minor payout after
 * 5 consecutive spins with no winning combination.
 * High cost item designed for late game use.
 *
 * @author Zain Atif
 */
public class InsurancePolicy extends Upgrade {

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates an InsurancePolicy upgrade.
     * Pre-condition: none
     * Post-condition: upgrade is ready to be purchased and applied
     */
    public InsurancePolicy() {
        super("Insurance Policy",
              "After 5 spins in a row with no win, guarantees a minor payout on the 6th spin.",
              4);
    }

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Activates the insurance policy on the player.
     * Pre-condition: player is not null
     * Post-condition: player's insurance is set to true
     */
    @Override
    public void apply(Player player) {
        player.setInsurance(true);
        player.addUpgrade(getName());
        setPurchased(true);
    }

    // -------------------------------------------------------
    // Validation
    // -------------------------------------------------------

    /**
     * Checks whether insurance can be purchased.
     * Player should not be able to buy it if already owned.
     * Pre-condition: player is not null
     * Post-condition: returns true if player does not already have insurance
     */
    public boolean canApply(Player player) {
        return !player.hasInsurance();
    }
}