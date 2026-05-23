import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Main GUI class for the slot machine game.
 * Handles all visual components, buttons, and user interaction.
 * Built using Java Swing.
 *
 * @author Shabd Bhola
 */
public class GameGUI extends JFrame {

    // -------------------------------------------------------
    // Colors and Fonts
    // -------------------------------------------------------
    private static final Color BG_COLOR       = new Color(15, 15, 30);
    private static final Color PANEL_COLOR     = new Color(25, 25, 50);
    private static final Color GOLD_COLOR      = new Color(255, 200, 50);
    private static final Color GREEN_COLOR     = new Color(50, 200, 100);
    private static final Color RED_COLOR       = new Color(220, 60, 60);
    private static final Color BUTTON_COLOR    = new Color(40, 40, 80);
    private static final Color REEL_COLOR      = new Color(30, 30, 60);
    private static final Color FRENZY_COLOR    = new Color(255, 100, 0);
    private static final Font  TITLE_FONT      = new Font("Arial", Font.BOLD, 28);
    private static final Font  REEL_FONT       = new Font("Arial", Font.BOLD, 36);
    private static final Font  LABEL_FONT      = new Font("Arial", Font.BOLD, 14);
    private static final Font  BUTTON_FONT     = new Font("Arial", Font.BOLD, 13);
    private static final Font  SMALL_FONT      = new Font("Arial", Font.PLAIN, 12);

    // -------------------------------------------------------
    // Game Components
    // -------------------------------------------------------
    private RoundManager roundManager;

    // -------------------------------------------------------
    // GUI Components
    // -------------------------------------------------------
    private JLabel titleLabel;
    private JLabel roundLabel;
    private JLabel coinsLabel;
    private JLabel scoreLabel;
    private JLabel targetLabel;
    private JLabel spinsLabel;
    private JLabel frenzyLabel;
    private JLabel resultLabel;
    private JLabel messageLabel;

    // Reel displays
    private JLabel[] reelLabels;
    private JPanel   reelPanel;

    // Bet buttons
    private JButton[] betButtons;
    private int[]     betValues = {1, 2, 5, 10, 25, 50, 100};
    private int       selectedBet = 1;

    // Action buttons
    // Action buttons
    private JButton spinButton;
    private JButton shopButton;
    private JButton nextRoundButton;
    private JButton restartButton;
    private JButton saveButton;
    private JButton loadButton;
    private SaveManager saveManager;

    // Frenzy timer
    private Timer frenzyTimer;

    // Symbol emoji map
    private static final String[] SYMBOLS = {
        "Cherry", "Lemon", "Orange", "Plum",
        "Bell", "Diamond", "Lucky7", "Wild"
    };
    private static final String[] EMOJIS = {
        "🍒", "🍋", "🍊", "🍑", "🔔", "💎", "7️⃣", "⭐"
    };

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates and displays the main game window.
     * Pre-condition: none
     * Post-condition: GUI is visible and game is ready to play
     */
    public GameGUI() {
        roundManager = new RoundManager();
        saveManager  = new SaveManager();
        setupWindow();
        setupComponents();
        updateDisplay();
        setVisible(true);
    }

    // -------------------------------------------------------
    // Window Setup
    // -------------------------------------------------------

