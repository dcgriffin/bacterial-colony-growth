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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Insets;


public class CellularAutomataGUI extends Application {

	private GridPane gridPane, buttonPane;
	private VBox rootPane;
	private Stage mainStage;
	private Scene mainScene;
	// TODO: variable for update time.
	
	private Grid grid;
    private int gridWidth = 10;
    private int gridHeight = 10;
    private int cellHeight = 15;
    private int cellWidth = 15;
    
    private double delta = 0.4;
    private double[] initialNutrientLevels = new double[] {};
    private CellularAutomataRules rules = new CellularAutomataRules(gridWidth, gridHeight, initialNutrientLevels, delta);

    // Creates a Timeline that calls a function to continously update the grid every "updateTime" seconds.
    private Timeline timeline = new Timeline(new KeyFrame( Duration.seconds(0.25),
        timelineEvent -> {rules.createUpdatedGrid(grid); }));

	// Displays the stage and creates the initial scene within it.
    @Override
    public void start(Stage primaryStage) {
	    	mainStage = primaryStage;
	    gridPane = new GridPane();
	    gridPane.setPadding(new Insets(10, 10, 0, 10));
	    
	    // Creates the grid.
	    	grid = new Grid(gridWidth, gridHeight, cellWidth, cellHeight);
	
	    Cell cellsOfGrid[][] = grid.getCells();
	    	
	    	// Add the cells of the grid to the gridPane in order to show them visually.
	    	for (int x=0; x<gridWidth; x++) {
	    		for (int y=0; y<gridHeight; y++) {
		        Cell c = cellsOfGrid[x][y];
	            gridPane.add(c,x,y);
	
	            // Mouse click event that is called when a cell is clicked.
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
	
	    Button showOnlyBacteriaButton = new Button("Show Only Bacteria");
	
	    // Stops the simulation and resets the grid to all dead cells.
	    showOnlyBacteriaButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            grid.showOnlyBacteria();
	            timeline.stop();
	        }
	    });
	
	    buttonPane = new GridPane();
	    buttonPane.setHgap(10);
	    buttonPane.setVgap(10);
	    buttonPane.setPadding(new Insets(0, 0, 0, 180));
	    buttonPane.add(startButton,1,1);
	    buttonPane.add(stopButton,2,1);
	    buttonPane.add(showOnlyBacteriaButton,3,1);
	
	    rootPane = new VBox(5);
	    rootPane.getChildren().addAll(gridPane, buttonPane);
	
	    mainScene = new Scene(rootPane,700,700);
	
	    mainStage.setTitle("Bacterial Colony Simulator");
	    mainStage.setScene(mainScene);
	    mainStage.show();
	}

    public static void main(String[] args) {
    	launch(args);
    }
}
