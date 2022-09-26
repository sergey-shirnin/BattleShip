package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class User {

    String playerName;

    String[][] field;
    int[][][] myShipsCoords = new int[Ships.values().length][][];
    int[] myShipsHealth = new int[Ships.values().length];
    List<String> myShipsSank = new ArrayList<>(List.of("Ghost"));

    final String space = " ";
    Scanner scanner = new Scanner(System.in);

    String[] initiateShipPlace() {
        return scanner.nextLine().split(space);
    }

    String takeShot() {
        return scanner.nextLine();
    }

    String[] retryShipPlace() { return scanner.nextLine().split(space); }

    String retryShot() { return scanner.nextLine(); }

    void setName(String name) {
        this.playerName = name;
    }
}