    /**
     * Configures the main JFrame window properties.
     * Pre-condition: none
     * Post-condition: window is sized, centered, and styled
     */
    private void setupWindow() {
        setTitle("Roguelite Slot Machine");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_COLOR);
        setLayout(new BorderLayout(10, 10));
    }

    // -------------------------------------------------------
    // Component Setup
    // -------------------------------------------------------

    /**
     * Builds and arranges all GUI panels and components.
     * Pre-condition: window is initialized
     * Post-condition: all panels added to the main frame
     */
    private void setupComponents() {
        add(buildTopPanel(),    BorderLayout.NORTH);
        add(buildReelPanel(),   BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Builds the top HUD panel showing round, coins, score, target, spins.
     * Pre-condition: none
     * Post-condition: returns styled JPanel with all stat labels
     */
    private JPanel buildTopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(10, 10, 5, 10));

        // Title
        titleLabel = new JLabel("🎰 ROGUELITE SLOTS", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(GOLD_COLOR);
        panel.add(titleLabel);

        // Stats row
        JPanel statsPanel = new JPanel(new GridLayout(1, 5, 5, 0));
        statsPanel.setBackground(BG_COLOR);

        roundLabel  = makeStatLabel("Round: 1");
        coinsLabel  = makeStatLabel("Coins: 7");
        scoreLabel  = makeStatLabel("Score: 0");
        targetLabel = makeStatLabel("Target: 5");
        spinsLabel  = makeStatLabel("Spins: 7");

        statsPanel.add(roundLabel);
        statsPanel.add(coinsLabel);
        statsPanel.add(scoreLabel);
        statsPanel.add(targetLabel);
        statsPanel.add(spinsLabel);
        panel.add(statsPanel);

        return panel;
    }

    /**
     * Builds the center panel containing the three reels and result display.
     * Pre-condition: none
     * Post-condition: returns styled JPanel with reel labels and result label
     */
    private JPanel buildReelPanel() {
        JPanel wrapper = new JPanel(new BorderLayout(5, 10));
        wrapper.setBackground(BG_COLOR);
        wrapper.setBorder(new EmptyBorder(10, 40, 10, 40));

        // Reels
        reelPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        reelPanel.setBackground(BG_COLOR);
        reelLabels = new JLabel[3];

        for (int i = 0; i < 3; i++) {
            reelLabels[i] = new JLabel("🎰", SwingConstants.CENTER);
            reelLabels[i].setFont(REEL_FONT);
            reelLabels[i].setForeground(Color.WHITE);
            reelLabels[i].setOpaque(true);
            reelLabels[i].setBackground(REEL_COLOR);
            reelLabels[i].setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 2));
            reelLabels[i].setPreferredSize(new Dimension(160, 120));
            reelPanel.add(reelLabels[i]);
        }
        wrapper.add(reelPanel, BorderLayout.CENTER);

        // Result and frenzy labels
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 0, 4));
        infoPanel.setBackground(BG_COLOR);

        resultLabel = new JLabel("Press SPIN to play!", SwingConstants.CENTER);
        resultLabel.setFont(LABEL_FONT);
        resultLabel.setForeground(Color.WHITE);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(LABEL_FONT);
        messageLabel.setForeground(GREEN_COLOR);

        frenzyLabel = new JLabel("", SwingConstants.CENTER);
        frenzyLabel.setFont(LABEL_FONT);
        frenzyLabel.setForeground(FRENZY_COLOR);

        infoPanel.add(resultLabel);
        infoPanel.add(messageLabel);
        infoPanel.add(frenzyLabel);
        wrapper.add(infoPanel, BorderLayout.SOUTH);

        return wrapper;
    }

    /**
     * Builds the bottom panel with bet buttons and action buttons.
     * Pre-condition: none
     * Post-condition: returns styled JPanel with all interactive buttons
     */
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 5, 5));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));

        // Bet buttons row
        JPanel betPanel = new JPanel(new GridLayout(1, 7, 5, 0));
        betPanel.setBackground(BG_COLOR);
        betButtons = new JButton[betValues.length];

        for (int i = 0; i < betValues.length; i++) {
            final int betAmount = betValues[i];
            betButtons[i] = makeButton("" + betAmount);
            betButtons[i].addActionListener(e -> selectBet(betAmount));
            betPanel.add(betButtons[i]);
        }
        panel.add(betPanel);

        // Spin and shop buttons row
        JPanel actionPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        actionPanel.setBackground(BG_COLOR);

        spinButton = makeButton("🎰 SPIN");
        spinButton.setFont(new Font("Arial", Font.BOLD, 16));
        spinButton.setForeground(GOLD_COLOR);
        spinButton.addActionListener(e -> handleSpin());

        shopButton = makeButton("🛒 SHOP");
        shopButton.setFont(new Font("Arial", Font.BOLD, 16));
        shopButton.addActionListener(e -> openShop());

        actionPanel.add(spinButton);
        actionPanel.add(shopButton);
        panel.add(actionPanel);

        // Next round and restart buttons row
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        controlPanel.setBackground(BG_COLOR);

        nextRoundButton = makeButton("➡ NEXT ROUND");
        nextRoundButton.setEnabled(false);
        nextRoundButton.addActionListener(e -> handleNextRound());

        restartButton = makeButton("🔄 RESTART");
        restartButton.addActionListener(e -> handleRestart());

        controlPanel.add(nextRoundButton);
        controlPanel.add(restartButton);
        panel.add(controlPanel);

