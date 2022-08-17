package battleship;

import java.util.Arrays;
import java.util.stream.IntStream;


public class Game {

    int gameSize;
    String boardFill;

    public Game(int gameSize, String boardFill) {
        this.gameSize = gameSize;
        this.boardFill = boardFill;
    }

    String[] colNames;
    Character[] rowNames;

    String[][] gameProgress;

    boolean startUp = true;
    final String space = " ";

    int numOfShips = Ships.values().length;
    int[][][] allShipsCoords = new int[numOfShips][][];

    public void setUpBoard() {

        this.gameProgress = new String[this.gameSize][this.gameSize];
        this.colNames = IntStream.rangeClosed(1, this.gameSize)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        this.rowNames = IntStream.range('A', 'A' + this.gameSize)
                .mapToObj(num -> (char) num)
                .toArray(Character[]::new);
        for (String[] row : this.gameProgress) {
            Arrays.setAll(row, i -> this.boardFill);
        }
    }

    public void printBoard() {

        if (startUp) {
            System.out.print(space.repeat(2));
            startUp = false;
        } else {
            System.out.println();
            System.out.print(space.repeat(2));
        }

        System.out.println(String.join(space, this.colNames));
        for (int i = 0; i < this.gameProgress.length; i++) {
            System.out.print(rowNames[i] + space);
            System.out.println(String.join(space, this.gameProgress[i]));
        }
    }


    public void placeShip(Ships ship) {

        UserAssist playerAssist = new UserAssist();

        int[][] shipCoords = playerAssist.buildShip(allShipsCoords, ship);

        // draw on board
        for (int[] xyPair : shipCoords) {
            int row = xyPair[0];
            int col = xyPair[1];
            this.gameProgress[row][col] = "O";
        }

        // add to all ships 3d array [[[x, y],[x, y],[x, y]], ..., ..., ... ...]
        allShipsCoords[ship.ordinal()] = shipCoords;
    }
}




