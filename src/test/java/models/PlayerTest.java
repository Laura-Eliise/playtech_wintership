package models;

import processors.OrderProcessor;
import processors.Result;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private final String match_raw_data_1 = "abae2255-4255-4304-8589-737cdff61640,1.45,0.75,A";
    private final String player_id = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4";
    private final String player_raw_data_deposit = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,DEPOSIT,,4000,";
    private final String player_raw_data_bet_1 = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,abae2255-4255-4304-8589-737cdff61640,1000,A";
    private final String player_raw_data_withdraw = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,WITHDRAW,,300,";

    @Test
    public void buildTest() {
        Player player = new Player(player_id);

        assertEquals(0, player.getBalance());
        assertEquals(player_id, player.id);
        assertTrue(player.isNotACriminal());
    }
    @Test
    public void depositTest() {
        Result result = new OrderProcessor().process(List.of(match_raw_data_1), List.of(player_raw_data_deposit));
        Player player = result.players.getFirst();

        assertEquals(player_id, player.id);
        assertEquals(4000, player.getBalance());
        assertTrue(player.isNotACriminal());
        assertEquals("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4000 null", player.toString());

    }
    @Test
    public void DepositAndWithdrawTest() {
        List<String> orders = List.of(player_raw_data_deposit, player_raw_data_withdraw);
        Result result = new OrderProcessor().process(List.of(match_raw_data_1), orders);
        Player player = result.players.getFirst();

        assertEquals(player_id, player.id);
        assertEquals(3700, player.getBalance());
        assertTrue(player.isNotACriminal());
        assertEquals("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 3700 null", player.toString());
    }
    @Test
    public void betTest() {
        List<String> matches = List.of(match_raw_data_1);
        List<String> orders = List.of(player_raw_data_deposit, player_raw_data_bet_1, player_raw_data_withdraw);
        Result result = new OrderProcessor().process(matches, orders);
        Player player = result.players.getFirst();

        assertEquals(player_id, player.id);
        assertEquals(4000 + ((int) (1000*1.45)) - 300, player.getBalance());
        assertTrue(player.isNotACriminal());
        assertEquals("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 5150 1,00", player.toString());
    }    @Test
    public void multipleBetTest() {
        String match_raw_data_2 = "12ae2255-4255-4304-8589-737cdff61634,1.45,0.75,A";
        String player_raw_data_bet_2 = "163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,12ae2255-4255-4304-8589-737cdff61634,1000,B";

        List<String> matches = List.of(match_raw_data_1, match_raw_data_2);
        List<String> orders = List.of(player_raw_data_deposit, player_raw_data_bet_1, player_raw_data_bet_2, player_raw_data_withdraw);
        Result result = new OrderProcessor().process(matches, orders);
        Player player = result.players.getFirst();

        assertEquals(player_id, player.id);
        assertEquals(4000 +((int) (1000*1.45)) -1000 -300, player.getBalance());
        assertTrue(player.isNotACriminal());
        assertEquals("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4150 0,50", player.toString());
    }
    @Test
    public void IllegalWithdrawTest() {
        List<String> orders = List.of(player_raw_data_withdraw);
        Result result = new OrderProcessor().process(List.of(match_raw_data_1), orders);
        Player player = result.players.getFirst();

        assertEquals(player_id, player.id);
        assertTrue(player.isACriminal());
        assertEquals("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 WITHDRAW null 300 null", player.toString());
    }
    @Test
    public void IllegalBetTest() {
        List<String> orders = List.of(player_raw_data_deposit, player_raw_data_bet_1, player_raw_data_bet_1);
        Result result = new OrderProcessor().process(List.of(match_raw_data_1), orders);
        Player player = result.players.getFirst();

        assertEquals(player_id, player.id);
        assertTrue(player.isACriminal());
        assertEquals("163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61640 1000 A", player.toString());
    }
}