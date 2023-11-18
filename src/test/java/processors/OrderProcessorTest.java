package processors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import models.Match;
import models.Player;
import utils.Pair;

import static utils.ListUtils.sumIntValues;

class OrderProcessorTest {
    private final List<String> match_list_data = List.of(new String[]{
            "abae2255-4255-4304-8589-737cdff61640,1.45,0.75,A",
            "a3815c17-9def-4034-a21f-65369f6d4a56,4.34,0.23,B",
            "2b20e5bb-9a32-4d33-b304-a9c7000e6de9,0.54,1.85,DRAW",
    });
    private final List<String> player_list_data_deposit = List.of(new String[]{
            "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,DEPOSIT,,4000,",
            "4925ac98-833b-454b-9342-13ed3dfd3ccf,DEPOSIT,,100,",
            "423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR,DEPOSIT,,700,",
    });
    private final List<String> player_list_data_withdraw = List.of(new String[]{
            "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,WITHDRAW,,1100,",
            "4925ac98-833b-454b-9342-13ed3dfd3ccf,WITHDRAW,,10,",
            "423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR,WITHDRAW,,800,",
    });
    private final List<String> player_list_data_bet_1 = List.of(new String[]{
            "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,abae2255-4255-4304-8589-737cdff61640,300,B",
            "4925ac98-833b-454b-9342-13ed3dfd3ccf,BET,abae2255-4255-4304-8589-737cdff61640,40,A",
            "423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR,BET,abae2255-4255-4304-8589-737cdff61640,500,B",
    });
    private final List<String> player_list_data_bet_2 = List.of(new String[]{
            "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,a3815c17-9def-4034-a21f-65369f6d4a56,1000,B",
            "4925ac98-833b-454b-9342-13ed3dfd3ccf,BET,a3815c17-9def-4034-a21f-65369f6d4a56,50,A",
            "423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR,BET,2b20e5bb-9a32-4d33-b304-a9c7000e6de9,100,B",
    });
    private final List<String> player_list_data_bet_illegal = List.of(new String[]{
            "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,abae2255-4255-4304-8589-737cdff61640,300,DRAW",
            "4925ac98-833b-454b-9342-13ed3dfd3ccf,BET,a3815c17-9def-4034-a21f-65369f6d4a56,40,A",
            "4925ac98-833b-454b-9342-13ed3dfd3ccf,BET,a3815c17-9def-4034-a21f-65369f6d4a56,40,A",
            "423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR,BET,2b20e5bb-9a32-4d33-b304-a9c7000e6de9,800,B",
    });

    @Test
    public void depositTest() {
        Result result = new OrderProcessor().process(match_list_data, player_list_data_deposit);
        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int casinoTotal = sumIntValues(result.matches.stream()
                .map(Match::getBalance)
                .flatMap(List::stream)
                .toList(),
                Pair::getSecond
        );
        int playerTotal = sumIntValues(result.players, Player::getBalance);
        assertEquals(0, casinoTotal);
        assertEquals(4000+100+700, playerTotal);
    }
    @Test
    public void illegalWithdrawTest() {
        Result result = new OrderProcessor().process(match_list_data, player_list_data_withdraw);
        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int playerTotal = sumIntValues(result.players, Player::getBalance);
        assertEquals(0, playerTotal);

        for (Player player : result.players) {
            assertTrue(player.isACriminal());
        }
    }
    @Test
    public void depositAndWithdrawTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_withdraw);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);

        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int playerTotal = sumIntValues(result.players, Player::getBalance);
        assertEquals(4000+100+700-1100-10, playerTotal);

        for (Player player : result.players) {
            if ("423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR".equals(player.id)) {
                assertTrue(player.isACriminal());
            } else {
                assertFalse(player.isACriminal());
            }
        }
    }
    @Test
    public void legalBetTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_1);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);

        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int playerTotal = sumIntValues(result.players, Player::getBalance);
        int casinoTotal = sumIntValues(result.matches.stream()
                        .map(Match::getBalance)
                        .flatMap(List::stream)
                        .toList(),
                Pair::getSecond
        );
        assertEquals(300+500-((int) (40*1.45)), casinoTotal);
        assertEquals(4000+100+700-300-500+((int) (40*1.45)), playerTotal);

        for (Player player : result.players) {
            assertFalse(player.isACriminal());
        }
    }
    @Test
    public void illegalBetTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_illegal);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);

        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int playerTotal = sumIntValues(result.players, Player::getBalance);
        int casinoTotal = sumIntValues(result.matches.stream()
                        .map(Match::getBalance)
                        .flatMap(List::stream)
                        .toList(),
                Pair::getSecond
        );
        assertEquals(40, casinoTotal);
        assertEquals(4000+100+700-40, playerTotal);

        for (Player player : result.players) {
            assertTrue(player.isACriminal());
        }
    }
    @Test
    public void multipleBetTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_1);
        combinedPlayerOrders.addAll(player_list_data_bet_2);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);

        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int playerTotal = sumIntValues(result.players, Player::getBalance);
        int casinoTotal = sumIntValues(result.matches.stream()
                        .map(Match::getBalance)
                        .flatMap(List::stream)
                        .toList(),
                Pair::getSecond
        );
        assertEquals(300+500-((int) (40*1.45))-((int) (1000*0.23))+50, casinoTotal);
        assertEquals(4000+100+700-300-500+((int) (40*1.45))+((int) (1000*0.23))-50, playerTotal);

        for (Player player : result.players) {
            assertFalse(player.isACriminal());
        }
    }
    @Test
    public void AllThreeActionsTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_1);
        combinedPlayerOrders.addAll(player_list_data_bet_2);
        combinedPlayerOrders.addAll(player_list_data_withdraw);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);

        assertEquals(3, result.matches.size());
        assertEquals(3, result.players.size());

        int playerTotal = sumIntValues(result.players, Player::getBalance);
        int casinoTotal = sumIntValues(result.matches.stream()
                        .map(Match::getBalance)
                        .flatMap(List::stream)
                        .toList(),
                Pair::getSecond
        );
        assertEquals(300+500-((int) (40*1.45))-((int) (1000*0.23))+50, casinoTotal);
        assertEquals(4000+100+700-300-500+((int) (40*1.45))+((int) (1000*0.23))-50-1100-10, playerTotal);

        for (Player player : result.players) {
            if ("423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR".equals(player.id)) {
                assertTrue(player.isACriminal());
            } else {
                assertFalse(player.isACriminal());
            }
        }
    }
}