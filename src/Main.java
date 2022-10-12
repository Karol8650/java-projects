import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

    static void enterCoordinates(char[][] battleField) {
        LinkedHashMap<String, Integer> ships = getShips();

        for (Map.Entry<String, Integer> ship : ships.entrySet()) {
            int cells = ship.getValue();
            String name = ship.getKey();

            String input;
            do {
                System.out.println("Enter the coordinates of the " + name + " (" + cells + "):");
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
            } while (!checkInput(input, ship));


        }
    }

    static private boolean checkToken(String token) {
        if (token == null || (token.length() != 2 && token.length() != 3)) {
            return false;
        }
        if (token.charAt(0) >= 'A' && token.charAt(0) <= 'J') {
            if (token.length() == 2) {
                return token.charAt(1) >= '1' && token.charAt(1) <= '9';
            } else {
                return token.charAt(1) >= '1' && token.charAt(2) <= '0';
            }
        }
       return false;
    }

    static private boolean checkInput(String input, Map.Entry<String, Integer> ship) {
        if (input == null || ship == null) {
            return false;
        }

        StringTokenizer tokenizer = new StringTokenizer(input);
        String first = tokenizer.nextToken();
        String second = tokenizer.nextToken();

        if (!checkToken(first) || !checkToken(second)) {
            System.out.println("Error! Wrong format of input! Try again!:");
            return false;
        }


        final char startRow = input.charAt(0);
        final char startCol = input.charAt(1);
        final char endRow = input.charAt(3);
        final char endCol = input.charAt(4);

        if(startRow != endRow && startCol != endCol) {
            System.out.println("Error! Wrong ship location! Try again:");
            return false;
        }


        return false;
    }



    private static LinkedHashMap getShips() {
        LinkedHashMap<String, Integer> ships = new LinkedHashMap<>();
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);
        return ships;
    }

    static void display(char[][] battleField) {
        final int size = 10;
        System.out.print("  ");
        for (int i = 1; i <= size; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < size; i++) {
            char c = (char) ('A' + i);
            System.out.print(c + " ");
            for (int j = 0; j < size; j++) {
                System.out.print(battleField[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        final int size = 10;
        char[][] battleField = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                battleField[i][j] = '~';
            }
        }






    }
}