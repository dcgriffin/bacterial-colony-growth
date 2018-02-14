/* *****************************************************************************
* Description: A class used to simulate a bacterial colony.
* The grid wraps around so the bottom meets up with the top, and the left side
* meets up with the right side.
*
* CURRENTLY IMPLEMENTS GAME OF LIFE AS A STARTING POINT
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import javafx.scene.paint.Color;
import org.jblas.DoubleMatrix;


public class CellularAutomataRules {
    private int width;
    private int height;
    private int numberOfCellsInGrid;
    
    private int nutrientForSustenance = 10;
    private int nutrientForGrowth = 60;
    private int thresholdForDivision = 2200;

    private DoubleMatrix updateMatrixPeriodicBoundary;
    private DoubleMatrix nutrientLevels;
    // Rate of diffusion (value should be between 0 and 1).
    private double delta;

    // Stores a value for each possible number of surrounding cells (0-8), which
    // is then used to determine if cell division takes place.
    private int[] crowdingFunctionValues;

	public CellularAutomataRules(int x, int y, double initialNutrientLevels[], double valueOfDelta) {
        width = x;
        height = y;
        numberOfCellsInGrid = width * height;
        delta = valueOfDelta;
        updateMatrixPeriodicBoundary = DoubleMatrix.zeros(numberOfCellsInGrid, numberOfCellsInGrid);
        nutrientLevels = DoubleMatrix.zeros(numberOfCellsInGrid);
        this.setNutrientLevels(initialNutrientLevels);
        this.createUpdateMatrixForPeriodicBoundary();
	}

    // Creates the update matrix for a cellular automata with a periodic boundary.
    public void createUpdateMatrixForPeriodicBoundary() {
        for (int i=0; i<numberOfCellsInGrid; i++) {
            // The cell itself.
	        updateMatrixPeriodicBoundary.put(i, i, 1 - delta);
            // Cell to the left.
            if (i % width == 0) {
                updateMatrixPeriodicBoundary.put(i, i + width - 1, delta/4);
            }
            else {
                updateMatrixPeriodicBoundary.put(i, i - 1, delta/4);
            }
            // Cell to the right.
            if ((i + 1) % width == 0) {
                updateMatrixPeriodicBoundary.put(i, i - width + 1, delta/4);
            }
            else {
                updateMatrixPeriodicBoundary.put(i, i + 1, delta/4);
            }
            // Cell above.
            if (i + width > numberOfCellsInGrid - 1) {
                updateMatrixPeriodicBoundary.put(i, i - numberOfCellsInGrid + width, delta/4);
            }
            else {
                updateMatrixPeriodicBoundary.put(i, i + width, delta/4);
            }
            // Cell below.
            if (i - width < 0) {
                updateMatrixPeriodicBoundary.put(i, i + numberOfCellsInGrid - width, delta/4);
            }
            else {
                updateMatrixPeriodicBoundary.put(i, i - width, delta/4);
            }
        }
    }
    
    // From 1D nutrient level matrix position, function returns the coordinates of the position within the 2D
    // cellular automata.
//    public int[] returnCoordinatesWithin2DCellularAutomata(int i) {
//    		int [] coordinates = {(i/width), i % width};
//    		return coordinates;
//    }
    
    // From 2D coordinates of a grid position, return the position this corresponds to in the 1D nutrient
    // matrix.
//    public int returnPositionInNutrientMatrix(int x, int y) {
//    		return x + y*height;
//    }

    // Updates the nutrient levels after a single time step.
    public void updateNutrientLevels(Grid currentGrid) {
        System.out.println(nutrientLevels);
        nutrientLevels = updateMatrixPeriodicBoundary.mmul(nutrientLevels);
        
        // Updates the visual appearance of the cellular automata. It converts x and y coordinates to the 
        // corresponding position in the nutrient matrix, then divides the nutrient level value by 100 to 
        // give a saturation value.
        for (int x=0; x<width; x++) {
	    		for (int y=0; y<height; y++) {
	    			currentGrid.setNutrientLevelColor(x, y, 0, getNutrientLevelOfCell(x + y*height)/100, 1);
	    		}
        }
    }

    // Sets the initial nutrient levels, passing empty array means every cell starts at 100.
    public void setNutrientLevels(double[] initialNutrientLevels) {
    		if (initialNutrientLevels.length == 0) {
    			for (int i=0; i<nutrientLevels.length; i++) {
   	             nutrientLevels.put(i, 100);
   	         }
    		}
    		else {
	         for (int i=0; i<initialNutrientLevels.length; i++) {
	             nutrientLevels.put(i, initialNutrientLevels[i]);
	         }
    		}
    }

    public double getNutrientLevelOfCell (int i) {
        return nutrientLevels.get(i);
    }
    
    public void setNutrientLevelOfCell (int i, double newNutrientLevel) {
    		nutrientLevels.put(i, newNutrientLevel);
    }

    // Sets the values for the crowding function.
    public void setCrowdingFunctionValues() {
        crowdingFunctionValues = new int[] {0, 40, 40, 30, 20, 10, 0, 0};
    }

    // Updates nutrients levels after bacteria have consummed some nutrient.
//    public void updateNutrientsLevelsAfterConsumption(Grid currentGrid) {
//        for (int x=0; x<width; x++) {
//	    		for (int y=0; y<height; y++) {
//			        if (currentGrid.cellStatus(x, y) == true) {
//			        	// Reduce nutrient level in that cell
//	                }
//	    		}
//        }
//    }

	// Creates an updated grid of the current grid after applying the
    // rules of the game. The rules are:
    // -Any live cell with fewer than two live neighbours dies, as if caused by under-population.
    // -Any live cell with two or three live neighbours lives on to the next generation.
    // -Any live cell with more than three live neighbours dies, as if by over-population.
    // -Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	public void createUpdatedGrid(Grid currentGrid) {
		Grid tempGrid = createNewGrid();

        this.updateNutrientLevels(currentGrid);
        this.setCrowdingFunctionValues();

		// Loops through each cell of the grid and checks if it is dead or alive.
		// It then calls another function to handle that cell depending on
		// whether it is dead or alive.
//        for (int x=0; x<width; x++) {
//	    		for (int y=0; y<height; y++) {
//		        if (currentGrid.cellStatus(x, y) == true)
//		        		liveCellNeighbourChecker(currentGrid, tempGrid, x, y);
//		        else
//		        		deadCellNeighbourChecker(currentGrid, tempGrid, x, y);
//	    		}
//	    }
//
//    	copyTempGridToCurrentGrid(currentGrid, tempGrid);
	}

	// Creates a Grid object which contains a grid of Cells of size width*height, all white.
	public Grid createNewGrid() {
		Grid newGrid = new Grid(width, height);

	    	for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
		        Cell c = new Cell(15,15, Color.WHITE);
		        newGrid.add(c, x, y);
			}
	    	}
	    	
	    	return newGrid;
	}

	// Checks the neighbours of alive cells and updates the tempGrid accordingly.
	public void liveCellNeighbourChecker(Grid currentGrid, Grid tempGrid, int x, int y) {
		// Set to -1 as it will count the cell itself in the loop below.
		int numberOfNeighbours = -1;

		// Loops through 9 cells. The cell in question along with the surrounding 8.
    		for (int col = (x-1); col<(x+2); col++) {
    			for (int row = (y-1); row<(y+2); row++) {
                    int tempRow = row;
                    int tempCol = col;

                    if (tempCol == width)
                        tempCol = 0;
                    else if (tempCol == -1)
                        tempCol = width - 1;

                    if (tempRow == height)
                        tempRow = 0;
                    else if (tempRow == -1)
                        tempRow = height - 1;

    				if (currentGrid.cellStatus(tempCol, tempRow) == true)
    					numberOfNeighbours++;
    			}
    		}

		if (numberOfNeighbours == 2 || numberOfNeighbours == 3)
			tempGrid.setBacteriumAlive(x, y);
	}

	// Checks the neighbours of dead cells and updates the tempGrid accordingly.
	public void deadCellNeighbourChecker(Grid currentGrid, Grid tempGrid, int x, int y) {
		int numberOfNeighbours = 0;

        // Loops through 9 cells. The cell in question along with the surrounding 8.
        for (int col = (x-1); col<(x+2); col++) {
	        	for (int row = (y-1); row<(y+2); row++) {
                int tempRow = row;
                int tempCol = col;

                if (tempCol == width)
                    tempCol = 0;
                else if (tempCol == - 1)
                    tempCol = width - 1;

                if (tempRow == height)
                    tempRow = 0;
                else if (tempRow == -1)
                    tempRow = height - 1;

                if (currentGrid.cellStatus(tempCol, tempRow) == true)
                    numberOfNeighbours++;
	        	}
        }

		if (numberOfNeighbours == 3)
			tempGrid.setBacteriumAlive(x, y);
	}

	// Copies the tempGrid to the currentGrid. This is done once the tempGrid
	// has been fully completed to show the next stage after the what the
	// currentGrid shows.
	public void copyTempGridToCurrentGrid(Grid currentGrid, Grid tempGrid) {
	    	for (int x=0; x<width; x++) {
	    		for (int y=0; y<height; y++) {
		        if (tempGrid.cellStatus(x, y) == true)
		        		currentGrid.setBacteriumAlive(x, y);
		        else
		        		currentGrid.setBacteriumDead(x, y);
	    		}
	    }
	}
}
