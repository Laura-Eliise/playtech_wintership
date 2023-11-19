# Playtech Wintership Project: Text Better

Text Better is a simple betting game simulator that processes player bets and transactions.

## Table of Contents
- [Overview](#overview)
- [Project Structure](#project-structure)
- [How to run](#how-to-run)
- [Usage Example](#usage-example)
- [Authors](#authors)

## Overview

The Text Better is a betting game simulator that allows players to place bets on matches. It processes match and player data from text files, executes player orders, and calculates the final state of players and casino balance. The results are printed to a text file.

## Project Structure

- `src/main/java`: Contains the Java source code:
    - `modules`: Package for match and player related classes,
    - `processor`: Package for the order processing logic,
    - `textio`: Package for reading and writing text files,
    - `utils`: Package for utility classes.
- `src/main/resources`: Contains the match and player data.

- `src/test/java`: Contains unit tests for the project.

## How to run

To execute the project, follow these steps:

1. Clone the repository.
2. Open the project in your preferred Java IDE (e.g., IntelliJ IDEA).
3. Execute the program by running `src/main/java/Main.Java`.

## Usage Example

The project comes with an example input files. Simply add your own player and match data files into the resource folder and chane the file names in the Main class.

NEW_match_data.txt
```text
abae2255-4255-4304-8589-737cdff61640,1.45,0.75,A
a3815c17-9def-4034-a21f-65369f6d4a56,4.34,0.23,B
2b20e5bb-9a32-4d33-b304-a9c7000e6de9,0.54,1.85,DRAW
```

NEW_player_data.txt
```text
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,DEPOSIT,,4000,
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,BET,abae2255-4255-4304-8589-737cdff61640,500,B
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4,WITHDRAW,,200,
4925ac98-833b-454b-9342-13ed3dfd3ccf,WITHDRAW,,8093,
```

Main.java
```java
public class Main {
    public static void main(String[] args) {
        TextReader reader = new TextReader();
        TextWriter write = new TextWriter();
  
        List<String> rawMatchData = reader.read("NEW_match_data.txt"); 
        List<String> rawPlayerData = reader.read("NEW_player_data.txt");
  
        Result result = new OrderProcessor().process(rawMatchData, rawPlayerData);
        write.write("./src/main/java/NEW_result.txt", result.toString());
    }
}
```
NEW_result.txt
```text
163f23ed-e9a9-4e54-a5b1-4e1fc86f12f4 3300 0,00

4925ac98-833b-454b-9342-13ed3dfd3ccf WITHDRAW null 8093 null

500
```

## Authors

Laura-Eliise Marrandi