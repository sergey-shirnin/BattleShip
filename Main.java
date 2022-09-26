package battleship;


public class Main {

    public static void main(String[] args) {
        // primary game set up
        final int gameSize = 10;
        final String boardFill = "~";
        final String shipMark = "O";
        final String shipHitMark = "X";
        final String hitMsg = "You hit a ship!";
        final String shipMissMark = "M";
        final String missMsg = "You missed.";
        final String sankMsg = "You sank a %s ship! Specify a new target:";
        final String gameOverMsg = "You sank the last ship (%s). You won. Congratulations!";
        final String[] players = {"Player 1", "Player 2"};

        Game myGame = new Game(gameSize, boardFill, shipMark,
                shipHitMark, hitMsg, shipMissMark, missMsg, sankMsg, gameOverMsg);

        // set up players and their board
        for (String playerName : players) {
            myGame.setUp(playerName);
            for (Ships ship : Ships.values()) {
                myGame.placeShip(ship);
            }
        }

        // taking shots till all ships in sank list
        while (!Game.gameOver) {
            myGame.placeShot();
        }
    }
}
