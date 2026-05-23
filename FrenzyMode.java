/**
 * Frenzy Mode upgrade. Triples all payout values for 60 seconds.
 * Only one Frenzy Mode can be active per round.
 * Can be extended using the FrenzyExtension upgrade.
 *
 * @author Zain Atif
 */
public class FrenzyMode extends Upgrade {

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a FrenzyMode upgrade.
     * Pre-condition: none
     * Post-condition: upgrade is ready to be purchased and applied
     */
    public FrenzyMode() {
        super("Frenzy Mode",
              "Triples all payout values for 60 seconds. One per round only.",
              50);
    }

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Activates Frenzy Mode on the player for 60 seconds.
     * Pre-condition: player is not null, player does not already have frenzy active
     * Post-condition: player's frenzy is activated and timer set to 60 seconds
     */
    @Override
    public void apply(Player player) {
        if (!player.hasFrenzy()) {
            player.activateFrenzy();
            player.addUpgrade(getName());
            setPurchased(true);
        }
    }
}