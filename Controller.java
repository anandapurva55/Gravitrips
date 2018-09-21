package com.apurvaAnand.connect4;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.awt.*;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 125;
	private static final String discColor1 = "#000000";
	private static final String discColor2 = "#1642E4";

	private static  String PLAYER1 = "Player One";
	private static  String PLAYER2 = "Player Two";

	private boolean isPLAYER1 = true;
    public Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS];

      @FXML
	public GridPane rootGridPane;

      @FXML
	public Pane insertedDiscsPane;

       @FXML
	public Label playerNameLabel;

       @FXML
	public TextField playerOneTextField,playerTwoTextField;
       @FXML
	public javafx.scene.control.Button setNamesButton;

	private  boolean isAllowedToInsert = true;

	public void createPlayground() {
                  Shape rectangleWithHoles =createGamesStructuralGrid ();
            rootGridPane.add(rectangleWithHoles,0,1);
            List<Rectangle> rectangleList = createClickableColumns ();
            for(Rectangle rectangle : rectangleList) {
	            rootGridPane.add (rectangle, 0, 1);
            }
            setNamesButton.setOnAction (event->{
            	String p1 = playerOneTextField.getText ();
            	String p2 = playerTwoTextField.getText ();

		            PLAYER1 = p1;
		            PLAYER2 =p2;
		            playerNameLabel.setText (p1);




            });
}
private Shape createGamesStructuralGrid()
{
	Shape rectangleWithHoles = new Rectangle ((COLUMNS+1) * CIRCLE_DIAMETER, (ROWS+0.5) * CIRCLE_DIAMETER);
	for (int row = 0; row < ROWS; row++) {
		for (int col = 0; col < COLUMNS; col++){
			Circle circle = new Circle ();
			circle.setRadius (CIRCLE_DIAMETER / 2);
			circle.setCenterX (CIRCLE_DIAMETER / 2);
			circle.setCenterY (CIRCLE_DIAMETER / 2);
			circle.setSmooth (true);
			circle.setTranslateX (col*(CIRCLE_DIAMETER+5)+20);
			circle.setTranslateY (row*(CIRCLE_DIAMETER+5)+20);
			rectangleWithHoles = Shape.subtract (rectangleWithHoles, circle);

		}
	}
	rectangleWithHoles.setFill(Color.WHITE);
	return rectangleWithHoles;
}
public List<Rectangle>createClickableColumns(){
	List<Rectangle>rectangleList = new ArrayList<> ();//wecreated these rectangles so that we can put discs in these
	for(int col=0 ;col <COLUMNS;col++) {
		Rectangle rectangle = new Rectangle (CIRCLE_DIAMETER, (ROWS + 0.5) * CIRCLE_DIAMETER);
		rectangle.setFill (Color.TRANSPARENT);
		rectangle.setTranslateX (col*(CIRCLE_DIAMETER+5)+20);
		rectangle.setOnMouseEntered (e->{
			rectangle.setFill (Color.valueOf ("#eeeeee50"));//hover effect
		});
		rectangle.setOnMouseExited (e->{
			rectangle.setFill (Color.TRANSPARENT);//hover effect
		});
		final int column = col;
		rectangle.setOnMouseClicked (e->{
			if(isAllowedToInsert) {
				isAllowedToInsert = false;
				insertDisc (new Disc (isPLAYER1), column);
			}//constructor of Disc object will be called
		});
		rectangleList.add(rectangle);
	}

		return rectangleList;

}

	private void insertDisc(Disc disc, int column) {
		int row = ROWS - 1;
		while(row>=0){
			if(getDiscsPresent (row,column) == null) {//we are checking weather there is a empty space or not
				break;
			}

				row--;

			if(row<0)  //if disc is full we cannot insert disc anymore
				return;
		}
                  int currentRow = row;
                  insertedDiscArray[row][column] = disc;  //for structural change
		          insertedDiscsPane.getChildren ().add(disc);
		          disc.setTranslateX (column*(CIRCLE_DIAMETER+5)+20);
		TranslateTransition translateTransition = new TranslateTransition ((Duration.seconds (0.6)),disc);
		          translateTransition.setToY (row*(CIRCLE_DIAMETER+5)+20);
		          translateTransition.setOnFinished (e->{
		          	isAllowedToInsert = true;
		          	if (gameEnded(currentRow,column)){
		          		gameOver();
		          		return;
		            }
		          	isPLAYER1 = !isPLAYER1;
		          	playerNameLabel.setText (isPLAYER1?PLAYER1:PLAYER2);

		          });
		          translateTransition.play ();

	}

	private void gameOver() {
           String winner = isPLAYER1?PLAYER1:PLAYER2;

		Alert alert = new Alert (Alert.AlertType.INFORMATION);
		alert.setTitle ("Gravitrips");
		alert.setHeaderText ("The Winner is "+winner);
		alert.setContentText ("Do you want to play again ?");
		ButtonType yesButton = new ButtonType ("Yes");
		ButtonType noButton = new ButtonType ("No");
		alert.getButtonTypes ().setAll (yesButton,noButton);
		Platform.runLater (()->{
			Optional<ButtonType> btnClicked = alert.showAndWait ();
			if(btnClicked.isPresent () && btnClicked.get () == yesButton){
				resetGame();
			}else{
				noBtn();


			}
		});


	}

	public void resetGame() {
		insertedDiscsPane.getChildren ().clear ();  //visually removing discs
		for (int row = 0;row<insertedDiscArray.length;row++){
			for (int col = 0 ; col<insertedDiscArray[row].length;col++){
				insertedDiscArray[row][col] = null;

			}
		}
		isPLAYER1 = true;
		playerNameLabel.setText ("Player one turn");
		playerOneTextField.clear ();
		playerTwoTextField.clear ();
		createPlayground ();

	}

	private void noBtn() {
		Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
		alert.setTitle ("Exiting Gravitrips");
		alert.setHeaderText ("Thanks for playing Gravitrips");
		alert.setContentText ("***Have a nice day***");
		ButtonType yes = new ButtonType ("Ok");

		alert.getButtonTypes ().setAll (yes);
		Platform.runLater (()->
		{
			Optional<ButtonType> bttn = alert.showAndWait ();
			if(bttn.isPresent () && bttn.get () == yes){
				Platform.exit ();
				System.exit (0);
			}
		});


	}

	private boolean gameEnded(int row, int column) {
         //A small ex:-Player has inserted his last disc  at row = 2,column = 3
		/*index of element present at all the location within the column [row][column]:0,3   1,3   2,3   3,3   4,3   5,3   6,3
		(any four consecutive
		points can be the possibility )
		 */

		List<javafx.geometry.Point2D> verticalPoints = IntStream.rangeClosed (row-3,row+3)//range of row values=0,1,2,3,4,5,6
				                       .mapToObj (r-> new javafx.geometry.Point2D (r, column ))//0,3  1,3  2,3  3,3  4,3  5,3
				                      .collect(Collectors.toList());
		List<javafx.geometry.Point2D> horizontalPoints = IntStream.rangeClosed (column-3,column+3)
				.mapToObj (col-> new javafx.geometry.Point2D (row, col))
				.collect(Collectors.toList());
		javafx.geometry.Point2D start1Point = new javafx.geometry.Point2D (row-3,column+3);
		List<javafx.geometry.Point2D> diagonal1Points = IntStream.rangeClosed (0,6)
				                                                 .mapToObj (i->start1Point.add (i,-i))
				                                                 .collect(Collectors.toList());
		javafx.geometry.Point2D start2Point = new javafx.geometry.Point2D (row-3,column-3);
		List<javafx.geometry.Point2D> diagonal2Points = IntStream.rangeClosed (0,6)
				.mapToObj (i->start2Point.add (i,i))
				.collect(Collectors.toList());

		boolean isEnded = checkCombination(verticalPoints)||checkCombination (horizontalPoints)||checkCombination (diagonal1Points)||checkCombination (diagonal2Points);

		return  isEnded;

	}

	private boolean checkCombination(List<javafx.geometry.Point2D> Points) {
		int chain = 0;
		for (javafx.geometry.Point2D point:Points) {
			int rowIndexForArray = (int) point.getX ();
			int colIndexForArray = (int) point.getY ();
			Disc disc = getDiscsPresent (rowIndexForArray,colIndexForArray);
			 if(disc != null && disc.isPlayerOneMove == isPLAYER1)//if the last inserted disc belong to the current player
			 {
			 	chain++;
			 	if(chain == 4){
			 		return true;

			    }
			 }else{
				 chain = 0;

			 }

		}
		return  false;
	}
       private  Disc getDiscsPresent(int row,int col){
		if(row>=ROWS || row<0 || col >= COLUMNS || col <0)
			return null;
		return insertedDiscArray[row][col];
       }


	private static class  Disc extends Circle{
		private final boolean isPlayerOneMove;

		public Disc(boolean isPlayerOneMove)
		{
			this.isPlayerOneMove = isPlayerOneMove;
			setFill(isPlayerOneMove?Color.valueOf (discColor1):Color.valueOf (discColor2));
			setRadius (CIRCLE_DIAMETER/2);
			setCenterX (CIRCLE_DIAMETER/2);
			setCenterY (CIRCLE_DIAMETER/2);
		}





	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
