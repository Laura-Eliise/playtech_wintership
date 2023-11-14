import processor.DataProcessor;
import processor.Result;
import textio.*;

public class Main {
    public static void Greetings() {
        System.out.println("Hello Playtech!");
    }

    public static void main(String[] args) {
        Greetings();
        TextReader reader = new TextReader();
        TextWriter write = new TextWriter();

        String rawMatchData = reader.read("../resources/match_data.txt");
        String rawPlayerData = reader.read("../resources/player_data.txt");

        Result result = new DataProcessor().process(rawMatchData, rawPlayerData);

        write.write("result.txt", result.toString());
    }
}
