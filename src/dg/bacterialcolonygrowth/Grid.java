/* *****************************************************************************
* Description: A class used to represent the grid of a cellular automata.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import org.junit.validator.PublicClassValidator;

import javafx.scene.paint.Color;


public class Grid {

	private Cell[][] cells;
    private int width, height;

	// Constructor which creates an x by y sized array of Cells.
	public Grid(int x, int y, int cellHeight, int cellWidth) {
        width = x;
        height = y;   
        cells = new Cell[width][height];
        
        // Adds each cell to the cells array.
        for (int i=0; i<width; i++) {
	    		for (int j=0; j<height; j++) {
	    			cells[i][j] = new Cell(cellHeight, cellWidth, Color.WHITE);
	    		}
        }	
	}
	
	// Constructor used to create a deep copy of the grid passed to it.
	public Grid(Grid currentGrid) {
		width = currentGrid.getGridWidth();
        height = currentGrid.getGridHeight();
        Cell[][] currentGridCells = currentGrid.getCells();
        cells = new Cell[width][height];
        
        // For every cell in the original grid a new cell is created that is a copy, and then added to the 
        // new grid.
        for (int i=0; i<width; i++) {
        		for (int j=0; j<height; j++) {
        			Cell c = new Cell(currentGridCells[i][j]);
        			this.add(c, i, j);
        		}
        }
    }

    // Returns the width of the grid.
    public int getGridWidth() {
        return width;
    }

    // Returns the height of the grid.
    public int getGridHeight() {
        return height;
    }
    
    // Returns the Cell array.
    public Cell[][] getCells() {
    		return cells;
    }

	// Adds a Cell to the array of cells in the grid.
	public void add(Cell c, int x, int y) {
		cells[x][y] = c;
	}
	
	// Sets the colour of the specified cell using hue, saturation and brightness.
	public void setNutrientLevelColor(int x, int y, double hue, double saturation, double brightness) {
		cells[x][y].setColorOfCell(hue, saturation, brightness);
	}

	// Turns a cell grey to represent a previously alive state.
	public void setBacteriumDead (int x, int y) {
		cells[x][y].setBacteriumDead();
	}

	// Turns a cell black to represent an alive state.
	public void setBacteriumAlive (int x, int y) {
		cells[x][y].setBacteriumAlive();
	}
	
	// Turns the bacteria part of the cell white to represent no bacteria existing there.
    public void setBacteriumEmpty (int x, int y) {
    		cells[x][y].setBacteriumEmpty();
    }

	// Returns 'true' if cell is alive and 'false' if it is dead.
	public Boolean cellAlive(int x, int y) {
		return cells[x][y].cellAlive();
	}
	
	// Returns 'true' if cell is alive and 'false' if it is dead.
		public Boolean cellAliveOrContainsRemains(int x, int y) {
			return cells[x][y].cellAliveOrContainsRemains();
		}
	
	// Sets the parameters of the program to those specified in the input file.
	public void loadInputFile() {
		
	}

    // Shows only bacteria as squares and removes the nutrient as a way of viewing the final image.
    public void showOnlyBacteriaAsSquares() {
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
            		// If cell contains either alive or dead bacteria, then remove the bacteria circle and
            		// set the grid space & border to be completely black.
	            	if (cells[x][y].cellAliveOrContainsRemains()) {
	            		cells[x][y].setBacteriumEmpty();
	            		cells[x][y].setColorOfCell(0, 0, 0);
	            		cells[x][y].setGridSpaceBorderColor(Color.BLACK);
	            	}
	            	else {
	            		// Set colour of cell and border to white.
	            		cells[x][y].setColorOfCell(0, 0, 1);
	            		cells[x][y].setGridSpaceBorderColor(Color.WHITE);
	            	}           	
            }
        }	
    }
}
