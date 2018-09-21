package com.apurvaAnand.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static javafx.scene.control.Alert.*;

public class Main extends Application {
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader (getClass ().getResource ("game.fxml"));
        GridPane rootGridPane = loader.load();
        controller = loader.getController ();
        controller.createPlayground ();
        Pane menuPane = (Pane) rootGridPane.getChildren ().get(0);
        MenuBar menuBar = createMenu ();
        menuPane.getChildren ().add (0,menuBar);
        menuBar.prefWidthProperty ().bind (primaryStage.widthProperty ());

        Scene scene = new Scene(rootGridPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle ("Gravitrips");
        primaryStage.setResizable (true);
        primaryStage.show();

    }
    public MenuBar createMenu()
    {

        Menu fileMenu = new Menu ("File");
        MenuItem newMenu = new MenuItem ("New Game");
        newMenu.setOnAction (e->{
        	controller.resetGame();
        });

        MenuItem resetMenu = new MenuItem ("Reset Game");
        resetMenu.setOnAction (e->{
        	controller.resetGame();
        });
        MenuItem quitMenu = new MenuItem ("Exit Game");
        quitMenu.setOnAction (e->{
        	exitGame();
        });
        SeparatorMenuItem separator = new SeparatorMenuItem ();
        fileMenu.getItems ().addAll (newMenu,resetMenu,separator,quitMenu);
        Menu helpMenu = new Menu ("Help");
        MenuItem aboutGravitrips = new MenuItem ("About the Game");
        aboutGravitrips.setOnAction (e->{
        	aboutGravitripsGame();
        });
        MenuItem aboutMe = new MenuItem ("About the Developer");
        aboutMe.setOnAction (e->{
        	aboutApurva();
        });
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem ();
        helpMenu.getItems ().addAll (aboutGravitrips,separatorMenuItem,aboutMe);
        MenuBar menuBar = new MenuBar ();
        menuBar.getMenus ().addAll (fileMenu,helpMenu);
        return menuBar;
    }

	private void aboutApurva() {
		Alert aboutGame = new Alert(Alert.AlertType.INFORMATION);

		aboutGame.setTitle ("About the Developer");
		aboutGame.setHeaderText ("Apurva Anand\n"+
				                 "Sophomore,Information Technology\n"+
				                 "SRM Institute of Science and Technology");

		aboutGame.setContentText ("I am also an another part and parcel of Lord like everyone and a passionate lover of philosophy !");



		aboutGame.show ();
	}

	private void aboutGravitripsGame() {
		Alert aboutGame = new Alert(Alert.AlertType.INFORMATION);

		aboutGame.setTitle ("About Gravitripes");
		aboutGame.setHeaderText ("Rule to play the game :-" );
		aboutGame.setContentText ("Gravitrips is a two-player connection game in"+
				                  " which the players first choose a color and then "+
				                  "take turns dropping colored discs from the top into a seven-column"+
				                  ", six-row vertically suspended grid. The " +
				                  "pieces fall straight down, occupying the next available "+
				                  "space within the column. The objective of the game is to be"+
				                  " the first to form a horizontal, vertical, or diagonal line"+
				                  " of four of one's own discs.");

		aboutGame.show ();
	}

	private void exitGame() {
    	Platform.exit();

    	System.exit(0);
	}




	public static void main(String[] args) {
        launch(args);
    }
}
