/* *****************************************************************************
* Description: A class used to represent and store the details of the cells in
* a cellular automata grid. It extends the Rectangle Class.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import com.sun.org.apache.bcel.internal.generic.RETURN;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {
	
	private Rectangle cellArea;
	private Circle bacteria;

    // Constructor which creates a new Cell.
    public Cell(int cellHeight, int cellWidth, Color color) {
        cellArea  = new Rectangle(cellHeight, cellWidth, color);
        cellArea.setStroke(Color.GRAY);
        bacteria = new Circle(cellWidth/2.5);
        bacteria.setFill(Color.WHITE);
        this.getChildren().addAll(cellArea, bacteria);
    }
    
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

    // A method that is called when a cell is clicked. It changes the dead/alive
    // status of the cell.
    public void cellClicked () {
        if (this.cellAlive() == true)
            this.setBacteriumEmpty();
        else
            this.setBacteriumAlive();
    }
}
