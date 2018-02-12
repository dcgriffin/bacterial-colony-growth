/* *****************************************************************************
* Description: A class used to represent and store the details of the cells in
* a cellular automata grid. It extends the Rectangle Class.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane { //extends StackPane
	
	private Rectangle cellArea;
	private Circle bacteria;

    // Constructor which creates a new Cell.
    public Cell(int cellHeight, int cellWidth, Color color) {
        cellArea  = new Rectangle(cellHeight, cellWidth, color);
        cellArea.setStroke(Color.GRAY);
        bacteria = new Circle(cellWidth/3);
        bacteria.setFill(Color.BLACK);
        this.getChildren().addAll(cellArea, bacteria);
    }
    
    public void setColorOfCell(double hue, double saturation, double brightness) {
    		cellArea.setFill(Color.hsb(hue, saturation, brightness));
    		if (this.cellStatus() == false) {
    			bacteria.setFill(Color.hsb(hue, saturation, brightness));
    		}
    }

    // Returns 'true' if cell is alive and 'false' if it is dead.
    public Boolean cellStatus() {
        if (bacteria.getFill() == Color.BLACK)
            return true;
        else
            return false;
    }

    // Turns a cell black to represent an alive state.
    public void turnCellBlack () {
        bacteria.setFill(Color.BLACK);
    }

    // Turns a cell white to represent a dead state.
    public void turnCellWhite () {
        bacteria.setFill(Color.WHITE);
    }

    // A method that is called when a cell is clicked. It changes the dead/alive
    // status of the cell.
    public void cellClicked () {
        if (this.cellStatus() == true)
            this.turnCellWhite();
        else
            this.turnCellBlack();
    }
}