// Save and load buttons row
        JPanel savePanel = new JPanel(new GridLayout(1, 2, 10, 0));
        savePanel.setBackground(BG_COLOR);

        saveButton = makeButton("💾 SAVE GAME");
        saveButton.addActionListener(e -> handleSave());

        loadButton = makeButton("📂 LOAD GAME");
        loadButton.addActionListener(e -> handleLoad());

        savePanel.add(saveButton);
        savePanel.add(loadButton);
        panel.add(savePanel);

        return panel;
    }

    // -------------------------------------------------------
    // Game Logic Handlers
    // -------------------------------------------------------

    /**
     * Handles a spin button press. Executes spin and updates display.
     * Pre-condition: player has coins and spins remaining
     * Post-condition: reels updated, payout applied, display refreshed
     */
    private void handleSpin() {
        Player player = roundManager.getPlayer();

        if (player.getCoins() < selectedBet) {
            messageLabel.setText("Not enough coins to spin!");
            messageLabel.setForeground(RED_COLOR);
            return;
        }

        if (player.getSpinsLeft() <= 0) {
            messageLabel.setText("No spins remaining!");
            messageLabel.setForeground(RED_COLOR);
            return;
        }

        // Set bet and execute spin
        player.setBet(selectedBet);
        animateReels();
    }

    /**
     * Animates the reels spinning then shows the result.
     * Pre-condition: spin is valid
     * Post-condition: reels display final symbols after animation
     */
    private void animateReels() {
        spinButton.setEnabled(false);
        String[] spinSymbols = {"🍒","🍋","🍊","🍑","🔔","💎","7️⃣","⭐"};
        Timer animTimer = new Timer(80, null);
        final int[] ticks = {0};

        animTimer.addActionListener(e -> {
            ticks[0]++;
            for (JLabel reel : reelLabels) {
                int idx = (int)(Math.random() * spinSymbols.length);
                reel.setText(spinSymbols[idx]);
            }
            if (ticks[0] >= 12) {
                animTimer.stop();
                finishSpin();
            }
        });
        animTimer.start();
    }

    /**
     * Completes the spin, shows results, and updates game state.
     * Pre-condition: reel animation has finished
     * Post-condition: final symbols shown, payout applied, display updated
     */
    private void finishSpin() {
        // Get the actual spin result from RoundManager
        Reel[] reels = roundManager.getReels();
        String s1 = reels[0].spin();
        String s2 = reels[1].spin();
        String s3 = reels[2].spin();

        // Show symbols on reels
        reelLabels[0].setText(getEmoji(s1));
        reelLabels[1].setText(getEmoji(s2));
        reelLabels[2].setText(getEmoji(s3));

        // Calculate payout
        Player player = roundManager.getPlayer();
        player.spin();
        PayoutCalculator calc = roundManager.getPayoutCalculator();
        int payout = calc.calculatePayout(s1, s2, s3,
                                          player.getCurrentBet(), player);

        // Apply insurance if triggered
        if (payout == 0 && calc.shouldInsuranceTrigger(player)) {
            payout = calc.getInsurancePayout(player.getCurrentBet());
            player.applyPayout(payout);
            messageLabel.setText("🛡 Insurance triggered! +" + payout + " coins");
            messageLabel.setForeground(GREEN_COLOR);
        } else if (payout > 0) {
            player.applyPayout(payout);
            messageLabel.setText("WIN! +" + payout + " coins!");
            messageLabel.setForeground(GREEN_COLOR);
        } else {
            player.recordMiss();
            messageLabel.setText("No win this spin.");
            messageLabel.setForeground(Color.GRAY);
        }

        resultLabel.setText(calc.getResultDescription(s1, s2, s3));
        roundManager.checkRoundEnd();
        updateDisplay();
        spinButton.setEnabled(true);
    }

    /**
     * Handles selecting a bet amount from the bet buttons.
     * Pre-condition: betAmount is a valid bet value
     * Post-condition: selectedBet updated, buttons refreshed
     */
    private void selectBet(int betAmount) {
        selectedBet = betAmount;
        roundManager.getPlayer().setBet(betAmount);
        updateBetButtons();
    }

    /**
     * Handles the next round button press.
     * Pre-condition: player has met the current round target
     * Post-condition: game advances to next round, display updated
     */
    private void handleNextRound() {
        roundManager.advanceToNextRound();
        nextRoundButton.setEnabled(false);
        spinButton.setEnabled(true);
        messageLabel.setText("Round " + roundManager.getPlayer().getCurrentRound() + " — Good luck!");
        messageLabel.setForeground(GOLD_COLOR);
        resultLabel.setText("Press SPIN to play!");
        for (JLabel reel : reelLabels) {
            reel.setText("🎰");
        }
        updateDisplay();
    }

    /**
     * Handles the restart button press. Resets entire game.
     * Pre-condition: none
     * Post-condition: game fully reset to starting state
     */
    /**
 * Handles the save button press. Saves current game state to file.
 * Pre-condition: roundManager and player are initialized
 * Post-condition: game state written to savegame.txt
 */
