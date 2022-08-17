package battleship;


public enum Ships {

    AIC("Aircraft Carrier", 5),
    BAT("Battleship", 4),
    SUB("Submarine", 3),
    CRU("Cruiser", 3),
    DES("Destroyer", 2);

    private final String shipType;
    private final int shipSize;

    Ships(String shipType, int shipSize) {
        this.shipType = shipType;
        this.shipSize = shipSize;
    }
    public String getType() {
        return shipType;
    }
    public int getSize() {
        return shipSize;
    }
}


