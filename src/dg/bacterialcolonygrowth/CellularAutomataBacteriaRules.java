/* *****************************************************************************
* Description: A class used to simulate the rules of a bacterial colony.
* The grid wraps around so the bottom meets up with the top, and the left side
* meets up with the right side.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import org.jblas.FloatMatrix;


public class CellularAutomataBacteriaRules {
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

    private FloatMatrix updateMatrixPeriodicBoundary;
    private FloatMatrix nutrientLevels;
    
    // Rate of diffusion (value should be between 0 and 1).
    private float delta = 0.4f;

    // Stores a value for each possible number of surrounding cells (0-8), which
    // is then used to determine if cell division takes place.
    private int[] crowdingFunctionValues = {0, 40, 40, 40, 30, 20, 10, 0, 0};

    // Constructor which create a new rules object.
	public CellularAutomataBacteriaRules(int x, int y) {
        width = x;
        height = y;
        numberOfCellsInGrid = width * height;
        updateMatrixPeriodicBoundary = FloatMatrix.zeros(numberOfCellsInGrid, numberOfCellsInGrid);
        nutrientLevels = FloatMatrix.zeros(numberOfCellsInGrid);
        
        this.setInitialDefaultNutrientLevels();
        this.createUpdateMatrixForPeriodicBoundary();
	}
	
	// Constructor which creates a new rules object with specified conditions.
	public CellularAutomataBacteriaRules(int x, int y, float[] initialNutrientLevels, float deltaValue) {
        width = x;
        height = y;
        numberOfCellsInGrid = width * height;
        updateMatrixPeriodicBoundary = FloatMatrix.zeros(numberOfCellsInGrid, numberOfCellsInGrid);
        delta = deltaValue;
        nutrientLevels = FloatMatrix.zeros(numberOfCellsInGrid);
        
        this.setInitialNutrientLevels(initialNutrientLevels);
        this.createUpdateMatrixForPeriodicBoundary();
	}

    // Creates the update matrix for a cellular automata with a periodic boundary.
    private void createUpdateMatrixForPeriodicBoundary() {
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
    
    // From 2D coordinates of a grid position, return the position this corresponds to in the 1D nutrient
    // matrix.
    public int returnPositionInNutrientMatrix(int x, int y) {
    		return x + y*height;
    }

    // Updates the nutrient levels for diffusion after a single time step.
    public void updateNutrientLevelsAfterDiffusion() {
        nutrientLevels = updateMatrixPeriodicBoundary.mmul(nutrientLevels);
    }

    // Sets the initial nutrient levels based on the array of values passes to it.
    private void setInitialNutrientLevels(float[] initialNutrientLevels) {
         for (int i=0; i<initialNutrientLevels.length; i++) {
             nutrientLevels.put(i, initialNutrientLevels[i]);
         }
    }
    
    // Sets every cell to have 100 nutrient level.
    private void setInitialDefaultNutrientLevels() {
		for (int i=0; i<nutrientLevels.length; i++) {
			nutrientLevels.put(i, 100);
		}
    }
    
    // Returns the nutrient level in the cell specified as an argument.
    public double getNutrientLevelOfCell (int i) {
        return nutrientLevels.get(i);
    }
    
    // Sets the nutrient level of the specified cell, to the amount of nutrient specified.
    public void setNutrientLevelOfCell (int i, float newNutrientLevel) {
    		nutrientLevels.put(i, newNutrientLevel);
    }

    // Updates nutrients levels after bacteria have consumed some nutrient.
    public void updateBacteriaAndNutrientAfterConsumptionAndCellDivision(Grid currentGrid, Grid gridBeforeThisUpdate) {
    		// Variable that is set to true during time steps where cell division can occur.
    		boolean checkForCellDivision = false;
    		
    		// Check if it is the correct time step for cell division to take place.
		if (timeStepForCellDivisionCounter == numberOfTimeStepsForCellDivision) {
			checkForCellDivision = true;
		}
		
		// Loops through all the grid spaces in the cellular automata.
        for (int x=0; x<width; x++) {
	    		for (int y=0; y<height; y++) {
	    			// If grid is alive, then bacteria eat nutrient of the required amount to survive. If there
	    			// is not enough they will consume all the nutrient and then die.
	    			if (gridBeforeThisUpdate.cellAlive(x, y) == true) {
	    				if (nutrientLevels.get(returnPositionInNutrientMatrix(x, y)) >= 10) {
	    					setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y), 
	    						nutrientLevels.get(returnPositionInNutrientMatrix(x, y))-nutrientForSustenance);
	    				}
	    				else {
	    					currentGrid.setBacteriumDead(x, y);
	    					setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y),0);
	    				}
	    			}
	    			// In the case where they is no alive or previously alive bacterium cell occupying the 
	    			// grid space.
	    			else if (gridBeforeThisUpdate.cellAliveOrContainsRemains(x, y) == false) {
	    				// Check value of flag used to indicate that cell division may occur this times step.
	    				if (checkForCellDivision == true) {
	    					if (shouldCellDivisionOccur(gridBeforeThisUpdate, x, y) && nutrientLevels.get(returnPositionInNutrientMatrix(x, y)) >= 60) {
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
    public boolean shouldCellDivisionOccur(Grid gridBeforeUpdate, int x, int y) {   	
    		double nutrientInCell = nutrientLevels.get(returnPositionInNutrientMatrix(x, y));
    		int numberOfNeighbours = returnNumberOfAliveNeighboursForPeriodicGrid(gridBeforeUpdate, x, y);
    		
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

                if (currentGrid.cellAlive(tempCol, tempRow) == true) {
    					// Make sure it is not the cell itself that is being counted.
    					if (tempCol != x || tempRow != y) {
    						numberOfNeighbours++;
    					}
				}
    			}
    		}
    		
    		return numberOfNeighbours;
    }

	// Creates an updated grid after one iteration of the rules governing the bacterial colony.
	public void createUpdatedGrid(Grid currentGrid) {
		long startTime = System.currentTimeMillis();
		
		long startTime4 = System.currentTimeMillis();
		
		// Creates a copy of the current grid.
		Grid copyOfCurrentGrid = new Grid(currentGrid);
		
		long stopTime4 = System.currentTimeMillis();
        long elapsedTime4 = stopTime4 - startTime4;
        System.out.println("Time to make a copy of current grid: " + elapsedTime4);
		
		long startTime2 = System.currentTimeMillis();
		
		// Update for diffusion.
        this.updateNutrientLevelsAfterDiffusion();
        
        long stopTime2 = System.currentTimeMillis();
        long elapsedTime2 = stopTime2 - startTime2;
        System.out.println("Time to for matrix multiplication: " + elapsedTime2);
        
        long startTime3 = System.currentTimeMillis();
        
        // Update for bacteria consuming nutrient and reproducing.
        this.updateBacteriaAndNutrientAfterConsumptionAndCellDivision(currentGrid, copyOfCurrentGrid);
        
        long stopTime3 = System.currentTimeMillis();
        long elapsedTime3 = stopTime3 - startTime3;
        System.out.println("Time to update grid with new nutrient levels and bacteria: " + elapsedTime3);
        
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time for overall update of grid: " + elapsedTime);
	}
}