private void handleSave() {
    boolean success = saveManager.saveGame(roundManager.getPlayer());
    if (success) {
        messageLabel.setText("💾 Game saved successfully!");
        messageLabel.setForeground(GREEN_COLOR);
    } else {
        messageLabel.setText("Error saving game.");
        messageLabel.setForeground(RED_COLOR);
    }
}

/**
 * Handles the load button press. Loads saved game state from file.
 * Pre-condition: savegame.txt exists
 * Post-condition: player state restored from file, display updated
 */
private void handleLoad() {
    if (!saveManager.saveExists()) {
        messageLabel.setText("No save file found.");
        messageLabel.setForeground(RED_COLOR);
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Load saved game? Current progress will be lost.",
        "Load Game",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        Player loadedPlayer = saveManager.loadGame();
        if (loadedPlayer != null) {
            roundManager = new RoundManager();
            messageLabel.setText("📂 Game loaded successfully!");
            messageLabel.setForeground(GREEN_COLOR);
            resultLabel.setText("Press SPIN to play!");
            for (JLabel reel : reelLabels) {
                reel.setText("🎰");
            }
            updateDisplay();
        } else {
            messageLabel.setText("Error loading save file.");
            messageLabel.setForeground(RED_COLOR);
        }
    }
}
    private void handleRestart() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to restart?",
            "Restart Game",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            roundManager.resetGame();
            selectedBet = 1;
            spinButton.setEnabled(true);
            nextRoundButton.setEnabled(false);
            messageLabel.setText("");
            resultLabel.setText("Press SPIN to play!");
            for (JLabel reel : reelLabels) {
                reel.setText("🎰");
            }
            if (frenzyTimer != null) frenzyTimer.stop();
            frenzyLabel.setText("");
            updateDisplay();
        }
    }

    /**
     * Opens the shop dialog for purchasing upgrades.
     * Pre-condition: none
     * Post-condition: shop dialog shown, purchases applied if made
     */
    private void openShop() {
        Shop shop = roundManager.getShop();
        Player player = roundManager.getPlayer();
        ArrayList<Upgrade> upgrades = shop.getUpgrades();

        String[] options = new String[upgrades.size() + 1];
        for (int i = 0; i < upgrades.size(); i++) {
            Upgrade u = upgrades.get(i);
            boolean affordable = shop.canAfford(player, u);
            boolean available  = shop.isAvailable(u, player);
            String status = (!available) ? " [UNAVAILABLE]" :
                            (!affordable) ? " [CAN'T AFFORD]" : "";
            options[i] = (i + 1) + ". " + u.getName() +
                         " (" + u.getCost() + " coins)" + status;
        }
        options[upgrades.size()] = "Close Shop";

        String choice = (String) JOptionPane.showInputDialog(
            this,
            "Your coins: " + player.getCoins() + "\nChoose an upgrade:",
            "🛒 Shop",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice != null && !choice.equals("Close Shop")) {
            for (int i = 0; i < upgrades.size(); i++) {
                if (choice.equals(options[i])) {
                    boolean success = roundManager.purchaseUpgrade(i);
                    if (success) {
                        messageLabel.setText("Purchased: " + upgrades.get(i).getName());
                        messageLabel.setForeground(GREEN_COLOR);

                        // Start frenzy timer if frenzy was just bought
                        if (upgrades.get(i) instanceof FrenzyMode) {
                            startFrenzyTimer();
                        }
                        if (upgrades.get(i) instanceof FrenzyExtension) {
                            // Timer already running, just extended
                        }
                    } else {
                        messageLabel.setText("Cannot purchase that item.");
                        messageLabel.setForeground(RED_COLOR);
                    }
                    updateDisplay();
                    break;
                }
            }
        }
    }

    /**
     * Starts the frenzy mode countdown timer.
     * Pre-condition: player has just activated frenzy mode
     * Post-condition: timer ticks every second, updates frenzy label
     */
    private void startFrenzyTimer() {
        if (frenzyTimer != null) frenzyTimer.stop();
        frenzyTimer = new Timer(1000, e -> {
            roundManager.getPlayer().tickFrenzy();
            updateFrenzyLabel();
            if (!roundManager.getPlayer().hasFrenzy()) {
                frenzyTimer.stop();
                frenzyLabel.setText("");
            }
        });
        frenzyTimer.start();
        updateFrenzyLabel();
    }

    // -------------------------------------------------------
    // Display Updates
    // -------------------------------------------------------

    /**
     * Updates all HUD labels and button states to reflect current game state.
     * Pre-condition: roundManager and player are initialized
     * Post-condition: all labels and buttons reflect current player state
     */
    private void updateDisplay() {
        Player player = roundManager.getPlayer();

        roundLabel.setText("Round: "  + player.getCurrentRound());
        coinsLabel.setText("Coins: "  + player.getCoins());
        scoreLabel.setText("Score: "  + player.getScore());
        targetLabel.setText("Target: " + roundManager.getCurrentTargetScore());
        spinsLabel.setText("Spins: "  + player.getSpinsLeft());

        updateBetButtons();
        updateFrenzyLabel();

        // Handle win conditions
        if (roundManager.isGameWon()) {
            spinButton.setEnabled(false);
            nextRoundButton.setEnabled(false);
            messageLabel.setText("🏆 YOU WIN! Congratulations!");
            messageLabel.setForeground(GOLD_COLOR);
            titleLabel.setText("🏆 WINNER!");
            titleLabel.setForeground(GOLD_COLOR);
        } else if (roundManager.isGameOver()) {
            spinButton.setEnabled(false);
            nextRoundButton.setEnabled(false);
            messageLabel.setText("💀 GAME OVER! Better luck next time.");
            messageLabel.setForeground(RED_COLOR);
            titleLabel.setText("💀 GAME OVER");
            titleLabel.setForeground(RED_COLOR);
        } else if (roundManager.hasMetTarget()) {
            spinButton.setEnabled(false);
            nextRoundButton.setEnabled(
                player.getCurrentRound() < Player.TOTAL_ROUNDS
            );
            messageLabel.setText("✅ Round complete! Visit shop or continue.");
            messageLabel.setForeground(GREEN_COLOR);
        }
    }

    /**
     * Updates bet buttons enabling/disabling based on player's coins.
     * Pre-condition: player is initialized, betButtons array is built
     * Post-condition: buttons disabled if player cannot afford that bet
     */
    private void updateBetButtons() {
        Player player = roundManager.getPlayer();
        for (int i = 0; i < betButtons.length; i++) {
            boolean canAfford = player.getCoins() >= betValues[i];
            betButtons[i].setEnabled(canAfford);
            if (betValues[i] == selectedBet && canAfford) {
                betButtons[i].setBackground(GOLD_COLOR);
                betButtons[i].setForeground(Color.BLACK);
            } else {
                betButtons[i].setBackground(BUTTON_COLOR);
                betButtons[i].setForeground(Color.WHITE);
            }
        }
    }

    /**
     * Updates the frenzy timer label display.
     * Pre-condition: player is initialized
     * Post-condition: frenzy label shows remaining seconds or empty string
     */
    private void updateFrenzyLabel() {
        Player player = roundManager.getPlayer();
        if (player.hasFrenzy()) {
            frenzyLabel.setText("🔥 FRENZY MODE: " +
                                player.getFrenzySecondsLeft() + "s remaining!");
        } else {
            frenzyLabel.setText("");
        }
    }

    // -------------------------------------------------------
    // Helper Methods
    // -------------------------------------------------------

    /**
     * Creates a styled JButton with consistent appearance.
     * Pre-condition: text is not null
     * Post-condition: returns a styled JButton
     */
    private JButton makeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(BUTTON_FONT);
        btn.setBackground(BUTTON_COLOR);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(true);
        btn.setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Creates a styled stat label for the HUD panel.
     * Pre-condition: text is not null
     * Post-condition: returns a styled JLabel
     */
    private JLabel makeStatLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(LABEL_FONT);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(PANEL_COLOR);
        label.setBorder(BorderFactory.createLineBorder(GOLD_COLOR, 1));
        return label;
    }

    /**
     * Returns the emoji string for a given symbol name.
     * Pre-condition: symbol is a valid non-null symbol string
     * Post-condition: returns matching emoji String
     */
    private String getEmoji(String symbol) {
        for (int i = 0; i < SYMBOLS.length; i++) {
            if (SYMBOLS[i].equals(symbol)) {
                return EMOJIS[i];
            }
        }
        return "❓";
    }

    // -------------------------------------------------------
    // Main Method
    // -------------------------------------------------------

    /**
     * Entry point for the game. Launches the GUI on the Swing thread.
     * Pre-condition: none
     * Post-condition: game window is displayed and ready to play
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameGUI());
    }
}