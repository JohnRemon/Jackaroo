package view;
import javafx.application.Application;
import javafx.stage.Stage;


public class BoardView extends Application {


    @Override
    public void start(Stage stage) throws Exception {
            initGame();     //this should use the constructors and start the game back-end wise
            initUI();       //this will start the static UI (start menu, players, score)
            drawBoard();    //this will render the board itself (track + safezone) WITHOUT MARBLES
            drawMarbles();  //this will render the marbles. They're on a seperate layer as they're dynamic and ever changing.
            initTimeLine(); //this will control the turn responsibilites of the game. Possibly can do without
    }
}
