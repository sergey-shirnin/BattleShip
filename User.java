package battleship;


import java.util.Scanner;

public class User {

    final String space = " ";

    Scanner scanner = new Scanner(System.in);

    public String[] initiateShip(String shipType, int shipSize) {

        System.out.printf(
                "%nEnter the coordinates of the %s (%d cells):%n%n", shipType, shipSize);
        return scanner.nextLine().split(space);
    }

    public String takeShot() {
        System.out.printf(
                "%nTake a shot!%n%n");
        return scanner.nextLine();
    }

    public String[] placeAgain() {
        return scanner.nextLine().split(space);
    }

    public String shootAgain() {
        return scanner.nextLine();
    }
}
