/**
 * Frenzy Extension upgrade. Adds 15 seconds to an active Frenzy Mode.
 * Cheaper than buying a full Frenzy Mode.
 * Can only be used while Frenzy Mode is already active.
 *
 * @author Zain Atif
 */
public class FrenzyExtension extends Upgrade {

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a FrenzyExtension upgrade.
     * Pre-condition: none
     * Post-condition: upgrade is ready to be purchased and applied
     */
    public FrenzyExtension() {
        super("Frenzy Extension",
              "Adds 15 seconds to active Frenzy Mode. Must have Frenzy active to use.",
              2);
    }

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Extends the active Frenzy Mode by 15 seconds.
     * Pre-condition: player is not null, player must have frenzy active
     * Post-condition: player's frenzy timer increased by 15 seconds
     */
    @Override
    public void apply(Player player) {
        if (player.hasFrenzy()) {
            player.extendFrenzy();
            player.addUpgrade(getName());
            setPurchased(true);
        }
    }

    // -------------------------------------------------------
    // Validation
    // -------------------------------------------------------

    /**
     * Checks whether this extension can currently be applied.
     * Pre-condition: player is not null
     * Post-condition: returns true only if frenzy is currently active
     */
    public boolean canApply(Player player) {
        return player.hasFrenzy();
    }
}