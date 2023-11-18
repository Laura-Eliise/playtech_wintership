package processors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
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
    public void buildTest() {
        assertDoesNotThrow(() -> {
            Result result = new Result(new ArrayList<>(), new ArrayList<>());
            assertEquals("\n\n\n\nnull", result.toString());
        });
    }

    @Test
    public void depositTest() {
        Result result = new OrderProcessor().process(match_list_data, player_list_data_deposit);
        assertEquals("""
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 4000 null
            4925ac98-833b-454b-9342-13ed3dfd3ccf 100 null
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR 700 null



            null""",
            result.toString()
        );
    }
    @Test
    public void depositAndWithdrawTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_withdraw);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);
        assertEquals("""
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 2900 null
            4925ac98-833b-454b-9342-13ed3dfd3ccf 90 null
                        
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR WITHDRAW null 800 null
                        
            null""",
            result.toString()
        );
    }
    @Test
    public void betTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_1);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);
        assertEquals("""
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 3700 0,00
            4925ac98-833b-454b-9342-13ed3dfd3ccf 158 1,00
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR 200 0,00
                            
                            
                            
            742""",
            result.toString()
        );
    }
    @Test
    public void multipleBetTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_1);
        combinedPlayerOrders.addAll(player_list_data_bet_2);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);
        assertEquals("""
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 3930 0,50
            4925ac98-833b-454b-9342-13ed3dfd3ccf 108 0,50
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR 200 0,00
                        
                        
                        
            562""",
            result.toString()
        );
    }
    @Test
    public void allActionsTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_1);
        combinedPlayerOrders.addAll(player_list_data_bet_2);
        combinedPlayerOrders.addAll(player_list_data_withdraw);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);
        assertEquals("""
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 2830 0,50
            4925ac98-833b-454b-9342-13ed3dfd3ccf 98 0,50
                        
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR WITHDRAW null 800 null
                        
            62""",
            result.toString()
        );
    }
    @Test
    public void illegalBetTest() {
        List<String> combinedPlayerOrders = new ArrayList<>(player_list_data_deposit);
        combinedPlayerOrders.addAll(player_list_data_bet_illegal);
        Result result = new OrderProcessor().process(match_list_data, combinedPlayerOrders);
        assertEquals("""
                        
                        
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 BET abae2255-4255-4304-8589-737cdff61640 300 DRAW
            4925ac98-833b-454b-9342-13ed3dfd3ccf BET a3815c17-9def-4034-a21f-65369f6d4a56 40 A
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR BET 2b20e5bb-9a32-4d33-b304-a9c7000e6de9 800 B
                        
            null""",
            result.toString()
        );
    }
    @Test
    public void illegalWithdrawTest() {
        Result result = new OrderProcessor().process(match_list_data, player_list_data_withdraw);
        assertEquals("""
            
            
            163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 WITHDRAW null 1100 null
            4925ac98-833b-454b-9342-13ed3dfd3ccf WITHDRAW null 10 null
            423f23iu-l2hj-l4n6-l2k4-2H1fc86f18eR WITHDRAW null 800 null
            
            null""",
            result.toString()
        );
    }
}