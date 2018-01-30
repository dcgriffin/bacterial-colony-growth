/* *****************************************************************************
* Description: A class used to simulate a bacterial colony.
* It is not meant to be instantiated. It just contains static methods that carry
* out the algorithm to simulate the rules of the game. The grid wraps around so
* the bottom meets up with the top, and the left side meets up with the right side.
*
* CURRENTLY IMPLEMENTS GAME OF LIFE AS A STARTING POINT
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.jblas.FloatMatrix;

public class CellularAutomataRules {
    private int width;
    private int height;
    private int numberOfGridSpaces;
    // Rate of diffusion (value should be between 0 and 1).
    private int delta;
    private FloatMatrix updateMatrixPeriodicBoundary;

	public CellularAutomataRules(int x, int y) {
        width = x;
        height = y;
        delta = 1;
        updateMatrixPeriodicBoundary = FloatMatrix.zeros(width, height);
	}

	// Creates an updated grid of the current grid after applying the
    // rules of the game. The rules are:
    // -Any live cell with fewer than two live neighbours dies, as if caused by under-population.
    // -Any live cell with two or three live neighbours lives on to the next generation.
    // -Any live cell with more than three live neighbours dies, as if by over-population.
    // -Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
	public void createUpdatedGrid(Grid currentGrid) {
		Grid tempGrid = createNewGrid();

		// Loops through each cell of the grid and checks if it is dead or alive.
		// It then calls another function to handle that cell depending on
		// whether it is dead or alive.
    	for (int x=0; x<width; x++) {
    		for (int y=0; y<height; y++) {
		        if (currentGrid.cellStatus(x, y) == true)
		        	liveCellNeighbourChecker(currentGrid, tempGrid, x, y);
		        else
		        	deadCellNeighbourChecker(currentGrid, tempGrid, x, y);
    		}
        }

    	copyTempGridToCurrentGrid(currentGrid, tempGrid);
	}

	// Creates a Grid object which contains a 40 by 40 grid of Rectangles, all white.
	public Grid createNewGrid() {
		Grid newGrid = new Grid(width, height);

    	for (int x=0; x<width; x++) {
    		for (int y=0; y<height; y++) {
		        Rectangle r = new Rectangle(15,15, Color.WHITE);
		        newGrid.add(r, x, y);
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
			tempGrid.turnCellBlack(x, y);
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
			tempGrid.turnCellBlack(x, y);
	}

	// Copies the tempGrid to the currentGrid. This is done once the tempGrid
	// has been fully completed to show the next stage after the what the
	// currentGrid shows.
	public void copyTempGridToCurrentGrid(Grid currentGrid, Grid tempGrid) {
    	for (int x=0; x<width; x++) {
    		for (int y=0; y<height; y++) {
		        if (tempGrid.cellStatus(x, y) == true)
		        	currentGrid.turnCellBlack(x, y);
		        else
		        	currentGrid.turnCellWhite(x, y);
    		}
        }
	}
}
