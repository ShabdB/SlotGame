/**
 * Abstract base class for all shop upgrades and items.
 * Each upgrade has a name, description, and coin cost.
 * Subclasses define their own apply() behavior (polymorphism).
 *
 * @author Shabd Bhola
 */
public abstract class Upgrade {

    // -------------------------------------------------------
    // Fields
    // -------------------------------------------------------
    protected String name;
    protected String description;
    protected int cost;
    protected boolean purchased;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates an Upgrade with a name, description, and cost.
     * Pre-condition: name and description are not null, cost >= 0
     * Post-condition: Upgrade is initialized and not yet purchased
     */
    public Upgrade(String name, String description, int cost) {
        this.name        = name;
        this.description = description;
        this.cost        = cost;
        this.purchased   = false;
    }

    // -------------------------------------------------------
    // Abstract Method (must be implemented by each subclass)
    // -------------------------------------------------------

    /**
     * Applies this upgrade's effect to the player.
     * Pre-condition: player is not null, upgrade has been purchased
     * Post-condition: player's state is modified based on upgrade type
     */
    public abstract void apply(Player player);

    // -------------------------------------------------------
    // Accessors
    // -------------------------------------------------------

    /**
     * Returns the name of this upgrade.
     * Pre-condition: none
     * Post-condition: returns name as String
     */
    public String getName() { return name; }

    /**
     * Returns the description of this upgrade.
     * Pre-condition: none
     * Post-condition: returns description as String
     */
    public String getDescription() { return description; }

    /**
     * Returns the coin cost of this upgrade.
     * Pre-condition: none
     * Post-condition: returns cost as int
     */
    public int getCost() { return cost; }

    /**
     * Returns whether this upgrade has been purchased.
     * Pre-condition: none
     * Post-condition: returns purchased as boolean
     */
    public boolean isPurchased() { return purchased; }

    // -------------------------------------------------------
    // Mutators
    // -------------------------------------------------------

    /**
     * Marks this upgrade as purchased.
     * Pre-condition: none
     * Post-condition: purchased is set to true
     */
    public void setPurchased(boolean value) { this.purchased = value; }

    /**
     * Returns a string summary of this upgrade.
     * Pre-condition: none
     * Post-condition: returns formatted String with name, cost, description
     */
    @Override
    public String toString() {
        return "[" + name + "] Cost: " + cost + " coins — " + description;
    }
}