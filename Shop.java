import java.util.ArrayList;

/**
 * Represents the in-game shop where players purchase upgrades.
 * Holds an ArrayList of all available upgrades.
 * Handles purchase validation and applying effects to the player.
 *
 * @author Zain Atif
 */
public class Shop {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    private ArrayList<Upgrade> upgrades;
    private Reel[] reels;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a Shop and populates it with all available upgrades.
     * Pre-condition: reels array has exactly 3 initialized Reel objects
     * Post-condition: shop is stocked with all 7 upgrade types
     */
    public Shop(Reel[] reels) {
        this.reels    = reels;
        this.upgrades = new ArrayList<Upgrade>();
        stockShop();
    }

    // -------------------------------------------------------
    // Shop Setup
    // -------------------------------------------------------

    /**
     * Populates the upgrades list with all available shop items.
     * Pre-condition: upgrades list is empty
     * Post-condition: all upgrade types added to the list
     */
    private void stockShop() {
        upgrades.add(new TempMultiplier(1.1, 5));
        upgrades.add(new PermanentMultiplier(1.1));
        upgrades.add(new PermanentMultiplier(1.2));
        upgrades.add(new PermanentMultiplier(1.5));
        upgrades.add(new PermanentMultiplier(2.0));
        upgrades.add(new LuckyDrop());
        upgrades.add(new DiamondFever());
        upgrades.add(new FrenzyMode());
        upgrades.add(new FrenzyExtension());
        upgrades.add(new InsurancePolicy());
    }

    // -------------------------------------------------------
    // Purchase Logic
    // -------------------------------------------------------

    /**
     * Attempts to purchase an upgrade by index for the player.
     * Pre-condition: index is valid, player is not null
     * Post-condition: upgrade applied and coins deducted if affordable
     */
    public boolean purchase(int index, Player player) {
        if (index < 0 || index >= upgrades.size()) {
            return false;
        }

        Upgrade upgrade = upgrades.get(index);

        if (!canAfford(player, upgrade)) {
            return false;
        }

        if (!isAvailable(upgrade, player)) {
            return false;
        }

        // Deduct cost and apply effect
        player.purchase(upgrade.getCost());

        // Special handling for reel-based upgrades
        if (upgrade instanceof LuckyDrop) {
            ((LuckyDrop) upgrade).applyToReels(reels);
        } else if (upgrade instanceof DiamondFever) {
            ((DiamondFever) upgrade).applyToReels(reels);
        } else {
            upgrade.apply(player);
        }

        return true;
    }

    /**
     * Checks whether the player can afford a given upgrade.
     * Pre-condition: player and upgrade are not null
     * Post-condition: returns true if player has enough coins
     */
    public boolean canAfford(Player player, Upgrade upgrade) {
        return player.getCoins() >= upgrade.getCost();
    }

    /**
     * Checks whether an upgrade is currently available to purchase.
     * Pre-condition: upgrade and player are not null
     * Post-condition: returns false if upgrade is already purchased or conditions not met
     */
    public boolean isAvailable(Upgrade upgrade, Player player) {
        if (upgrade instanceof FrenzyMode && player.hasFrenzy()) {
            return false;
        }
        if (upgrade instanceof FrenzyExtension && !player.hasFrenzy()) {
            return false;
        }
        if (upgrade instanceof InsurancePolicy && player.hasInsurance()) {
            return false;
        }
        if (upgrade instanceof LuckyDrop && upgrade.isPurchased()) {
            return false;
        }
        if (upgrade instanceof DiamondFever && upgrade.isPurchased()) {
            return false;
        }
        return true;
    }

    // -------------------------------------------------------
    // Accessors
    // -------------------------------------------------------

    /**
     * Returns the full list of upgrades in the shop.
     * Pre-condition: none
     * Post-condition: returns upgrades as ArrayList of Upgrade objects
     */
    public ArrayList<Upgrade> getUpgrades() { return upgrades; }

    /**
     * Returns the upgrade at a specific index.
     * Pre-condition: index >= 0 and index < upgrades.size()
     * Post-condition: returns Upgrade object at that index
     */
    public Upgrade getUpgrade(int index) { return upgrades.get(index); }

    /**
     * Returns the number of upgrades available in the shop.
     * Pre-condition: none
     * Post-condition: returns size of upgrades list as int
     */
    public int getSize() { return upgrades.size(); }

    /**
     * Returns a formatted string listing all upgrades and their costs.
     * Pre-condition: none
     * Post-condition: returns full shop inventory as a String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SHOP ===\n");
        for (int i = 0; i < upgrades.size(); i++) {
            sb.append(i + 1).append(". ").append(upgrades.get(i).toString()).append("\n");
        }
        return sb.toString();
    }
}