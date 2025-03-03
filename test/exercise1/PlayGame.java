package exercise1;

public class PlayGame extends AbstractGame {

    public void playGame() {
        // Create a thread for each player
        Thread player1 = new Thread(() -> {
            while (!gameIsOver()) {
                playCard();
            }
        });

        Thread player2 = new Thread(() -> {
            while (!gameIsOver()) {
                playCard();
            }
        });

        // Start both player threads
        player1.start();
        player2.start();

        // Wait for both players to finish
        try {
            player1.join();
            player2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
