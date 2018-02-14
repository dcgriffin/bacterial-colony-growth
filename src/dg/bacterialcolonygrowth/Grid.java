/* *****************************************************************************
* Description: A class used to represent the grid of a cellular automata.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import static org.junit.Assert.assertFalse;

import javafx.scene.paint.Color;

public class Grid {

	private Cell[][] cells;
    private int width, height;

	// Constructor which creates an x by y sized array of Cells.
	public Grid(int x, int y) {
        width = x;
        height = y;
		cells = new Cell[width][height];
	}

    // Returns the width of the grid.
    public int getGridWidth() {
        return width;
    }

    // Returns the height of the grid.
    public int getGridHeight() {
        return height;
    }

	// Adds a Cell to the array of cells in the grid.
	public void add(Cell c, int x, int y) {
		cells[x][y] = c;
	}
	
	// Sets the colour of the specified cell using hue, saturation and brightness.
	public void setNutrientLevelColor(int x, int y, double hue, double saturation, double brightness) {
		cells[x][y].setColorOfCell(hue, saturation, brightness);
	}

	// Turns a cell white to represent a dead state.
	public void setBacteriumDead (int x, int y) {
		cells[x][y].setBacteriumDead();
	}

	// Turns a cell black to represent an alive state.
	public void setBacteriumAlive (int x, int y) {
		cells[x][y].setBacteriumAlive();
	}

	// Returns 'true' if cell is alive and 'false' if it is dead.
	public Boolean cellStatus(int x, int y) {
		return cells[x][y].cellStatus();
	}

    // Resets the grid so all the cells are white/dead.
    public void resetGrid() {
        for (int x=0; x<width; x++)
            for (int y=0; y<height; y++)
            		setBacteriumDead(x,y);
    }

	// Returns true if there are any live cells left in the grid visible to the user.
	public Boolean gridStatus() {
		for (int x=0; x<width; x++) {
	    		for (int y=0; y<height; y++) {
			        if (this.cellStatus(x, y) == true) {
			        		return true;
			        }
	    		}
        }
		return false;
	}
}
