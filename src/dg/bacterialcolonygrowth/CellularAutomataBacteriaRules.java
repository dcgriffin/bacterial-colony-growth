/* *****************************************************************************
* Description: A class used to represent the cellular automaton model of a bacterial colony.
* 
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.la4j.matrix.sparse.CRSMatrix;
import org.la4j.vector.DenseVector;

public class CellularAutomataBacteriaRules {
	private Grid grid;
    private int gridHeight = 80; // Default = 80
    private int gridWidth = 80; // Default = 80
    private int cellHeight = 5; // Default = 5
    private int cellWidth = 5; // Default = 5
    private int numberOfCellsInGrid;
    
    // Every m time steps cell division occurs, the following two variables are used to keep track of when
    // cell division should occur.
    private int timeStepForCellDivisionCounter = 1; // Default = 1
    private int numberOfTimeStepsForCellDivision = 8; // Default = 8
    
    private int nutrientForSustenance = 10; // Default = 10
    private int nutrientForGrowth = 60; // Default = 60
    private int thresholdForDivision = 100; // Default = 2600
    private double probabilityOfCellDivision = 0.5; // Default = 0.5 (value should be between 0 and 1).
    
    private String boundaryType = "reflecting"; // Default = "reflecting"
    private CRSMatrix updateMatrix;
    private DenseVector nutrientLevels;
    private String initalNutrientPattern = "default"; // Default = "default"
    
    // Rate of diffusion (value should be between 0 and 1).
    private double delta = 0.4; // Default = 0.4

    // Stores a value for each possible number of surrounding cells (0-8), which
    // is then used to determine if cell division takes place.
    private int[] crowdingFunctionValues = {0, 40, 40, 40, 30, 20, 10, 0, 0}; // Default = 0,40,40,40,30,20,10,0,0

    
    /* ****************************************************************************
	* Constructors
	*******************************************************************************/
    
    // Constructor which create a new rules object.
	public CellularAutomataBacteriaRules() {
        numberOfCellsInGrid = gridWidth * gridHeight;
        
    		grid = new Grid(gridHeight, gridWidth, cellHeight, cellWidth);
        
        updateMatrix = CRSMatrix.zero(numberOfCellsInGrid, numberOfCellsInGrid);
        nutrientLevels = DenseVector.zero(numberOfCellsInGrid);
        
        this.setInitialDefaultNutrientLevels();        
        this.createUpdateMatrix();
        
        grid.setBacteriumAlive(gridWidth/2, gridWidth/2);
	}
	
	// Constructor which creates a rules object with the parameters specified in an input file.
	public CellularAutomataBacteriaRules(File inputFile) throws IOException, IllegalArgumentException {
		// Reads the input file and sets the parameters.
		InputFileReader inputFileReader = new InputFileReader(inputFile, this);
		inputFileReader.setParametersFromInputFile();
		
        numberOfCellsInGrid = gridWidth * gridHeight;
        
        grid = new Grid(gridHeight, gridWidth, cellHeight, cellWidth);
        
        updateMatrix = CRSMatrix.zero(numberOfCellsInGrid, numberOfCellsInGrid);
        nutrientLevels = DenseVector.zero(numberOfCellsInGrid);
        
        this.setInitialNutrientLevels();        
        this.createUpdateMatrix();
        
        grid.setBacteriumAlive(gridWidth/2, gridWidth/2);
	}
	
	/* ****************************************************************************
	* Setters
	*******************************************************************************/

	// Set grid height.
	public void setGridHeight(int x) {
		gridHeight = x;
		
		// Must redefine certain parameters to reflect new grid size.
		this.createNewGridAfterDimensionChange();
		
		updateMatrix = CRSMatrix.zero(numberOfCellsInGrid, numberOfCellsInGrid);
        nutrientLevels = DenseVector.zero(numberOfCellsInGrid);
        
        this.setInitialNutrientLevels();        
        this.createUpdateMatrix();
	}
	
	// Set grid width.
	public void setGridWidth(int x) {
		gridWidth = x;
		
		// Must redefine certain parameters to reflect new grid size.
		this.createNewGridAfterDimensionChange();
		
		updateMatrix = CRSMatrix.zero(numberOfCellsInGrid, numberOfCellsInGrid);
        nutrientLevels = DenseVector.zero(numberOfCellsInGrid);
        
        this.setInitialNutrientLevels();        
        this.createUpdateMatrix();
	}
	
	// Set cell height.
	public void setCellHeight(int x) {
		cellHeight = x;
		this.createNewGridAfterDimensionChange();
	}
	
	// Set cell width.
	public void setCellWidth(int x) {
		cellWidth = x;
		this.createNewGridAfterDimensionChange();
	}
	
	// Set rate of diffusion (delta)
	public void setDiffusionRate(double x) {
		delta = x;
	}
	
	// Set nutrient for sustenance.
	public void setNutrientForSustenance(int x) {
		nutrientForSustenance = x;
	}
	
	// Set nutrient for growth.
	public void setNutrientForGrowth(int x) {
		nutrientForGrowth = x;
	}
	
	// Set threshold for cell division.
	public void setThresholdForCellDivision(int x) {
		thresholdForDivision = x;
	}
	
	// Set crowding function.
	public void setCrowdingFunctionValues(int[] x) {
		crowdingFunctionValues = x.clone();
	}
	
	// Set number of time steps for cell division.
	public void setNumberOfTimestepsForCellDivision(int x) {
		numberOfTimeStepsForCellDivision = x;
	}
	
	// Sets every cell to have 100 nutrient level.
    private void setInitialDefaultNutrientLevels() {
		for (int i=0; i<nutrientLevels.length(); i++) {
			nutrientLevels.set(i, 100.0);
		}
    }
    
    // Set initial nutrient pattern string.
    public void setNutrientLevelPatternChoice(String nutrientPattern) {
    		initalNutrientPattern = nutrientPattern;
    }
    
    // Sets the initial nutrient levels to one that is specified.
    private void setInitialNutrientLevels() {	
   		// Creates a nutrient pattern where there is a gap in the middle.
    		if (initalNutrientPattern.equals("absorbingmiddle")) {
    			// Make sure grid is at least 3 high.
    			if (gridHeight < 3) throw new IllegalArgumentException("Grid width not large enough for absorbing middle pattern.");
    				// Find values in the nutirent matrix that correspond to the middle row of the cellular automata.
    				int middleRow = (int)(gridHeight/2);
    				int nutrientMatrixValueForStartOfMiddleRow = returnPositionInNutrientMatrix(0, middleRow);
    				int nutrientMatrixValueForEndOfMiddleRow = nutrientMatrixValueForStartOfMiddleRow + gridWidth - 1;
    				
				for (int i=0; i<nutrientLevels.length(); i++) {
					// Set all cells to 100 except the middle column.
					if(!(i >= nutrientMatrixValueForStartOfMiddleRow && i <= nutrientMatrixValueForEndOfMiddleRow)) {
						nutrientLevels.set(i, 100.0);
					}
				}
    		}
    		// Set random nutrient level in each cell.
		if (initalNutrientPattern.equals("random")) {
			Random random = new Random();
			for (int i=0; i<nutrientLevels.length(); i++) {			
				nutrientLevels.set(i, (double)random.nextInt(101));
			}
		}
		else setInitialDefaultNutrientLevels();
    	}
    
    // Sets the nutrient levels based on the array of values passes to it.
    public void setNutrientLevelsToSpecifiedValues(double[] newNutrientLevels) {
    		// Reset nutrient levels.
    		nutrientLevels = DenseVector.zero(numberOfCellsInGrid);
    	
    		// Set new nutrient levels.
		for (int i=0; i<newNutrientLevels.length; i++) {
		    nutrientLevels.set(i, newNutrientLevels[i]);
		}
    }
    
    // Sets the nutrient level of the specified cell, to the amount of nutrient specified.
    public void setNutrientLevelOfCell (int i, double newNutrientLevel) {
    		nutrientLevels.set(i, newNutrientLevel);
    }
    
    // Sets the boundary condition to the string specified as an argument.
    public void setBoundaryCondition(String newBoundaryCondition) {
    		boundaryType = newBoundaryCondition;
    		this.createUpdateMatrix();		
    }
    
    public void setProbabilityOfCellDivision(double probabilty) {
    		probabilityOfCellDivision = probabilty;
    }
    
    /* ****************************************************************************
	* Getters
	*******************************************************************************/
    
    // Returns the nutrient level in the cell specified as an argument.
    public double getNutrientLevelOfCell (int i) {
    		return nutrientLevels.get(i);
    }
    
    // Returns the Grid object 'grid'.
 	public Grid getCellularAutomataGrid() {
 		return grid;
 	}
    
    /* ****************************************************************************
	* General Methods
	*******************************************************************************/
    
    // Creates a new grid after the grid width or height has being changed/set.
    public void createNewGridAfterDimensionChange() {
    		grid = new Grid(gridHeight, gridWidth, cellHeight, cellWidth);
    		numberOfCellsInGrid = gridHeight*gridWidth;
    }
    
    // Creates the update matrix specified by the boundaryType parameter.
    private void createUpdateMatrix() {
        if (boundaryType.equals("absorbent")) {
        		this.createUpdateMatrixForAbsorbentBoundary();
        }
        else if (boundaryType.equals("periodic")) {
    			this.createUpdateMatrixForPeriodicBoundary();
        }
        else {
        		this.createUpdateMatrixForReflectingBoundary();
        }
    }
    
    // Creates the update matrix for a cellular automata with a periodic boundary.
    private void createUpdateMatrixForPeriodicBoundary() {
    		// Loop through each cell and set the values for that row in that matrix.
        for (int i=0; i<numberOfCellsInGrid; i++) {
            // The cell itself.
	        updateMatrix.set(i, i, 1 - delta);
            // Cell to the left.
            if (i % gridWidth == 0) {
                updateMatrix.set(i + gridWidth - 1, i, delta/4);
            }
            else {
                updateMatrix.set(i-1, i, delta/4);
            }
            // Cell to the right.
            if ((i + 1) % gridWidth == 0) {
                updateMatrix.set(i - gridWidth + 1, i, delta/4);
            }
            else {
                updateMatrix.set(i + 1, i, delta/4);
            }
            // Cell above.
            if (i + gridWidth > numberOfCellsInGrid - 1) {
                updateMatrix.set(i - numberOfCellsInGrid + gridWidth, i, delta/4);
            }
            else {
                updateMatrix.set(i + gridWidth, i, delta/4);
            }
            // Cell below.
            if (i - gridWidth < 0) {
                updateMatrix.set(i + numberOfCellsInGrid - gridWidth, i, delta/4);
            }
            else {
                updateMatrix.set(i - gridWidth, i, delta/4);
            }
        }
    }
    
    // Creates the update matrix for a cellular automata with an absorbent boundary.
    private void createUpdateMatrixForAbsorbentBoundary() {
        for (int i=0; i<numberOfCellsInGrid; i++) {
        		// The cell itself.
	        updateMatrix.set(i, i, 1 - delta);
            // Cell to the left.
            if (i % gridWidth != 0) {
            		updateMatrix.set(i-1, i, delta/4);
            }
            // Cell to the right.
            if ((i + 1) % gridWidth != 0) {
            		updateMatrix.set(i + 1, i, delta/4);
            }
            // Cell above.
            if (!(i + gridWidth > numberOfCellsInGrid - 1)) {
            		updateMatrix.set(i + gridWidth, i, delta/4);
            }
            // Cell below.
            if (!(i - gridWidth < 0)) {
            		updateMatrix.set(i - gridWidth, i, delta/4);
            }
        }   
    }
    
 // Creates the update matrix for a cellular automata with a reflecting boundary.
    private void createUpdateMatrixForReflectingBoundary() {
        for (int i=0; i<numberOfCellsInGrid; i++) {
        		// The cell itself 
        		// (for corner cells)
        		if(i == 0 || i == gridWidth-1 || i == numberOfCellsInGrid - gridWidth || i == numberOfCellsInGrid - 1) {
        			updateMatrix.set(i, i, 1 - delta/2);
        		}
        		// (for left boundary)
        		else if (i % gridWidth == 0) {
        			updateMatrix.set(i, i, 1 - (3*delta/4));
        		}
        		// (for right boundary)
        		else if ((i+1) % gridWidth == 0) {
        			updateMatrix.set(i, i, 1 - (3*delta/4));
        		}
        		// (for lower boundary)
        		else if (i > numberOfCellsInGrid - gridWidth) {
        			updateMatrix.set(i, i, 1 - (3*delta/4));
        		}
        		// (for upper boundary)
        		else if (i < gridWidth - 1) {
        			updateMatrix.set(i, i, 1 - (3*delta/4));
        		}
        		// (any other position in the cellular automata, i.e. not on a boundary)
        		else {
        			updateMatrix.set(i, i, 1 - delta);
        		}
        		
            // Cell to the right.
            if ((i + 1) % gridWidth != 0) {
            		updateMatrix.set(i + 1, i, delta/4);
            }
            // Cell to the left.
            if (i % gridWidth != 0) {
            		updateMatrix.set(i-1, i, delta/4);
            }
            // Cell above.
            if (!(i + gridWidth > numberOfCellsInGrid - 1)) {
            		updateMatrix.set(i + gridWidth, i, delta/4);
            }
            // Cell below.
            if (!(i - gridWidth < 0)) {
            		updateMatrix.set(i - gridWidth, i, delta/4);
            }
        }   
    }
    
    // From 2D coordinates of a grid position, return the position this corresponds to in the 1D nutrient
    // matrix.
    public int returnPositionInNutrientMatrix(int x, int y) {
    		return x + y*gridWidth;
    }

    // Updates the nutrient levels for diffusion after a single time step.
    public void updateNutrientLevelsAfterDiffusion() {
        nutrientLevels = (DenseVector) updateMatrix.multiply(nutrientLevels);
        
        // Check if absorbing middle pattern has been selected.
		if (initalNutrientPattern.equals("absorbingmiddle")) {
			// Find values in the nutirent matrix that correspond to the middle row of the cellular automata.
			int middleRow = (int)(gridHeight/2);
			int nutrientMatrixValueForStartOfMiddleRow = returnPositionInNutrientMatrix(0, middleRow);
			int nutrientMatrixValueForEndOfMiddleRow = nutrientMatrixValueForStartOfMiddleRow + gridWidth - 1;
			
			// Remove nutrient from the absorbing middle section.
			for (int i=nutrientMatrixValueForStartOfMiddleRow; i<nutrientMatrixValueForEndOfMiddleRow + 1; i++) {
				nutrientLevels.set(i, 0.0);
			}
		}
    }

    // Updates nutrients levels after bacteria have consumed some nutrient.
    public void updateBacteriaAndNutrientAfterConsumptionAndCellDivision(Grid gridBeforeThisUpdate) {
    		// Variable that is set to true during time steps where cell division can occur.
    		boolean checkForCellDivision = false;
    		
    		// Check if it is the correct time step for cell division to take place.
		if (timeStepForCellDivisionCounter == numberOfTimeStepsForCellDivision) {
			checkForCellDivision = true;
		}
		
		// Loops through all the grid spaces in the cellular automata.
        for (int x=0; x<gridWidth; x++) {
	    		for (int y=0; y<gridHeight; y++) {
	    			// If grid is alive, then bacteria eat nutrient of the required amount to survive. If there
	    			// is not enough they will consume all the nutrient and then die.
	    			if (gridBeforeThisUpdate.cellAlive(x, y) == true) {
	    				updateAliveGridSpace(x, y);
	    			}
	    			// In the case where they is no alive or previously alive bacterium cell occupying the 
	    			// grid space.
	    			else if (gridBeforeThisUpdate.cellAliveOrContainsRemains(x, y) == false) {
	    				// Check value of flag used to indicate that cell division may occur this times step.
	    				if (checkForCellDivision == true) {
	    					updateEmptyGridSpace(gridBeforeThisUpdate, x, y);
	    				}
	    			}
	    			// Set the colour of the grid cell.
	    			this.grid.setNutrientLevelColor(x, y, 0, getNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y))/100, 1);
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
    
    
    // Carries out the necessary updates to the nutrient matrix and to the bacteria for a grid space that
    // contains an alive cell.
    private void updateAliveGridSpace(int x, int y) {
    		// Checks if there is enough food for the bacteria to survive, and then updates the nutrient
    		// and bacteria accordingly.
	    	if (nutrientLevels.get(returnPositionInNutrientMatrix(x, y)) >= 10) {
			setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y), 
			nutrientLevels.get(returnPositionInNutrientMatrix(x, y))-nutrientForSustenance);
		}
		else {
			this.grid.setBacteriumDead(x, y);
			setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y),0);
		}
    }
    
    // Carries out the necessary update for an empty grid cell, for time steps in which cell division can
    // occur.
    private void updateEmptyGridSpace(Grid gridBeforeThisUpdate, int x, int y) {
    		// Checks if cell division conditions are met.
	    	if (shouldCellDivisionOccur(gridBeforeThisUpdate, x, y) && nutrientLevels.get(returnPositionInNutrientMatrix(x, y)) >= 60) {
			this.grid.setBacteriumAlive(x, y);
			setNutrientLevelOfCell(returnPositionInNutrientMatrix(x, y), 
						nutrientLevels.get(returnPositionInNutrientMatrix(x, y))-nutrientForGrowth);
		}
    }
    
    // Checks whether the conditions for cell division to occur are met.
    // Cell growth may take place in a grid space, if for that cell the product of the crowding function 
    // and the food in the cell, is greater than a given threshold for division. If it is greater than the
    // threshold then bacterium cell will appear in the grid space with probability 50%.
    public boolean shouldCellDivisionOccur(Grid gridBeforeUpdate, int x, int y) {   	
    		double nutrientInCell = nutrientLevels.get(returnPositionInNutrientMatrix(x, y));
    		int numberOfNeighbours; 
    		
    		// Check boundary condition type and find number of neighbours for the cell.
    		if (boundaryType.equals("periodic")) {
    			numberOfNeighbours = returnNumberOfAliveNeighboursForPeriodicGrid(gridBeforeUpdate, x, y);
    		}
    		else {
    			numberOfNeighbours = returnNumberOfAliveNeighboursForRelectingOrAbsorbentGrids(gridBeforeUpdate, x, y);
    		}
    		
    		// Check if crowding function * nutrient level is greater than threshold.
    		if (crowdingFunctionValues[numberOfNeighbours] * nutrientInCell > thresholdForDivision) {
    			// If a random number from 0 up to 1 is less than 0.5 then cell division takes place.
    			if (Math.random() < probabilityOfCellDivision) {
    				return true;
    			}
    		}
    		
    		return false;
    }
    
    // Returns the number of alive neighbours of cell x,y in the grid passed to this function.
    public int returnNumberOfAliveNeighboursForPeriodicGrid(Grid gridBeforeThisUpdate, int x, int y) {
		int numberOfNeighbours = 0;

		// Loops through 9 cells, the cell in question along with the surrounding 8.
    		for (int col = (x-1); col<(x+2); col++) {
    			for (int row = (y-1); row<(y+2); row++) {
                int tempRow = row;
                int tempCol = col;

                if (tempCol == gridWidth)
                    tempCol = 0;
                else if (tempCol == -1)
                    tempCol = gridWidth - 1;

                if (tempRow == gridHeight)
                    tempRow = 0;
                else if (tempRow == -1)
                    tempRow = gridHeight - 1;

                if (gridBeforeThisUpdate.cellAlive(tempCol, tempRow) == true) {
    					// Make sure it is not the cell itself that is being counted.
    					if (tempCol != x || tempRow != y) {
    						numberOfNeighbours++;
    					}
				}
    			}
    		}
    		
    		return numberOfNeighbours;
    }
    
    // Returns the number of alive neighbours of cell x,y in the grid passed to this function.
    public int returnNumberOfAliveNeighboursForRelectingOrAbsorbentGrids(Grid gridBeforeThisUpdate, int x, int y) {
		int numberOfNeighbours = 0;

		// Loops through 9 cells, the cell in question along with the surrounding 8.
    		for (int col = (x-1); col<(x+2); col++) {
    			for (int row = (y-1); row<(y+2); row++) {
                int tempRow = row;
                int tempCol = col;

                	// If cell is not an edge cell.
                if (!(tempCol == gridWidth) && !(tempCol == -1)  && !(tempRow == gridHeight)  && !(tempRow == -1)) {
                		// If cell is alive.
					if (gridBeforeThisUpdate.cellAlive(tempCol, tempRow) == true) {
						// Make sure it is not the cell itself that is being counted.
						if (tempCol != x || tempRow != y) {
							numberOfNeighbours++;
						}
					}
                }     
    			}
    		}
    		
    		return numberOfNeighbours;
    }

	// Creates an updated grid after one iteration of the rules governing the bacterial colony.
	public void createUpdatedGrid() {
		// Creates a copy of the current grid.
		Grid copyOfCurrentGrid = new Grid(this.grid);
		
		// Update for diffusion.
        this.updateNutrientLevelsAfterDiffusion();
        
        // Update for bacteria consuming nutrient and reproducing.
        this.updateBacteriaAndNutrientAfterConsumptionAndCellDivision(copyOfCurrentGrid);
	}
}
