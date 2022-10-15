package battleship;

public enum Ship {
    Aircraft_Carrier("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private final String name;
    private final int cells;

    Ship(String name, int cells) {
        this.name = name;
        this.cells = cells;
    }

    public String getName() {
        return name;
    }

    public int getCells() {
        return cells;
    }

    public static int numOfShips() {
        int count = 0;
        for(Ship ignored : Ship.values()) {
            count++;
        }
        return count;
    }
}
