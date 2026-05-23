/**
 * Diamond Fever upgrade. Increases the odds of landing a Diamond.
 * Cherry and Lemon odds decrease to compensate.
 * Priced slightly below Lucky Drop to maintain Lucky Drop's prestige.
 *
 * @author Zain Atif
 */
public class DiamondFever extends Upgrade {

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a DiamondFever upgrade.
     * Pre-condition: none
     * Post-condition: upgrade is ready to be purchased and applied
     */
    public DiamondFever() {
        super("Diamond Fever",
              "Boosts Diamond odds from 4% to 20%. Cherry drops 8%, Lemon drops 8%.",
              70);
    }

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Applies the Diamond Fever odds boost to all three reels.
     * Pre-condition: reels array has exactly 3 Reel objects
     * Post-condition: each reel's Diamond odds increased, Cherry and Lemon reduced
     */
    public void applyToReels(Reel[] reels) {
        for (Reel reel : reels) {
            reel.applyDiamondFever();
        }
        setPurchased(true);
    }

    /**
     * Required apply override. Use applyToReels() for full effect.
     * Pre-condition: player is not null
     * Post-condition: marks upgrade as purchased on player
     */
    @Override
    public void apply(Player player) {
        player.addUpgrade(getName());
        setPurchased(true);
    }
}