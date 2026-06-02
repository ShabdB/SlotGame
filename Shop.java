import java.util.ArrayList;

public class Shop {
    private ArrayList<Upgrade> upgrades;
    private Reel[] reels;

    public Shop(Reel[] reels) {
        this.reels    = reels;
        this.upgrades = new ArrayList<Upgrade>();
        stockShop();
    }

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

    public boolean purchase(int index, Player player) {
        if (index < 0 || index >= upgrades.size()) return false;
        Upgrade upgrade = upgrades.get(index);
        if (!canAfford(player, upgrade)) return false;
        if (!isAvailable(upgrade, player)) return false;
        player.spendTickets(upgrade.getCost());
        if (upgrade instanceof LuckyDrop) {
            ((LuckyDrop) upgrade).applyToReels(reels);
        } else if (upgrade instanceof DiamondFever) {
            ((DiamondFever) upgrade).applyToReels(reels);
        } else {
            upgrade.apply(player);
        }
        return true;
    }

    public boolean canAfford(Player player, Upgrade upgrade) {
        return player.getTickets() >= upgrade.getCost();
    }

    public boolean isAvailable(Upgrade upgrade, Player player) {
        if (upgrade instanceof FrenzyMode && player.hasFrenzy()) return false;
        if (upgrade instanceof FrenzyExtension && !player.hasFrenzy()) return false;
        if (upgrade instanceof InsurancePolicy && player.hasInsurance()) return false;
        if (upgrade instanceof LuckyDrop && upgrade.isPurchased()) return false;
        if (upgrade instanceof DiamondFever && upgrade.isPurchased()) return false;
        return true;
    }

    public ArrayList<Upgrade> getUpgrades() { return upgrades; }
    public Upgrade getUpgrade(int index) { return upgrades.get(index); }
    public int getSize() { return upgrades.size(); }

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
