/* *****************************************************************************
* Description: A class used to represent the grid of a cellular automata.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grid {

	private Rectangle[][] cells;
    private int gridSize;

	// Constructor which creates a gridSize by gridSize array for the Rectangles.
	public Grid(int sizeOfGrid) {
        gridSize = sizeOfGrid;
		cells = new Rectangle[gridSize][gridSize];
	}

    // Returns the size of the grid.
    public int getGridSize() {
        return gridSize;
    }

	// Adds a Rectangle to the array of cells in the grid.
	public void add(Rectangle r, int x, int y) {
		cells[x][y] = r;
	}

	// Turns a cell white to represent a dead state.
	public void turnCellWhite (int x, int y) {
		cells[x][y].setFill(Color.WHITE);
	}

	// Turns a cell black to represent an alive state.
	public void turnCellBlack (int x, int y) {
		cells[x][y].setFill(Color.BLACK);
	}

	// Returns 'true' if cell is alive and 'false' if it is dead.
	public Boolean cellStatus(int x, int y){
		if (cells[x][y].getFill() == Color.BLACK)
			return true;
		else
			return false;
	}

    // Resets the grid so all the cells are white/dead.
    public void resetGrid() {
        for (int x=0; x<gridSize; x++)
            for (int y=0; y<gridSize; y++)
                turnCellWhite(x,y);
    }

	// Returns true if there are any live cells left in the grid visible to the user.
	public Boolean gridStatus() {
		for (int x=0; x<gridSize; x++) {
    		for (int y=1; y<gridSize; y++) {
		        if (this.cellStatus(x, y) == true) {
		        	return true;
		        }
    		}
        }
		return false;
	}
}
