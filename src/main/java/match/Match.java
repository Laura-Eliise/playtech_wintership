package match;

import player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * The `Match` class represents a betting game match, including its win and lose rates
 * and the match result.
 * <p>
 * This class allows players to place bets on the match, tracks the match result,
 * and updates player balances accordingly.
 */
public class Match {
    public final String id;
    /**
     * Win rate for side A.
     */
    private final double A;

    /**
     * Win rate for side B.
     */
    private final double B;

    /**
     * Match result (A won, B won, or Draw).
     */
    private final MatchResult result;

    /**
     * Profit the casino has earned.
     */
    private int balance = 0;

    /**
     * Players who have already bet on this match.
     */
    private final List<String> blacklisted;


    public int getBalance() {return balance;}

    /**
     * Places a bet on the match and updates player balances based on the result.
     *
     * @param player The player placing the bet.
     * @param bet    The amount of the bet.
     * @param side   The side on which the bet is placed.
     * @throws IllegalArgumentException If the bet amount is negative, the bet is on a match draw or the player has already bet on this match.
     */
    public void bet(Player player, int bet, MatchResult side) {
        validateBet(player, bet, side);

        if (side == result) {
            won(player, bet, side);
        } else if (result != MatchResult.DRAW) {
            lost(player, bet);
        } else {
            player.matchDraw();
        }
        blacklisted.add(player.id);
    }

    /**
     * Validates a bet operation by checking various conditions.
     * <p>
     * This method ensures that the bet amount is non-negative, the player has not already bet on the match,
     * the bet is not placed on a draw, and the bet amount does not exceed the player's account balance.
     *
     * @param player the player placing the bet
     * @param bet    the amount to bet
     * @param side   the side on which the bet is placed (A or B)
     * @throws IllegalArgumentException if any of the validation conditions are not met
     */
    private void validateBet(Player player, int bet, MatchResult side) {
        if (bet < 0 ) {
            throw new IllegalArgumentException("Bet amount can't be negative");
        }
        if (hasAlreadyBet(player)) {
            throw new IllegalArgumentException("This player has already bet on this match");
        }
        if (side == MatchResult.DRAW) {
            throw new IllegalArgumentException("Can't bet on a draw");
        }
        if (bet > player.getBalance()) {
            throw new IllegalArgumentException("Can't bet more than the account balance");
        }
    }

    /**
     * Awards the winnings to the player.
     *
     * @param player The player who won the match.
     * @param bet    The amount the winner bet.
     * @param side   The side on which the bet was placed.
     */
    private void won(Player player, int bet, MatchResult side) {
        int amountWon = (int) (bet * ((side == MatchResult.A) ? A : B));
        player.matchWin(amountWon);
        balance -= amountWon;
    }

    /**
     * Deducts the lost bet amount from a player's balance
     *
     * @param player The player who lost the match.
     * @param bet    The amount the player lost.
     */
    private void lost(Player player, int bet) {
        player.matchLose(bet);
        balance += bet;
    }

    /**
     * Checks if a player has already bet on this match.
     *
     * @param player The player to check.
     * @return True if the player has already bet, false otherwise.
     */
    public boolean hasAlreadyBet(Player player) {
        return blacklisted.contains(player.id);
    }

    @Override
    public String toString() {
        return String.format("%s %s/%s result:%s balance:%s blacklisted:%s", id, A, B, result, balance, blacklisted);
    }

    /**
     * Creates a new `Match` instance based on the provided data string.
     *
     * @param data The data string containing match details.
     */
    public Match(String data) {
        String[] array = data.split(",");
        id = array[0];
        A = Double.parseDouble(array[1]);
        B = Double.parseDouble(array[2]);
        result = MatchResult.fromString(array[3]);
        blacklisted = new ArrayList<>();
    }
}
