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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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
	
	// The cellular automata grid.
	private Grid grid;
    
    // Variable used to store the rules governing the bacteria simulation.
    private CellularAutomataBacteriaRules rules;

    // Creates a Timeline that calls a function to continuously updates the grid every "gridUpdateRate" 
    // seconds.
    private Timeline timeline = new Timeline(new KeyFrame( Duration.seconds(gridUpdateRate),
    								timelineEvent -> { this.manageSimulation(); }));

	// Displays the stage and creates the initial scene within it.
    @Override
    public void start(Stage primaryStage) {
	    	mainStage = primaryStage;
	    gridPane = new GridPane();
	    gridPane.setPadding(new Insets(10, 10, 0, 10));
	    
	    rules = new CellularAutomataBacteriaRules();
	    
	    // Creates the grid.
	    	grid = rules.getCellularAutomataGrid();
	    	
	    	this.addGridToGridpane();
	
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
	
	    Button showOnlyBacteriaButton = new Button("Show final bacteria pattern");
	
	    // Stops the simulation and calls a function to show only bacteria cells.
	    showOnlyBacteriaButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            grid.showOnlyBacteriaAsSquares();
	            timeline.stop();
	            
	            // Remove start and stop buttons after final pattern is shown.
	            buttonPane.getChildren().remove(startButton);
	            buttonPane.getChildren().remove(stopButton);
	        }
	    });
	    
	    Button loadInputFile = new Button("Load input file");
		
	    // Allows a user to select a file by graphically selecting a file from their directories.
	    // Then calls a function to deal with setting the simulation parameters to those in the input file.
	    loadInputFile.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        		timeline.stop();
	        		
	            FileChooser fileChooser = new FileChooser();
	            File selectedFile = fileChooser.showOpenDialog(null);
	            
	            if (selectedFile != null) {
	            		// Creates new rule set the parameters specified in the selected file.
	            		try {
	            			rules = new CellularAutomataBacteriaRules(selectedFile);
	            			
	            			// Display start and stop buttons if they are not currently displayed.
	            			if (!(buttonPane.getChildren().contains(startButton))) {
	            				buttonPane.getChildren().add(0, startButton);
	            			}
	            			if (!(buttonPane.getChildren().contains(stopButton))) {
	            				buttonPane.getChildren().add(1, stopButton);
	            			}
	            		}
	            		catch(IOException e) {
	                     Alert alert = new Alert(AlertType.ERROR, "Cannot read input file.", ButtonType.OK);
	                     alert.showAndWait();
	                }
                    catch(IllegalArgumentException e) {
                    		Alert alert = new Alert(AlertType.ERROR, "Input file is not the correct format.\n\n" + e.getMessage(), ButtonType.OK);
                    		alert.showAndWait();
                    }
	            		
	            		// Gets the new grid to display graphically in case it has changed.
	            		grid = rules.getCellularAutomataGrid();	            		
	            		gridPane.getChildren().clear();	            		
	            		addGridToGridpane();
	            }
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
	    rootPane.getChildren().addAll(buttonPane, gridPane);
	
	    // Adds the rootpane to the scene.
	    mainScene = new Scene(rootPane,700,700);
	
	    // Adds the scene to the stage and shows the stage.
	    mainStage.setTitle("Bacterial Colony Simulator");
	    mainStage.setScene(mainScene);
	    mainStage.show();
	}
    
    // Manages the simulation by repeatedly calling the update grid method and pausing the simulation
    // until that method has finished executing.
    private void manageSimulation() {
    		timeline.stop();
		rules.createUpdatedGrid();
		timeline.play();
	}
    
    // Adds the cellular automata Grid to the gridpane to show them on screen.
    private void addGridToGridpane() {
	    Cell cellsOfGrid[][] = grid.getCells();
    	
	    	// Add each cell of the grid to the gridPane.
	    	for (int x=0; x<grid.getGridWidth(); x++) {
	    		for (int y=0; y<grid.getGridHeight(); y++) {
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
    }

    public static void main(String[] args) {
    		launch(args);
    }
}
