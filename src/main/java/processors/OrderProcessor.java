package processors;

import models.Match;
import models.MatchResult;
import models.Player;
import utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * The OrderProcessor class is responsible for processing raw match and player data.
 * The result is written
 * <p>
 * This class provides methods to process raw data, create player and match objects,
 * execute player orders, and return a Result object containing the final state.
 *
 * @see Match
 * @see Player
 * @see Result
 */
public class OrderProcessor {
    /**
     * Processes player bets and transactions.
     * <p>
     * The method takes raw match data and raw player data as input, creates match objects,
     * and executes orders for each player. The result is encapsulated in a Result object.
     * <p>
     * Raw Match Data Format:
     * Each element in rawMatchData represents raw data for a match.
     * The data format for each match should follow the Match class constructor format:
     * <pre>
     *     "matchID,winRateA,winRateB,matchResult"
     *     "Jordan,1.5,0.8,A"
     * </pre>
     * <p>
     * Raw Player Data Format:
     * Each element in rawPlayerData represents raw data for a player's order.
     * The data format for each player's order should follow the handleOrder method format:
     * <pre>
     *     "playerID,operation,matchId,amount,betPlacedSide"
     *     "Kakavas,DEPOSIT,,100,"
     *     "Watanabe,BET,Match1,50,A").
     * </pre>
     *
     * @param rawMatchData  the unprocessed matches
     * @param rawPlayerData the unprocessed player orders
     * @return a Result object containing the final state of the players and matches.
     * @throws IllegalArgumentException if there is an issue parsing match data or executing player orders
     * @throws NumberFormatException    if there is an issue parsing player data
     */
    public Result process(List<String> rawMatchData, List<String> rawPlayerData) {
        List<Player> players = new ArrayList<>();
        List<Match> matches = new ArrayList<>();

        for (String matchData : rawMatchData) {
            Match match = new Match(matchData);
            matches.add(match);
        }

        for (String playerData : rawPlayerData) {
            String[] order = playerData.split(",");
            Player player = getOrAddPlayer(players, order[0]);
            if (player.isNotACriminal()) {
               handleOrder(order, player, matches);
            }
        }

        return new Result(players, matches);
    }

    /**
     * Handles an order for a player.
     * <p>
     * The order array should have the following format:
     * <pre>
     * order[0]: Player ID
     * order[1]: Order Type ("DEPOSIT", "WITHDRAW" or "BET")
     * order[2]: [Optional] Match ID (for "BET" operation)
     * order[3]: Amount (For all three operations)
     * order[4]: [Optional] Bet result ("A", "B", or "DRAW" for "BET" operation)
     * </pre>
     * <p>
     * The method performs the specified operation on the player's balance or places a bet on a match.
     * In case of an illegal operation or an exception during processing, the player's illegalOperation
     * field is set, and the exception is rethrown.
     *
     * @param order   the array representing the order details
     * @param player  the player for whom the operation is performed
     * @param matches the list of matches in which the player can place bets
     * @throws IllegalArgumentException  if the order format is incorrect or an illegal operation occurs
     * @throws NumberFormatException     if there is an issue parsing integer values from the order
     * @throws IndexOutOfBoundsException if the match ID in the order does not correspond to any match in the list
     */
    private void handleOrder(String[] order, Player player, List<Match> matches) {
        try {
            switch (order[1]) {
                case "DEPOSIT" -> player.incrementBalance(Integer.parseInt(order[3]));
                case "WITHDRAW" -> player.incrementBalance(-1 * Integer.parseInt(order[3]));
                case "BET" -> {
                        int index = ListUtils.findFirstIndex(matches, match -> order[2].equals(match.id));
                        matches.get(index).bet(player, Integer.parseInt(order[3]), MatchResult.fromString(order[4]));
                }
            }
        } catch (Exception e) {
            player.setIllegalOperation(String.join(",", order));
        }
    }

    /**
     * Retrieves a player with the specified UUID from the given list of players,
     * or adds a new player with the provided UUID if not found.
     *
     * @param players the list of players to search
     * @param uuid    the UUID of the player to retrieve or add
     * @return the existing player with the specified UUID or a newly added player
     */
    private Player getOrAddPlayer(List<Player> players, String uuid) {
        int index = ListUtils.findFirstIndex(players, player -> uuid.equals(player.id));
        if (index < 0) {
            players.add(new Player(uuid));
            index = players.size() - 1;
        }
        return players.get(index);
    }
}
