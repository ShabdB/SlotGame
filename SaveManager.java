import java.io.*;
import java.util.ArrayList;

/**
 * Handles saving and loading the game state to and from a text file.
 * Writes player data to savegame.txt and reads it back on load.
 *
 * @author Shabd Bhola
 */
public class SaveManager {

    // -------------------------------------------------------
    // Constants
    // -------------------------------------------------------
    private static final String SAVE_FILE = "savegame.txt";

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a SaveManager instance.
     * Pre-condition: none
     * Post-condition: SaveManager is ready to save and load
     */
    public SaveManager() {}

    // -------------------------------------------------------
    // Save
    // -------------------------------------------------------

    /**
     * Saves the current player state to savegame.txt.
     * Pre-condition: player is not null
     * Post-condition: player data written to file, returns true if successful
     */
    public boolean saveGame(Player player) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(SAVE_FILE));

            writer.println(player.getCoins());
            writer.println(player.getScore());
            writer.println(player.getSpinsLeft());
            writer.println(player.getCurrentRound());
            writer.println(player.getCurrentBet());
            writer.println(player.getConsecutiveMisses());
            writer.println(player.hasInsurance());
            writer.println(player.hasFrenzy());
            writer.println(player.getFrenzySecondsLeft());
            writer.println(player.getTempMultiplierSpinsLeft());
            writer.println(player.getTempMultiplier());
            writer.println(player.getPermMultiplier());
            writer.println(player.getPermMultiplierRoundsLeft());

            // Save upgrades list
            ArrayList<String> upgrades = player.getUpgrades();
            writer.println(upgrades.size());
            for (String upgrade : upgrades) {
                writer.println(upgrade);
            }

            writer.close();
            return true;

        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------
    // Load
    // -------------------------------------------------------

    /**
     * Loads a saved game state from savegame.txt into a Player object.
     * Pre-condition: savegame.txt exists and is not corrupted
     * Post-condition: returns a Player with restored state, or null if failed
     */
    public Player loadGame() {
        try {
            BufferedReader reader = new BufferedReader(
                new FileReader(SAVE_FILE)
            );

            Player player = new Player();

            int coins              = Integer.parseInt(reader.readLine().trim());
            int score              = Integer.parseInt(reader.readLine().trim());
            int spinsLeft          = Integer.parseInt(reader.readLine().trim());
            int currentRound       = Integer.parseInt(reader.readLine().trim());
            int currentBet         = Integer.parseInt(reader.readLine().trim());
            int consecutiveMisses  = Integer.parseInt(reader.readLine().trim());
            boolean hasInsurance   = Boolean.parseBoolean(reader.readLine().trim());
            boolean hasFrenzy      = Boolean.parseBoolean(reader.readLine().trim());
            int frenzySeconds      = Integer.parseInt(reader.readLine().trim());
            int tempSpins          = Integer.parseInt(reader.readLine().trim());
            double tempMult        = Double.parseDouble(reader.readLine().trim());
            double permMult        = Double.parseDouble(reader.readLine().trim());
            int permRounds         = Integer.parseInt(reader.readLine().trim());

            // Restore upgrades list
            int upgradeCount = Integer.parseInt(reader.readLine().trim());
            for (int i = 0; i < upgradeCount; i++) {
                player.addUpgrade(reader.readLine().trim());
            }

            reader.close();

            // Apply all loaded values to player
            restorePlayer(player, coins, score, spinsLeft, currentRound,
                          currentBet, consecutiveMisses, hasInsurance,
                          hasFrenzy, frenzySeconds, tempSpins,
                          tempMult, permMult, permRounds);

            return player;

        } catch (FileNotFoundException e) {
            System.out.println("No save file found.");
            return null;
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Save file corrupted: " + e.getMessage());
            return null;
        }
    }

    // -------------------------------------------------------
    // Restore Helper
    // -------------------------------------------------------

    /**
     * Restores all numeric and boolean fields to a Player object.
     * Pre-condition: player is not null, all values are valid
     * Post-condition: player fields set to loaded values
     */
    private void restorePlayer(Player player, int coins, int score,
                                int spinsLeft, int currentRound,
                                int currentBet, int consecutiveMisses,
                                boolean hasInsurance, boolean hasFrenzy,
                                int frenzySeconds, int tempSpins,
                                double tempMult, double permMult,
                                int permRounds) {
        // Use repeated game actions to restore state
        player.resetGame();

        // Restore coins by adding difference from starting amount
        int diff = coins - Player.STARTING_COINS;
        if (diff > 0) {
            player.applyPayout(diff);
        }

        // Restore round by advancing
        for (int i = 1; i < currentRound; i++) {
            player.advanceRound();
        }

        // Restore remaining fields
        player.setBet(currentBet);
        player.setInsurance(hasInsurance);

        if (hasFrenzy) {
            player.activateFrenzy();
        }

        if (tempMult > 1.0 && tempSpins > 0) {
            player.applyTempMultiplier(tempMult, tempSpins);
        }

        if (permMult > 1.0) {
            player.applyPermMultiplier(permMult);
        }
    }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    /**
     * Checks whether a save file currently exists.
     * Pre-condition: none
     * Post-condition: returns true if savegame.txt exists
     */
    public boolean saveExists() {
        File file = new File(SAVE_FILE);
        return file.exists();
    }

    /**
     * Deletes the save file if it exists.
     * Pre-condition: none
     * Post-condition: savegame.txt deleted if it existed
     */
    public void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}