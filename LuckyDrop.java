/**
 * Lucky Drop upgrade. Increases the odds of landing a Lucky 7.
 * Cherry and Lemon odds decrease to compensate.
 *
 * @author Zain Atif
 */
public class LuckyDrop extends Upgrade {

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a LuckyDrop upgrade.
     * Pre-condition: none
     * Post-condition: upgrade is ready to be purchased and applied
     */
    public LuckyDrop() {
        super("Lucky Drop",
              "Boosts Lucky 7 odds from 1% to 15%. Cherry drops 8%, Lemon drops 7%.",
              80);
    }

    // -------------------------------------------------------
    // Apply Effect
    // -------------------------------------------------------

    /**
     * Applies the Lucky Drop odds boost to all three reels.
     * Pre-condition: reels array has exactly 3 Reel objects
     * Post-condition: each reel's Lucky 7 odds increased, Cherry and Lemon reduced
     */
    public void applyToReels(Reel[] reels) {
        for (Reel reel : reels) {
            reel.applyLuckyDrop();
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