/* *****************************************************************************
* Description: A class used to represent and store the details of the cells in
* a cellular automata grid. It extends the Rectangle Class.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sun.tracing.dtrace.ProviderAttributes;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {
	
	// Represents a square grid space whose colour shows the level of nutrient.
	private Rectangle cellArea;
	// Represents the space in the gird a bacterium cell can occupy. Its colour shows the presence or lack
	// of a bacterium cell.
	private Circle bacteria;
	
	private int height;
	private int width;

    // Constructor which creates a new Cell.
    public Cell(int cellHeight, int cellWidth, Color color) {
    		height = cellHeight;
    		width = cellWidth;
        cellArea = new Rectangle(height, width, color);
        cellArea.setStroke(Color.GRAY);
        bacteria = new Circle(width/2.5);
        bacteria.setFill(Color.WHITE);
        this.getChildren().addAll(cellArea, bacteria);
    }
    
    // Constructor used to create a deep copy of the grid passed to it.
    public Cell(Cell originalCell) {
    		height = originalCell.returnHeight();
    		width = originalCell.returnWidth();
    		
    		cellArea = new Rectangle(height, width, originalCell.getCellAreaColor());
        cellArea.setStroke(Color.GRAY); 
        bacteria = new Circle(width/2.5);
        bacteria.setFill(originalCell.getBacteriaAreaColor());
        
        this.getChildren().addAll(cellArea, bacteria);
    }
    
    // Returns the height of the cell.
    public int returnHeight() {
    		return height;
    }
    
    //Returns the width of the cell.
    public int returnWidth() {
		return width;
    }
    
    // Returns the colour of the cell area.
    public Paint getCellAreaColor() {
    		return cellArea.getFill();
    }
    
    // Returns the colour of the bacteria area.
    public Paint getBacteriaAreaColor() {
		return bacteria.getFill();
    }
    
    // Sets the border colour of the grid space.
    public void setGridSpaceBorderColor(Color color) {
    		cellArea.setStroke(color);
    }
    
    // Sets the colour of the grid cell, excluding the bacteria part if one is present.
    public void setColorOfCell(double hue, double saturation, double brightness) {
    		cellArea.setFill(Color.hsb(hue, saturation, brightness));
    		if (this.cellAliveOrContainsRemains() == false) {
    			bacteria.setFill(Color.hsb(hue, saturation, brightness));
    		}
    }

    // Returns 'true' if cell is alive and 'false' if it is dead.
    public boolean cellAlive() {
        if (bacteria.getFill() == Color.BLACK)
            return true;
        else
            return false;
    }
    
    // Return true if an alive or the dead remains of a bacterium cell occupy the grid cell.
    public boolean cellAliveOrContainsRemains() {
    		if (bacteria.getFill() == Color.BLACK || bacteria.getFill() == Color.GREY) {
    			return true;
    		}
    		
    		return false;
    }

    // Turns the bacteria part of a cell black to represent an alive state.
    public void setBacteriumAlive () {
        bacteria.setFill(Color.BLACK);
    }

    // Turns the bacteria part of the cell grey to represent a dead bacterium cell.
    public void setBacteriumDead () {
        bacteria.setFill(Color.GREY);
    }
    
    // Turns the bacteria part of the cell white to represent no bacteria existing there.
    public void setBacteriumEmpty () {
        bacteria.setFill(Color.WHITE);
    }

    // A method that is called when a grid cell is clicked. It changes the empty/alive
    // status of the bacteria part of the grid cell.
    public void cellClicked () {
        if (this.cellAlive() == true)
            this.setBacteriumEmpty();
        else
            this.setBacteriumAlive();
    }
}
