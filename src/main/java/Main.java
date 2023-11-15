import errors.CustomException;
import processor.OrderProcessor;
import processor.Result;
import textio.*;

import java.util.List;

public class Main {
    public static void Greetings() {
        System.out.println("Hello Playtech!");
    }

    public static void main(String[] args) throws CustomException {
        Greetings();
        TextReader reader = new TextReader();
        TextWriter write = new TextWriter();

        List<String> rawMatchData = reader.read("match_data.txt");
        List<String> rawPlayerData = reader.read("player_data.txt");

        Result result = new OrderProcessor().process(rawMatchData, rawPlayerData);
        write.write("result.txt", result.toString());
    }
}
