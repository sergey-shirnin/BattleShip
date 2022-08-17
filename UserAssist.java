package battleship;


public class UserAssist {

    final int unicodeA = 65;

    int[] shipRowRange;
    int[] shipColRange;

    User player = new User();

    public int[][] buildShip(int[][][] allShipsCoords, Ships ship) {

        int[][] shipCoords;

        String shipType = ship.getType();
        int shipSize = ship.getSize();

        String[] coords = player.initiateShip(shipType, shipSize);

        boolean validShip = false;
        do {
            // pre-fabricate ship per user entries
            shipRowRange = ShipBuildSupport.rowCoordsToRange(coords, unicodeA);
            shipColRange = ShipBuildSupport.colCoordsToRange(coords);
            shipCoords = ShipBuildSupport.rangesToCoords(shipRowRange, shipColRange);

            // check ship built is valid
            String cause = GameException.getPlacementExceptionCause(
                    shipRowRange, shipColRange, shipSize,
                    shipCoords, allShipsCoords);
            try {
                switch (cause) {
                    case "design": throw new ShipDesignException();
                    case "length": throw new ShipLengthException(shipType);
                    case "proximity": throw new ShipsProximityException();
                    default: validShip = true;
                }
            } catch (ShipDesignException | ShipLengthException | ShipsProximityException e) {
                System.out.println(e.getMessage());
                coords = player.placeAgain();
            }
        } while (!validShip);

        // once ship built return 2d array of coords [[x, y],[x, y],[x, y]]
        return shipCoords;
    }

    public int[] takeShot(int gameSize) {

        int[] shotCoords;

        String coords = player.takeShot();

        boolean validShot = false;
        do {
            shotCoords = new int[] {
                    Character.toUpperCase(coords.charAt(0)) - unicodeA,
                    Integer.parseInt(coords.substring(1)) - 1
            };

            String cause = GameException.getShotExceptionCause(shotCoords, gameSize);
            try {
                switch (cause) {
                    case "beyond": throw new ShotBeyondException();
                    default: validShot = true;
                }
            } catch (ShotBeyondException e) {
                System.out.println(e.getMessage());
                coords = player.shootAgain();
            }
        } while (!validShot);

        return shotCoords;
    }

}