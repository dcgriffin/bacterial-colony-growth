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

	// Properties of the GUI Simulation/visual setup.
	private GridPane gridPane, buttonPane;
	private VBox rootPane;
	private Stage mainStage;
	private Scene mainScene;
	private double gridUpdateRate = 0.001; // In seconds, default = 0.001
	
	// Visual properties of the cellular automata grid in the GUI.
	private Grid grid;
    private int gridWidth = 80;
    private int gridHeight = 80;
    private int cellHeight = 5;
    private int cellWidth = 5;
    
    private CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules(gridWidth, gridHeight);

    // Creates a Timeline that calls a function to continuously updates the grid every "gridUpdateRate" 
    // seconds.
    private Timeline timeline = new Timeline(new KeyFrame( Duration.seconds(gridUpdateRate),
    								timelineEvent -> { this.manageSimulation(); }));
    
    // Manages the simulation by repeatedly calling the update grid method and pausing the simulation
    // until that method has finished executing.
    private void manageSimulation() {
    		timeline.stop();
		rules.createUpdatedGrid(grid);
		timeline.play();
	}

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
	
	    Button showOnlyBacteriaButton = new Button("End Simulation: Show Only Bacteria");
	
	    // Stops the simulation and calls a function to show only bacteria cells.
	    showOnlyBacteriaButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            grid.showOnlyBacteriaAsSquares();
	            timeline.stop();
	        }
	    });
	    
	    Button loadInputFile = new Button("Load input file");
		
	    // Calls a function to deal with setting parameters of the simulation from an input file.
	    loadInputFile.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            grid.loadInputFile();
	        }
	    });
	
	    // Adds the buttons to the button pane.
	    buttonPane = new GridPane();
	    buttonPane.setHgap(10);
	    buttonPane.setVgap(10);
	    buttonPane.setPadding(new Insets(0, 0, 0, 0));
	    buttonPane.add(startButton,1,1);
	    buttonPane.add(stopButton,2,1);
	    buttonPane.add(showOnlyBacteriaButton,3,1);
	    buttonPane.add(loadInputFile,4,1);
	
	    // Add the grid and buttons to the rootPane.
	    rootPane = new VBox(5);
	    rootPane.getChildren().addAll(gridPane, buttonPane);
	
	    // Adds the rootpane to the scene.
	    mainScene = new Scene(rootPane,700,700);
	
	    // Adds the scene to the stage and shows the stage.
	    mainStage.setTitle("Bacterial Colony Simulator");
	    mainStage.setScene(mainScene);
	    mainStage.show();
	}

    public static void main(String[] args) {
    	launch(args);
    }
}
