import java.util.ArrayList;

/**
 * Represents the player in the slot machine game.
 * Tracks coins, tickets, spins, current round, and owned upgrades.
 *
 * @author Raj Patel
 */
public class Player {

    // Fields
    private int coins;
    private int tickets;
    private int spinsLeft;
    private int currentRound;
    private int currentBet;
    private ArrayList<String> upgrades;
    private int consecutiveMisses;
    private boolean hasInsurance;
    private boolean hasFrenzy;
    private int frenzySecondsLeft;
    private int tempMultiplierSpinsLeft;
    private double tempMultiplier;
    private double permMultiplier;
    private int permMultiplierRoundsLeft;

    // Constants
    public static final int STARTING_COINS = 7;
    public static final int MAX_SPINS = 7;
    public static final int TOTAL_ROUNDS = 10;

    // Constructor
    /**
     * Creates a new Player with default starting values.
     * Pre-condition: none
     * Post-condition: Player is initialized with 7 coins, round 1, full spins
     */
    public Player() {
        this.coins = STARTING_COINS;
        this.tickets = 2;
        this.spinsLeft = MAX_SPINS;
        this.currentRound = 1;
        this.currentBet = 1;
        this.upgrades = new ArrayList<String>();
        this.consecutiveMisses = 0;
        this.hasInsurance = false;
        this.hasFrenzy = false;
        this.frenzySecondsLeft = 0;
        this.tempMultiplierSpinsLeft = 0;
        this.tempMultiplier = 1.0;
        this.permMultiplier = 1.0;
    }

    // Accessors (Getters)

    /**
     * Returns the player's current coin count.
     * Pre-condition: none
     * Post-condition: returns coins as an int
     */
    public int getCoins() { 
        return coins; 
    }

    /**
     * Returns the player's current score for this round.
     * Pre-condition: none
     * Post-condition: returns score as an int
     */
    public int getTickets() { 
        return tickets; 
    }

    /**
     * Returns the number of spins remaining this round.
     * Pre-condition: none
     * Post-condition: returns spinsLeft as an int
     */
    public int getSpinsLeft() { 
        return spinsLeft; 
    }

    /**
     * Returns the current round number.
     * Pre-condition: none
     * Post-condition: returns currentRound as an int
     */
    public int getCurrentRound() { 
        return currentRound; 
    }

    /**
     * Returns the player's current bet amount.
     * Pre-condition: none
     * Post-condition: returns currentBet as an int
     */
    public int getCurrentBet() { 
        return currentBet; 
    }

    /**
     * Returns the list of upgrade names the player owns.
     * Pre-condition: none
     * Post-condition: returns upgrades as an ArrayList of Strings
     */
    public ArrayList<String> getUpgrades() { 
        return upgrades; 
    }

    /**
     * Returns how many spins in a row the player has missed.
     * Pre-condition: none
     * Post-condition: returns consecutiveMisses as an int
     */
    public int getConsecutiveMisses() { 
        return consecutiveMisses; 
    }

    /**
     * Returns whether the player has an active insurance policy.
     * Pre-condition: none
     * Post-condition: returns hasInsurance as a boolean
     */
    public boolean hasInsurance() { 
        return hasInsurance; 
    }

    /**
     * Returns whether frenzy mode is currently active.
     * Pre-condition: none
     * Post-condition: returns hasFrenzy as a boolean
     */
    public boolean hasFrenzy() { 
        return hasFrenzy; 
    }

    /**
     * Returns how many seconds are left in frenzy mode.
     * Pre-condition: none
     * Post-condition: returns frenzySecondsLeft as an int
     */
    public int getFrenzySecondsLeft() { 
        return frenzySecondsLeft; 
    }

    /**
     * Returns how many spins the temp multiplier has left.
     * Pre-condition: none
     * Post-condition: returns tempMultiplierSpinsLeft as an int
     */
    public int getTempMultiplierSpinsLeft() { 
        return tempMultiplierSpinsLeft; 
    }

    /**
     * Returns the current temporary multiplier value.
     * Pre-condition: none
     * Post-condition: returns tempMultiplier as a double
     */
    public double getTempMultiplier() { 
        return tempMultiplier; 
    }

    /**
     * Returns the current permanent multiplier value.
     * Pre-condition: none
     * Post-condition: returns permMultiplier as a double
     */
    public double getPermMultiplier() { 
        return permMultiplier; 
    }
    public int getPermMultiplierRoundsLeft() { return permMultiplierRoundsLeft; }

    // Mutators (Setters)
    /**
     * Sets the player's bet amount.
     * Pre-condition: bet must be one of [1,2,5,10,25,50,100] and <= coins
     * Post-condition: currentBet is updated
     */
    public void setBet(int bet) {
        if (bet <= coins) {
            this.currentBet = bet;
        }
    }

    /**
     * Sets whether the player has an insurance policy.
     * Pre-condition: none
     * Post-condition: hasInsurance is updated
     */
    public void setInsurance(boolean value) { 
        this.hasInsurance = value; 
    }

    /**
     * Activates frenzy mode for 60 seconds.
     * Pre-condition: hasFrenzy must be false (one per round)
     * Post-condition: hasFrenzy is true, frenzySecondsLeft set to 60
     */
    public void activateFrenzy() {
        if (!hasFrenzy) {
            hasFrenzy = true;
            frenzySecondsLeft = 60;
        }
    }

    /**
     * Extends frenzy mode by 15 seconds.
     * Pre-condition: hasFrenzy must be true
     * Post-condition: frenzySecondsLeft increases by 15
     */
    public void extendFrenzy() {
        if (hasFrenzy) {
            frenzySecondsLeft += 15;
        }
    }

