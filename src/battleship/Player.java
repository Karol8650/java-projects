package battleship;

import java.util.ArrayList;
import java.util.HashSet;

public class Player {
    private final Board board;
    private final ArrayList<HashSet<String>> coordsOfShips;
    private int sunkenShips;
    Player() {
        board = new Board();
        coordsOfShips = new ArrayList<>();
        sunkenShips = 0;
    }

    public void printBoard() {
        board.printBoard();
    }


    public char getChar(int row, int col) {
        return board.getChar(row, col);
    }




   public void setChar(int row, int col, char c) {
        board.setChar(row, col, c);
    }



    public void sinkShip() {
        sunkenShips++;
    }

    public int numOfSunkenShips() {
        return sunkenShips;
    }

    public void addShip() {
        coordsOfShips.add(new HashSet<>());
    }

    public void addCoordsToLastShip(String coords) {
        coordsOfShips.get(coordsOfShips.size() - 1).add(coords);
    }

    public boolean checkIfSunken(int x, int y) {
        String coords = "" + x + y;
        for(HashSet<String> set: coordsOfShips) {
            if (set.contains(coords)) {
                set.remove(coords);
                if(set.isEmpty()) {
                    coordsOfShips.remove(set);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
