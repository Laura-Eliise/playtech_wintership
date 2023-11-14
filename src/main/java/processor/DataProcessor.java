package processor;

import match.Match;
import player.Player;

import java.util.HashMap;
import java.util.List;

public class DataProcessor {
    /// Processes the player data and return the result
    public Result process(List<String> rawMatchData, List<String> rawPlayerData) {
        HashMap<String, Player> players = new HashMap<>();
        HashMap<String, Match> matches = new HashMap<>();

        // populate matches
        for (String matchData : rawMatchData) {
            Match match = new Match(matchData);
            matches.put(match.id, match);
        }

        // loop through player data and process it

        return new Result(players, matches);
    }
}
