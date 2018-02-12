/* *****************************************************************************
* Description: A class used to create the visuals of the cellular automata.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Insets;


public class CellularAutomataGUI extends Application {

	private Grid squareGrid;
	private GridPane squareGridPane, buttonPane;
	private VBox rootPane;
	private Stage mainStage;
	private Scene mainScene;
    private int width = 10;
    private int height = 10;
    private double[] initialNutrientLevels = new double[] {100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100};
    private CellularAutomataRules rules = new CellularAutomataRules(width, height, initialNutrientLevels);

    // Creates a Timeline that calls a function to continously update the grid
    // every 0.5 seconds.
    private Timeline timeline = new Timeline(new KeyFrame( Duration.seconds(0.5),
        timelineEvent -> {rules.createUpdatedGrid(squareGrid); }));

	// Displays the stage and creates the initial scene within it.
    @Override
    public void start(Stage primaryStage) {
    	mainStage = primaryStage;
    	squareGridPane = new GridPane();
    squareGridPane.setPadding(new Insets(10, 10, 0, 10));
    	squareGrid = new Grid(width, height);

        // Creates a 40 by 40 grid of Cells and adds them to the Pane.
    	for (int x=0; x<width; x++) {
    		for (int y=0; y<height; y++) {
		        Cell c = new Cell(15,15, Color.WHITE);
                squareGridPane.add(c,x,y);
                squareGrid.add(c,x,y);

                c.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        c.cellClicked();
                    }
                });
    		}
        }

    	Button startButton = new Button("Start");

    	// When the startButton is clicked it plays "timeline" indefinitely.
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.play();
            }
        });

        Button stopButton = new Button("Stop");

        // Stops the simulation.
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                timeline.stop();
            }
        });

        Button resetButton = new Button("Reset");

        // Stops the simulation and resets the grid to all dead cells.
        resetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                squareGrid.resetGrid();
                timeline.stop();
            }
        });

        buttonPane = new GridPane();
        buttonPane.setHgap(10);
        buttonPane.setVgap(10);
        buttonPane.setPadding(new Insets(0, 0, 0, 180));
        buttonPane.add(startButton,1,1);
        buttonPane.add(stopButton,2,1);
        buttonPane.add(resetButton,3,1);

        rootPane = new VBox(5);
        rootPane.getChildren().addAll(squareGridPane, buttonPane);

        mainScene = new Scene(rootPane,700,700);

        mainStage.setTitle("Bacterial Colony Simulator");
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