    /**
     * Decreases frenzy timer by 1 second. Deactivates if time runs out.
     * Pre-condition: hasFrenzy is true
     * Post-condition: frenzySecondsLeft decremented, hasFrenzy set false if 0
     */
    public void tickFrenzy() {
        if (hasFrenzy) {
            frenzySecondsLeft--;
            if (frenzySecondsLeft <= 0) {
                hasFrenzy = false;
                frenzySecondsLeft = 0;
            }
        }
    }

    /**
     * Applies a temporary multiplier for a given number of spins.
     * Pre-condition: spins > 0, multiplier > 1.0
     * Post-condition: tempMultiplier and tempMultiplierSpinsLeft are set
     */
    public void applyTempMultiplier(double multiplier, int spins) {
        this.tempMultiplier = multiplier;
        this.tempMultiplierSpinsLeft = spins;
    }

    /**
     * Applies a permanent multiplier lasting current round + 2 more.
     * Pre-condition: multiplier > 1.0
     * Post-condition: permMultiplier is  set
     */
    public void applyPermMultiplier(double multiplier) {
    this.permMultiplier = multiplier;
    this.permMultiplierRoundsLeft = 3;
    }

    // Game Actions

    /**
     * Deducts the current bet from coins and reduces spins by 1.
     * Pre-condition: coins >= currentBet, spinsLeft > 0
     * Post-condition: coins reduced by bet, spinsLeft reduced by 1
     */
    public void spin() {
        if (coins >= currentBet && spinsLeft > 0) {
            coins -= currentBet;
            spinsLeft--;
        }
    }

    /**
     * Adds a payout amount to the player's score and coins.
     * Pre-condition: amount >= 0
     * Post-condition: tickets and coins both increase by amount
     */
    public void applyPayout(int amount) {
        coins += amount;
        consecutiveMisses = 0;
        if (tempMultiplierSpinsLeft > 0) {
            tempMultiplierSpinsLeft--;
            if (tempMultiplierSpinsLeft == 0) {
                tempMultiplier = 1.0;
            }
        }
    }

    /**
     * Records a missed spin (no winning combination).
     * Pre-condition: none
     * Post-condition: consecutiveMisses incremented by 1
     */
    public void recordMiss() {
        consecutiveMisses++;
        if (tempMultiplierSpinsLeft > 0) {
            tempMultiplierSpinsLeft--;
            if (tempMultiplierSpinsLeft == 0) {
                tempMultiplier = 1.0;
            }
        }
    }

    /**
     * Adds an upgrade name to the player's upgrade list.
     * Pre-condition: upgradeName is not null
     * Post-condition: upgradeName added to upgrades list
     */
    public void addUpgrade(String upgradeName) {
        upgrades.add(upgradeName);
    }

    /**
     * Deducts a cost from the player's coins for a shop purchase.
     * Pre-condition: cost >= 0, coins >= cost
     * Post-condition: coins reduced by cost
     */
    public void purchase(int cost) {
        if (coins >= cost) {
            coins -= cost;
        }
    }
    /**
 * Deducts tickets for a shop purchase.
 * Pre-condition: cost >= 0, tickets >= cost
 * Post-condition: tickets reduced by cost
 */
public void spendTickets(int cost) {
    if (tickets >= cost) {
        tickets -= cost;
    }
}

    /**
     * Advances the player to the next round.
     * Resets spins and tickets, applies round bonus, ticks perm multiplier.
     * Pre-condition: currentRound < TOTAL_ROUNDS
     * Post-condition: round incremented, spins reset, bonus coins added
     */
    public void advanceRound() {
        currentRound++;
        spinsLeft = MAX_SPINS;
        hasFrenzy = false;
        frenzySecondsLeft = 0;

        // Add bonus coins based on which round was just completed
        int completedRound = currentRound - 1;
        if (completedRound >= 1 && completedRound <= 3) {
            coins += 7;
            tickets += 2;
        } else if (completedRound >= 4 && completedRound <= 6) {
            coins += 10;
            tickets += 3;
        } else if (completedRound >= 7 && completedRound <= 9) {
            coins += 13;
            tickets += 4;
        }

        // Tick down permanent multiplier
        if (permMultiplierRoundsLeft > 0) {
            permMultiplierRoundsLeft--;
            if (permMultiplierRoundsLeft == 0) {
                permMultiplier = 1.0;
            }
        }
    }

    /**
     * Checks whether the player has lost the game.
     * Pre-condition: none
     * Post-condition: returns true if coins = 0 AND spinsLeft = 0
     */
    public boolean isGameOver() {
        return coins <= 0 && spinsLeft <= 0;
    }

    /**
     * Resets the player completely for a new game.
     * Pre-condition: none
     * Post-condition: all fields restored to starting values
     */
    public void resetGame() {
        coins = STARTING_COINS;
        tickets = 2;
        spinsLeft = MAX_SPINS;
        currentRound = 1;
        currentBet = 1;
        upgrades.clear();
        consecutiveMisses = 0;
        hasInsurance = false;
        hasFrenzy = false;
        frenzySecondsLeft = 0;
        tempMultiplierSpinsLeft = 0;
        tempMultiplier = 1.0;
        permMultiplier = 1.0;
        permMultiplierRoundsLeft = 0;
    }

    /**
     * Returns a summary string of the player's current state.
     * Pre-condition: none
     * Post-condition: returns formatted String with key stats
     */
    @Override
    public String toString() {
        return "Round: " + currentRound +
               " | Coins: " + coins +
               " | Tickets: " + tickets +
               " | Spins Left: " + spinsLeft +
               " | Bet: " + currentBet;
    }
}