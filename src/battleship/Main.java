package battleship;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        try {
            game.play();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
