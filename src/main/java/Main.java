import processors.OrderProcessor;
import processors.Result;
import textio.TextReader;
import textio.TextWriter;

import java.util.List;

public class Main {
    public static void Greetings() {
        System.out.println("Hello Playtech!");
    }

    public static void main(String[] args) {
        Greetings();
        TextReader reader = new TextReader();
        TextWriter write = new TextWriter();

        List<String> rawMatchData = reader.read("match_data.txt");
        List<String> rawPlayerData = reader.read("player_data.txt");

        Result result = new OrderProcessor().process(rawMatchData, rawPlayerData);
        write.write("./src/main/java/result.txt", result.toString());
    }
}
