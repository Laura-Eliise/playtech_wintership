package processor;

import match.Match;
import player.Player;
import utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class Result {
    public final List<Player> players;
    public final List<Match> matches;

    public Result(List<Player> players, List<Match> matches) {
        this.players = players;
        this.matches = matches;
    }

    public String toString() {
        int casinoBalance = ListUtils.sumIntValues(matches, Match::getBalance);
        List<String> legalPlayers = new ArrayList<>();
        List<String> illegalPlayers = new ArrayList<>();

        for (Player player : players) {
            if (player.isACriminal()) {
                illegalPlayers.add(player.toString());
            } else {
                legalPlayers.add(player.toString());
            }
        }

        String illegalPlayersString = String.join("\n", illegalPlayers);
        String legalPlayersString = String.join("\n", legalPlayers);

        return String.format("%s\n\n%s\n\n%s", legalPlayersString, illegalPlayersString, casinoBalance);
    }
}
