package processors;

import models.Match;
import models.Player;
import utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static utils.ListUtils.sumIntValues;

/**
 * The Result class represents the final result obtained from processing
 * player actions in the betting games.
 * <p>
 * It contains information about the players involved, the matches played,
 * and provides a string representation of the result.
 */
public class Result {
    /**
     * List of players involved in the betting games.
     */
    public final List<Player> players;

    /**
     * List of matches played in the betting games.
     */
    public final List<Match> matches;

    /**
     * Constructs a Result object with the given lists of players and matches.
     *
     * @param players List of players involved in the betting games.
     * @param matches List of matches played in the betting games.
     */
    public Result(List<Player> players, List<Match> matches) {
        this.players = players;
        this.matches = matches;
    }

    /**
     * Generates a string representation of the final result. It has
     * information about legal and illegal players and the casino's balance.
     *
     * @return A formatted string representing the final result.
     */
    public String toString() {
        List<String> legalPlayers = new ArrayList<>();
        List<String> illegalPlayers = new ArrayList<>();
        List<Pair<String, Integer>> allBetsPlaced = new ArrayList<>(
                matches.stream()
                        .map(Match::getBalance)
                        .flatMap(List::stream)
                        .toList()
        );

        for (Player player : players) {
            if (player.isACriminal()) {
                illegalPlayers.add(player.toString());
                allBetsPlaced.removeIf(pair -> pair.getFirst().equals(player.id));
            } else {
                legalPlayers.add(player.toString());
            }
        }

        String illegalPlayersString = String.join("\n", illegalPlayers);
        String legalPlayersString = String.join("\n", legalPlayers);
        int casinoBalance = sumIntValues(allBetsPlaced, Pair::getSecond);
        if (allBetsPlaced.isEmpty()) {
            return String.format("%s\n\n%s\n\nnull", legalPlayersString, illegalPlayersString);
        }
        return String.format("%s\n\n%s\n\n%s", legalPlayersString, illegalPlayersString, casinoBalance);
    }
}
