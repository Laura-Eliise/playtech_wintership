package processor;

import match.Match;
import player.Player;

import java.util.HashMap;

public class DataProcessor {
    /// Processes the player data and return the result
    public Result process(String rawMatchData, String rawPlayerData) {
        HashMap<String, Player> players = new HashMap<>();
        HashMap<String, Match> matches = new HashMap<>();

        // populate matches

        // loop through player data and process it

        return new Result(players, matches);
    }
}
