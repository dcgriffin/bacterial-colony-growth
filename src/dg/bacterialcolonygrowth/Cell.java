/* *****************************************************************************
* Description: A class used to represent and store the details of the cells in
* a cellular automata grid. It extends the Rectangle Class.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {

    // Constructor which creates a new Cell.
    public Cell(int cellHeight, int cellWidth) {
        super(cellHeight, cellWidth, Color.WHITE);
    }

    // Returns 'true' if cell is alive and 'false' if it is dead.
    public Boolean cellStatus() {
        if (this.getFill() == Color.BLACK)
            return true;
        else
            return false;
    }

    // Turns a cell black to represent an alive state.
    public void turnCellBlack () {
        this.setFill(Color.BLACK);
    }

    // Turns a cell white to represent a dead state.
    public void turnCellWhite () {
        this.setFill(Color.WHITE);
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
