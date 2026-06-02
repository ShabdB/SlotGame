/**
 * Manages round progression, target coins, and win/loss conditions.
 * Controls the flow of the game from round to round.
 *
 * @author Raj Patel
 */
public class RoundManager {
    // Target coins Per Round
    private static final int[] ROUND_TARGETS = {
        0,    // index 0 unused (rounds start at 1)
        5,    // Round 1
        15,   // Round 2
        35,   // Round 3
        65,   // Round 4
        110,  // Round 5
        175,  // Round 6
        280,  // Round 7
        450,  // Round 8
        700,  // Round 9
        1100  // Round 10
    };

    // Fields
    private Player player;
    private PayoutCalculator payoutCalculator;
    private Reel[] reels;
    private Shop shop;
    private boolean gameOver;
    private boolean gameWon;

    // Constructor

    /**
     * Creates a RoundManager with all necessary game components.
     * Pre-condition: none
     * Post-condition: all components initialized, game ready to start
     */
    public RoundManager() {
        player           = new Player();
        payoutCalculator = new PayoutCalculator();
        reels            = new Reel[9];
        for (int i = 0; i < 9; i++) {
        reels[i]         = new Reel(); 
        }
        shop             = new Shop(reels);
        gameOver         = false;
        gameWon          = false;
    }
    /**
 * Creates a RoundManager with a preloaded player from a save file.
 * Pre-condition: loadedPlayer is not null
 * Post-condition: game state restored from saved player
 */
public RoundManager(Player loadedPlayer) {
    player           = loadedPlayer;
    payoutCalculator = new PayoutCalculator();
    reels            = new Reel[9];
    for (int i = 0; i < 9; i++) {
    reels[i]         = new Reel();
    }
    shop             = new Shop(reels);
    gameOver         = false;
    gameWon          = false;
}

    // Core Game Actions

    /**
     * Executes a single spin using the player's current bet.
     * Calculates payout, applies insurance if needed, checks round end.
     * Pre-condition: player has enough coins and spins remaining
     * Post-condition: player state updated based on spin result
     */
    public int executeSpin() {
        if (player.getCoins() < player.getCurrentBet()) {
            return -1;
        }
        if (player.getSpinsLeft() <= 0) {
            return -1;
        }

        // Deduct bet and spin
        player.spin();

        // Spin all 9 reels into a grid
        String[] grid = new String[9];
        for (int i = 0; i < 9; i++) {
         grid[i] = reels[i].spin();
        }
        // Calculate payout
        int payout = payoutCalculator.calculatePayout(grid,
                                                player.getCurrentBet(),
                                                player);

        // Check insurance trigger if no win
        if (payout == 0 && payoutCalculator.shouldInsuranceTrigger(player)) {
            payout = payoutCalculator.getInsurancePayout(player.getCurrentBet());
            player.applyPayout(payout);
        } else if (payout > 0) {
            player.applyPayout(payout);
        }

        // Check if round or game is over
        checkRoundEnd();

        return payout;
    }

    /**
     * Checks whether the current round has ended via win or loss.
     * Pre-condition: player state is current
     * Post-condition: gameOver or gameWon flags set if conditions met
     */
  public void checkRoundEnd() {
    int round = player.getCurrentRound();
    boolean metTarget = player.getCoins() >= getTargetCoins(round);

    // Win condition — final round met
    if (metTarget && round == Player.TOTAL_ROUNDS) {
        gameWon = true;
        return;
    }

    // Loss — out of spins and didn't meet target
    if (player.getSpinsLeft() <= 0 && !metTarget) {
        gameOver = true;
        return;
    }

    // Loss — out of coins, can't spin, and didn't meet target
    if (player.getCoins() <= 0 && !metTarget) {
        gameOver = true;
        return;
    }
}

    /**
     * Advances the game to the next round after a win.
     * Resets reels, restocks shop, and updates player state.
     * Pre-condition: player has met the target Coins for current round
     * Post-condition: player moved to next round, reels and shop reset
     */
    public void advanceToNextRound() {
        player.advanceRound();

        // Reset reel odds for the new round
        for (Reel reel : reels) {
            reel.resetOdds();
        }

        // Restock the shop
        shop = new Shop(reels);
    }

    /**
     * Purchases an upgrade from the shop by index.
     * Pre-condition: index is valid, player has enough coins
     * Post-condition: upgrade applied if affordable and available
     */
    public boolean purchaseUpgrade(int index) {
        return shop.purchase(index, player);
    }

    /**
     * Fully resets the game to starting state.
     * Pre-condition: none
     * Post-condition: all components restored to initial values
     */
    public void resetGame() {
        player.resetGame();
        for (Reel reel : reels) {
            reel.resetOdds();
        }
        shop     = new Shop(reels);
        gameOver = false;
        gameWon  = false;
    }

    // Round Info
    /**
     * Returns the target Coins for a given round number.
     * Pre-condition: round is between 1 and 10 inclusive
     * Post-condition: returns target Coins as int
     */
    public int getTargetCoins(int round) {
        if (round < 1 || round > Player.TOTAL_ROUNDS) {
            return 0;
        }
        return ROUND_TARGETS[round];
    }

    /**
     * Returns the target Coins for the current round.
     * Pre-condition: none
     * Post-condition: returns current round target as int
     */
    public int getCurrentTargetCoins() {
        return getTargetCoins(player.getCurrentRound());
    }

    /**
     * Returns whether the player has met the current round target.
     * Pre-condition: none
     * Post-condition: returns true if Coins >= target
     */
    public boolean hasMetTarget() {
        return player.getCoins() >= getCurrentTargetCoins();
    }

    // Accessors

    /**
     * Returns the player object.
     * Pre-condition: none
     * Post-condition: returns player as Player object
     */
    public Player getPlayer() { return player; }

    /**
     * Returns the shop object.
     * Pre-condition: none
     * Post-condition: returns shop as Shop object
     */
    public Shop getShop() { return shop; }

    /**
     * Returns the reels array.
     * Pre-condition: none
     * Post-condition: returns reels as Reel array
     */
    public Reel[] getReels() { return reels; }

    /**
     * Returns the payout calculator object.
     * Pre-condition: none
     * Post-condition: returns payoutCalculator as PayoutCalculator object
     */
    public PayoutCalculator getPayoutCalculator() { return payoutCalculator; }

    /**
     * Returns whether the game is over due to a loss.
     * Pre-condition: none
     * Post-condition: returns gameOver as boolean
     */
    public boolean isGameOver() { return gameOver; }

    /**
     * Returns whether the player has won the entire game.
     * Pre-condition: none
     * Post-condition: returns gameWon as boolean
     */
    public boolean isGameWon() { return gameWon; }

    /**
     * Returns the last spin result symbols as a String array.
     * Pre-condition: reels array has 3 Reel objects
     * Post-condition: returns array of 3 symbol strings
     */
    public String getLastSpinResult(String[] grid) {
    return payoutCalculator.getResultDescription(grid);
    }
}