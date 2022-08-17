package battleship;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.IntStream;


public class Game {

    UserAssist playerAssist = new UserAssist();

    final int gameSize;
    final String boardFill;
    final String shipMark;
    final String shipHitMark;
    final String hitMsg;
    final String shipMissMark;
    final String missMsg;

    public Game(int gameSize, String boardFill,
                String shipMark,
                String shipHitMark, String hitMsg, String shipMissMark, String missMsg) {
        this.gameSize = gameSize;
        this.boardFill = boardFill;
        this.shipMark = shipMark;
        this.shipHitMark = shipHitMark;
        this.hitMsg = hitMsg;
        this.shipMissMark = shipMissMark;
        this.missMsg = missMsg;
    }

    String[] colNames;
    Character[] rowNames;

    String[][] gameProgress;

    boolean startUp = true;
    boolean gameStart = true;

    final String space = " ";

    int numOfShips = Ships.values().length;
    int[][][] allShipsCoords = new int[numOfShips][][];


    public void printBoard(String mode) {

        if (startUp) {
            System.out.print(space.repeat(2));
            startUp = false;
        } else {
            System.out.println();
            System.out.print(space.repeat(2));
        }

        System.out.println(String.join(space, colNames));
        for (int i = 0; i < gameProgress.length; i++) {
            System.out.print(rowNames[i] + space);
            if (mode.equals("showShips")) {
                System.out.println(String.join(space, gameProgress[i]));
            } else if (mode.equals("hideShips")) {
                ArrayList<String> row = new ArrayList<>(Arrays.asList(gameProgress[i]));
                Collections.replaceAll(row, this.shipMark, this.boardFill);
                System.out.println(String.join(space, row));
            }
        }
    }

    public void setUpBoard() {
        this.gameProgress = new String[this.gameSize][this.gameSize];
        this.colNames = IntStream.rangeClosed(1, this.gameSize)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        this.rowNames = IntStream.range('A', 'A' + this.gameSize)
                .mapToObj(num -> (char) num)
                .toArray(Character[]::new);
        for (String[] row : gameProgress) {
            Arrays.fill(row, this.boardFill);
        }
        printBoard("showShips");
    }


    public void placeShip(Ships ship) {

        int[][] shipCoords = playerAssist.buildShip(allShipsCoords, ship);

        // draw on board
        for (int[] xyPair : shipCoords) {
            int row = xyPair[0];
            int col = xyPair[1];
            gameProgress[row][col] = this.shipMark;
        }
        printBoard("showShips");

        // add to all ships 3d array [[[x, y],[x, y],[x, y]], ..., ..., ... ...]
        allShipsCoords[ship.ordinal()] = shipCoords;
    }

    public void placeShot() {
        if (gameStart) {
            System.out.printf("%nThe game starts!%n");
            gameStart = false;
            printBoard("hideShips");
        }

        String[] shotOutcomes = new String[] { this.missMsg, this.hitMsg };
        String[] shotMarks = new String[] { this.shipMissMark, this.shipHitMark };

        int[] shotCoords = playerAssist.takeShot(gameSize);
        int row = shotCoords[0];
        int col = shotCoords[1];

        int shotResult = 1 & Boolean.hashCode( gameProgress[row][col] == this.shipMark) >> 1;

        // draw on board
        gameProgress[row][col] = shotMarks[shotResult];
        printBoard("hideShips");

        // update all ships 3d array [[[-1, -1],[x, y],[x, y]], ..., ..., ... ...]
        for (int i = 0; i < allShipsCoords.length; i++) {
            for (int j = 0; j < allShipsCoords[i].length; j++) {
                if (Arrays.equals(allShipsCoords[i][j], shotCoords)) {
                    allShipsCoords[i][j] = new int[] {-1, -1};
                }
            }
        }

        System.out.printf("%nYou %s!%n",  shotOutcomes[shotResult]);
    }
}
