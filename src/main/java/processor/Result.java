package processor;

import match.Match;
import player.Player;

import java.util.List;

public class Result {
    public final List<Player> players;
    public final List<Match> matches;

    public Result(List<Player> players, List<Match> matches) {
        this.players = players;
        this.matches = matches;
    }

    public String toString() {
        return "";
    }
}
