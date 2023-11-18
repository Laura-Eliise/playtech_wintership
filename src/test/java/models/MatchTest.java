package models;

import org.junit.jupiter.api.Test;
import utils.Pair;

import static org.junit.jupiter.api.Assertions.*;
import static utils.ListUtils.sumIntValues;

class MatchTest {
    private final String match_raw_data_1 = "abae2255-4255-4304-8589-737cdff61640,1.45,0.75,A";
    private final String player_raw_data_1 = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,DEPOSIT,,4000,";

    @Test
    void classBuildTest() {
        Match match = new Match(match_raw_data_1);
        assertEquals("abae2255-4255-4304-8589-737cdff61640 1.45/0.75 result:A balance:[] blacklisted:[]", match.toString());
    }

    @Test
    void validBettingTest() {
        Match match = new Match(match_raw_data_1);
        Player player = new Player(player_raw_data_1);
        player.incrementBalance(4000);
        assertDoesNotThrow(() -> match.bet(player, 20, MatchResult.B));
    }

    @Test
    void bettingTwiceTest() {
        Match match = new Match(match_raw_data_1);
        Player player = new Player(player_raw_data_1);
        player.incrementBalance(4000);
        assertDoesNotThrow(() -> match.bet(player, 20, MatchResult.B));
        assertThrows(IllegalArgumentException.class, () -> match.bet(player, 20, MatchResult.B));
    }
    @Test
    void negativeBetTest() {
        Match match = new Match(match_raw_data_1);
        Player player = new Player(player_raw_data_1);
        player.incrementBalance(4000);
        assertThrows(IllegalArgumentException.class, () -> match.bet(player, -20, MatchResult.B));
    }
    @Test
    void overBudgetBetTest() {
        Match match = new Match(match_raw_data_1);
        Player player = new Player(player_raw_data_1);
        player.incrementBalance(4000);
        assertThrows(IllegalArgumentException.class, () -> match.bet(player, 4001, MatchResult.B));
    }
    @Test
    void multipleBetTest() {
        String player_raw_data_2 = "4925ac98-833b-454b-9342-13ed3dfd3ccf,DEPOSIT,,100,";
        String player_raw_data_3 = "423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR,DEPOSIT,,700,";

        Match match = new Match(match_raw_data_1);
        Player player1 = new Player(player_raw_data_1);
        Player player2 = new Player(player_raw_data_2);
        Player player3 = new Player(player_raw_data_3);
        player1.incrementBalance(4000);
        player2.incrementBalance(50);
        player3.incrementBalance(700);

        assertDoesNotThrow(() -> match.bet(player1, 250, MatchResult.B));
        assertDoesNotThrow(() -> match.bet(player2, 50, MatchResult.A));
        assertDoesNotThrow(() -> match.bet(player3, 110, MatchResult.A));

        int matchBalance = sumIntValues(match.getBalance(), Pair::getSecond);
        assertEquals(250-(int) (50*1.45)-(int) (110*1.45), matchBalance);

        assertThrows(IllegalArgumentException.class, () -> match.bet(player1, 200, MatchResult.B));
        assertThrows(IllegalArgumentException.class, () -> match.bet(player2, 50, MatchResult.A));
        assertThrows(IllegalArgumentException.class, () -> match.bet(player3, 110, MatchResult.A));
    }
}