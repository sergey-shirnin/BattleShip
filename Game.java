package battleship;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.stream.IntStream;


public class Game {

    UserAssist playerAssist = new UserAssist();
    User player1 = new User();
    User player2 = new User();

    final int gameSize;
    final String boardFill;
    final String shipMark;
    final String shipHitMark;
    final String hitMsg;
    final String shipMissMark;
    final String missMsg;
    final String sankMsg;
    final String gameOverMsg;

    public Game(int gameSize, String boardFill,
                String shipMark, String shipHitMark, String hitMsg,
                String shipMissMark, String missMsg, String sankMsg, String gameOverMsg) {

        this.gameSize = gameSize;
        this.boardFill = boardFill;
        this.shipMark = shipMark;
        this.shipHitMark = shipHitMark;
        this.hitMsg = hitMsg;
        this.shipMissMark = shipMissMark;
        this.missMsg = missMsg;
        this.sankMsg = sankMsg;
        this.gameOverMsg = gameOverMsg;
    }

    String[] colNames;
    Character[] rowNames;
    final String space = " ";

    int numOfShips = Ships.values().length;
    static boolean gameOver = false;

    //---------------------------------METHODS---------------------------------

    User player = player1;

    public void switchPlayer(User player) {
        this.player = player == player1 ? player2 : player1;
    }

    public static void enterToCont() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("%nPress Enter and pass the move to another player");
        scanner.nextLine();
    }

    public void printBoard(String mode, User player) {

        System.out.println();
        System.out.println(String.join(space, colNames));
        for (int i = 0; i < player.field.length; i++) {
            System.out.print(rowNames[i] + space);
            if (mode.equals("showShips")) {
                System.out.println(String.join(space, player.field[i]));
            } else if (mode.equals("hideShips")) {
                ArrayList<String> row = new ArrayList<>(Arrays.asList(player.field[i]));
                Collections.replaceAll(row, this.shipMark, this.boardFill);
                System.out.println(String.join(space, row));
            }
        }
    }

    public void setUp(String name) {
        player.setName(name);
        player.field = new String[gameSize][gameSize];
        this.colNames = IntStream.rangeClosed(1, this.gameSize)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
        this.rowNames = IntStream.range('A', 'A' + this.gameSize)
                .mapToObj(num -> (char) num)
                .toArray(Character[]::new);
        for (String[] row : player.field) {
            Arrays.fill(row, this.boardFill);
        }
        System.out.printf(
                "%n%s, place your ships on the game field\n", player.playerName);
        printBoard("showShips", player);
    }

    public void placeShip(Ships ship) {

        String shipType = ship.getType();
        int shipSize = ship.getSize();

        System.out.printf(
                "%nEnter the coordinates of the %s (%d cells):%n%n", shipType, shipSize);
        int[][] shipCoords = playerAssist.buildShip(player.myShipsCoords, shipType, shipSize);

        // draw on board
        for (int[] xyPair : shipCoords) {
            int row = xyPair[0], col = xyPair[1];
            player.field[row][col] = this.shipMark;
        }
        printBoard("showShips", player);

        // add ship coords to all ships 3d array [[[x, y],[x, y],[x, y]], ..., ..., ... ...]
        player.myShipsCoords[ship.ordinal()] = shipCoords;

        // add ship health(size) to all ships 1d array [5, 4, 3, ...]
        player.myShipsHealth[ship.ordinal()] = Ships.values()[ship.ordinal()].getSize();

        if (ship.ordinal() == Ships.values().length - 1) {
            switchPlayer(player);
            enterToCont();
        }

    }

    public void placeShot() {

        switchPlayer(player); // switch to opponent
        printBoard("hideShips", player);
        System.out.println("-".repeat(gameSize * 2));

        switchPlayer(player); // switch to self
        printBoard("showShips", player);
        System.out.printf("%n%s, it's your turn:%n%n", player.playerName);


        String[] shotOutcomes = new String[] { this.missMsg, this.hitMsg, this.sankMsg, this.gameOverMsg };
        String[] shotMarks = new String[] { this.shipMissMark, this.shipHitMark };

        int[] shotCoords = playerAssist.takeShot(gameSize);
        int row = shotCoords[0], col = shotCoords[1];

        switchPlayer(player); // switch to opponent
        int shotResult = 1 & Boolean.hashCode(Arrays.asList(this.shipMark, this.shipHitMark)
                                                    .contains(player.field[row][col])) >> 1;

        // update game progress @opponent
        player.field[row][col] = shotMarks[shotResult];

        // update ships health @opponent
        for (int i = 0; i < player.myShipsCoords.length; i++) {
            for (int j = 0; j < player.myShipsCoords[i].length; j++) {
                if (Arrays.equals(player.myShipsCoords[i][j], shotCoords)) {
                    player.myShipsCoords[i][j] = new int[] {-1, -1};
                    player.myShipsHealth[i]--;
                    break;
                }
            }
        }

        // update sank ships list @opponent
        for (int i = 0; i < player.myShipsHealth.length; i++) {
            if (player.myShipsHealth[i] == 0 && player.myShipsSank.size() != Ships.values().length + 1) {
                player.myShipsSank.add(Ships.values()[i].getType());
                player.myShipsHealth[i]--;
                shotResult++;
                break;
            }
        }

        // shot result msg display
        gameOver = player.myShipsSank.size() == numOfShips + 1; // sank ships list starts from 1
        if (gameOver) {  shotResult++; }
        System.out.printf("%n%s" + "%n".repeat(shotResult == 3 ? 1 : 0),
                String.format(shotOutcomes[shotResult], player.myShipsSank.get(player.myShipsSank.size() - 1)));
        if (!gameOver) {
            enterToCont();
        }
    }
}
