package battleship;

import java.util.Scanner;


public class User {

    final String space = " ";
    Scanner scanner = new Scanner(System.in);

    public String[] initiateShip() {
        return scanner.nextLine().split(space);
    }

    public String takeShot() {
        return scanner.nextLine();
    }

    public String[] placeAgain() {
        return scanner.nextLine().split(space);
    }

    public String shootAgain() {
        return scanner.nextLine();
    }
}
