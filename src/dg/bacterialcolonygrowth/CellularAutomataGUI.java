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
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;


public class CellularAutomataGUI extends Application {

	private Grid squareGrid;
	private GridPane squareGridPane, buttonPane;
	private VBox rootPane;
	private Stage mainStage;
	private Scene mainScene;

    // Creates a Timeline that calls a function to continously update the grid
    // every 0.5 seconds.
    private Timeline timeline = new Timeline(new KeyFrame( Duration.seconds(0.5),
        timelineEvent -> {CellularAutomataRules.createUpdatedGrid(squareGrid); }));

	// Displays the stage and creates the initial scene within it.
    @Override
    public void start(Stage primaryStage) {
    	mainStage = primaryStage;
    	squareGridPane = new GridPane();
        squareGridPane.setPadding(new Insets(10, 10, 0, 10));
    	squareGrid = new Grid();

        // Creates a 40 by 40 grid of Cells and adds them to the Pane.
    	for (int x=0; x<40; x++) {
    		for (int y=0; y<40; y++) {
		        Cell c = new Cell(15,15);
                c.setStroke(Color.GRAY);
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

        Button gliderButton = new Button("Glider");

        // Creates a glider shape on the grid.
        gliderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                squareGrid.turnCellBlack(17,20);
                squareGrid.turnCellBlack(18,20);
                squareGrid.turnCellBlack(19,20);
                squareGrid.turnCellBlack(19,19);
                squareGrid.turnCellBlack(18,18);
            }
        });

        Button pulsarButton = new Button("Pulsar");

        // Creates a pulsar shape on the grid.
        pulsarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                squareGrid.turnCellBlack(14,14);
                squareGrid.turnCellBlack(15,14);
                squareGrid.turnCellBlack(16,14);
                squareGrid.turnCellBlack(17,18);
                squareGrid.turnCellBlack(17,17);
                squareGrid.turnCellBlack(17,16);
                squareGrid.turnCellBlack(12,18);
                squareGrid.turnCellBlack(12,17);
                squareGrid.turnCellBlack(12,16);
                squareGrid.turnCellBlack(14,19);
                squareGrid.turnCellBlack(15,19);
                squareGrid.turnCellBlack(16,19);

                squareGrid.turnCellBlack(20,14);
                squareGrid.turnCellBlack(21,14);
                squareGrid.turnCellBlack(22,14);
                squareGrid.turnCellBlack(24,18);
                squareGrid.turnCellBlack(24,17);
                squareGrid.turnCellBlack(24,16);
                squareGrid.turnCellBlack(19,18);
                squareGrid.turnCellBlack(19,17);
                squareGrid.turnCellBlack(19,16);
                squareGrid.turnCellBlack(20,19);
                squareGrid.turnCellBlack(21,19);
                squareGrid.turnCellBlack(22,19);

                squareGrid.turnCellBlack(14,21);
                squareGrid.turnCellBlack(15,21);
                squareGrid.turnCellBlack(16,21);
                squareGrid.turnCellBlack(17,24);
                squareGrid.turnCellBlack(17,23);
                squareGrid.turnCellBlack(17,22);
                squareGrid.turnCellBlack(12,24);
                squareGrid.turnCellBlack(12,23);
                squareGrid.turnCellBlack(12,22);
                squareGrid.turnCellBlack(14,26);
                squareGrid.turnCellBlack(15,26);
                squareGrid.turnCellBlack(16,26);

                squareGrid.turnCellBlack(20,21);
                squareGrid.turnCellBlack(21,21);
                squareGrid.turnCellBlack(22,21);
                squareGrid.turnCellBlack(24,24);
                squareGrid.turnCellBlack(24,23);
                squareGrid.turnCellBlack(24,22);
                squareGrid.turnCellBlack(19,24);
                squareGrid.turnCellBlack(19,23);
                squareGrid.turnCellBlack(19,22);
                squareGrid.turnCellBlack(20,26);
                squareGrid.turnCellBlack(21,26);
                squareGrid.turnCellBlack(22,26);



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
        buttonPane.add(gliderButton,4,1);
        buttonPane.add(pulsarButton,5,1);

        rootPane = new VBox(5);
        rootPane.getChildren().addAll(squareGridPane, buttonPane);

        mainScene = new Scene(rootPane,700,700);

        mainStage.setTitle("Conway's Game of Life");
        mainStage.setScene(mainScene);
        mainStage.show();
    }

    public static void main(String[] args) {
    	launch(args);
    }
}
