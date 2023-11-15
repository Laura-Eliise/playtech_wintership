package player;

import match.Match;

/**
 * The `Player` class represents a participant in a betting game.
 * <p>
 * Each player has a unique uuid (`id`), an account balance (`balance`), and statistics
 * regarding the number of games won (`gamesWon`) and games played (`gamesPlayed`).
 * Additionally, the class tracks the latest illegal operations performed by the player.
 * <p>
 * Players can participate in matches, where they can win, lose, or draw. The balance is adjusted
 * accordingly after each match. The class provides methods to increment the balance, record a match win
 * or loss, check if a player is involved in illegal operations, and retrieve the player's current balance.
 * <p>
 * The class also throws an exception if an attempt is made to remove more than the account balance.
 *
 * @see Match
 */
public class Player {
    /**
     * The unique identifier for the player.
     */
    public final String id;

    /**
     * The current account balance of the player.
     */
    private int balance = 0;

    /**
     * The number of games won by the player.
     */
    private int gamesWon = 0;

    /**
     * The total number of games played by the player.
     */
    private int gamesPlayed = 0;

    /**
     * A string indicating any illegal operation performed by the player, or null if none.
     */
    public String illegalOperation = null;

    /**
     * Retrieves the current account balance of the player.
     *
     * @return the current balance of the player
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Increments the player's balance by the specified amount.
     *
     * @param amount the amount to increment the balance
     * @throws IllegalArgumentException if an attempt is made to remove more than the account balance
     */
    public void incrementBalance(int amount) {
        if (balance + amount < 0) {
            throw new IllegalArgumentException("Tried to remove more than the account balance");
        }
        balance += amount;
    }

    /**
     * Records a match win for the player, updating the balance and game statistics.
     *
     * @param amount the amount to increment the balance for the win
     */
    public void matchWin(int amount) {
        incrementBalance(amount);
        gamesWon++;
        gamesPlayed++;
    }

    /**
     * Records a match loss for the player, updating the balance and game statistics.
     *
     * @param amount the amount to decrement the balance for the loss
     */
    public void matchLose(int amount) {
        incrementBalance(-1 * amount);
        gamesPlayed++;
    }

    /**
     * Records a match draw for the player, updating the game statistics.
     */
    public void matchDraw() {
        gamesPlayed++;
    }

    /**
     * Checks if the player is involved in any illegal activities. (e.g. betting twice on a match)
     *
     * @return true if the player is involved in illegal operations, false otherwise
     */
    public boolean isACriminal() {
        return illegalOperation != null;
    }

    public Player(String uuid) {
        id = uuid;
    }
}
