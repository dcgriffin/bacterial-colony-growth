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
    
    // Every m time steps cell division occurs, the following two variables are used to keep track of when
    // cell division should occur.
    private int timeStepForCellDivisionCounter = 1;
    private int numberOfTimeStepsForCellDivision = 8;
    
    private int nutrientForSustenance = 10;
    private int nutrientForGrowth = 60;
    private int thresholdForDivision = 2600;

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
        this.setCrowdingFunctionValues();
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
    public int returnPositionInNutrientMatrix(int x, int y) {
    		return x + y*height;
    }

    // Updates the nutrient levels for diffusion after a single time step.
    public void updateNutrientLevelsAfterDiffusion() {
        nutrientLevels = updateMatrixPeriodicBoundary.mmul(nutrientLevels);
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
        crowdingFunctionValues = new int[] {0, 40, 40, 40, 30, 20, 10, 0, 0};
        System.out.println(crowdingFunctionValues[1]);
    }

    // Updates nutrients levels after bacteria have consumed some nutrient.
    public void updateBacteriaAndNutrientAfterConsumption(Grid currentGrid) {
    		// Variable that is set to true during time steps where cell division can occur.
    		boolean checkForCellDivision = false;
    		
    		// Check if it is the correct time step for cell division to take place.
		if (timeStepForCellDivisionCounter == numberOfTimeStepsForCellDivision) {
			checkForCellDivision = true;
		}
		
		// Loops through all the grid spaces in the cellular automata.
        for (int x=0; x<width; x++) {
	    		for (int y=0; y<height; y++) {
	    			if (currentGrid.cellAlive(x, y) == true) {
	    				if (nutrientLevels.get(returnPositionInNutrientMatrix(x, y)) >= 10) {
	    					setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y), 
	    						nutrientLevels.get(returnPositionInNutrientMatrix(x, y))-nutrientForSustenance);
	    				}
	    				else {
	    					currentGrid.setBacteriumDead(x, y);
	    					setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y),0);
	    				}
	    			}
	    			// In the case where they is no alive or previosuly alive bacterium cell occupying the 
	    			// grid space.
	    			else if (currentGrid.cellAliveOrContainsRemains(x, y) == false) {
	    				// Check value of flag used to indicate that cell division may occur this times step.
	    				if (checkForCellDivision == true) {
	    					if (shouldCellDivisionOccur(currentGrid, x, y) && nutrientLevels.get(returnPositionInNutrientMatrix(x, y)) >= 60) {
	    						currentGrid.setBacteriumAlive(x, y);
	    						setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y), 
	    	    						nutrientLevels.get(returnPositionInNutrientMatrix(x, y))-nutrientForGrowth);
	    					}
	    				}
	    			}
	    			currentGrid.setNutrientLevelColor(x, y, 0, getNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y))/100, 1);
	    		}
        }
        
        // Reset the cell division counter and variable flag if necessary, otherwise increment cell
        // division counter.
 		if (checkForCellDivision == true) {
 			checkForCellDivision = false;
 			timeStepForCellDivisionCounter = 1;
 		}
 		else {
 			timeStepForCellDivisionCounter += 1;
 		}   
    }
    
    // Checks whether the conditions for cell division to occur are met.
    // Cell growth may take place in a grid space, if for that cell the product of the crowding function 
    // and the food in the cell, is greater than a given threshold for division. If it is greater than the
    // threshold then bacterium cell will appear in the grid space with probability 50%.
    public boolean shouldCellDivisionOccur(Grid currentGrid, int x, int y) {
    		double nutrientInCell = nutrientLevels.get(returnPositionInNutrientMatrix(x, y));
    		int numberOfNeighbours = returnNumberOfAliveNeighboursForPeriodicGrid(currentGrid, x, y);
    		// Check if crowding function * nutrient level is greater than threshold.
    		if (crowdingFunctionValues[numberOfNeighbours] * nutrientInCell > thresholdForDivision) {
    			// If a random number from 0 up to 1 is greater than 0.5 then cell division takes place.
    			if (Math.random() >= 0.5) {
    				return true;
    			}
    		}
    		
    		return false;
    }
    
    // Returns the number of alive neighbours of cell x,y in the grid passed to this function.
    // Will return -1 if the cell itself and all its neighbours are dead.
    public int returnNumberOfAliveNeighboursForPeriodicGrid(Grid currentGrid, int x, int y) {
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

    				if (currentGrid.cellAlive(tempCol, tempRow) == true)
    					numberOfNeighbours++;
    			}
    		}
    		
    		// If number of neighbours is still -1 at the end it means there are no neighbours and no bacterium
    		// in the cell itself, so should return 0.
    		if (numberOfNeighbours == -1) {
    			numberOfNeighbours = 0;
    		}
    		
    		return numberOfNeighbours;
    }

	// Creates an updated grid of the current grid after applying the
    // rules of the game. The rules are:
    // -Any live cell with fewer than two live neighbours dies, as if caused by under-population.
    // -Any live cell with two or three live neighbours lives on to the next generation.
    // -Any live cell with more than three live neighbours dies, as if by over-population.
    // -Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	public void createUpdatedGrid(Grid currentGrid) {
		Grid tempGrid = createNewGrid();

        this.updateNutrientLevelsAfterDiffusion();
        this.updateBacteriaAndNutrientAfterConsumption(currentGrid);

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
		int numberOfNeighbours = 0;
		
		// Loops through 9 cells, the cell in question along with the surrounding 8.
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

    				if (currentGrid.cellAlive(tempCol, tempRow) == true)
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

                if (currentGrid.cellAlive(tempCol, tempRow) == true)
                    numberOfNeighbours++;
	        	}
        }

		if (numberOfNeighbours == 3)
			tempGrid.setBacteriumAlive(x, y);
	}
}
