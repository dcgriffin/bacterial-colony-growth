package dg.bacterialcolonygrowth;

import static org.junit.Assert.*;
import org.junit.Test;

import javafx.scene.paint.Color;

public class TestingCellAndGridClasses {
	
	// Test bacteria in a cell changes color appropriately.
	@Test
	public void testBacteriaChangeColorCorrectlyUsingCellClassMethods() {
		Cell cell = new Cell(10, 10, Color.WHITE);
		
		// Bacteria should initially be dead.
		assertFalse(cell.cellAlive());
		
		// Changing color of cell within the red-white spectrum should not change color of bacteria.
		cell.setColorOfCell(0, 1, 1);
		assertFalse(cell.cellAlive());
		
		// In this case changing color of cell should not change the bacteria part.
		cell.setBacteriumAlive();
		cell.setColorOfCell(0, 0, 1);
		assertTrue(cell.cellAlive());
		
		// Check cell is correctly set to dead.
		cell.setBacteriumDead();
		assertFalse(cell.cellAlive());
		assertTrue(cell.cellAliveOrContainsRemains());
	}
	
	// Test a particular cells bacterium part can be set to alive within a Grid.
	@Test
	public void testBactriaChangeColorCorrectlyUsingGridClassMethods() {
		Grid grid = new Grid(3,3,7,7);
		
		// Bacteria should initially be dead.
		assertFalse(grid.cellAlive(0, 0));
		
		// Bacteria should still be dead after changing nutrient level.
		grid.setNutrientLevelColor(0, 0, 0, 1, 1);
		assertFalse(grid.cellAlive(0, 0));
		
		// Changing the nutrient level should not change the bacterium part when it is alive.
		grid.setBacteriumAlive(0, 0);
		grid.setNutrientLevelColor(0, 0, 0, 0, 1);
		assertTrue(grid.cellAlive(0, 0));
		
		// Check cell correctly changes to dead.
		grid.setBacteriumDead(0, 0);
		assertFalse(grid.cellAlive(0, 0));		
	}
	
	// Test correct size grid is created.
	@Test
	public void testCorrectSizedGridIsCreated() {
		Grid grid = new Grid(5, 5, 7, 7);
		
		assertEquals(5, grid.getGridHeight(), 0);
		assertEquals(5, grid.getGridWidth(), 0);
	}
	
	// Tests that the deep copy constructor correctly creates a deep copy of the grid passed to it.
	@Test
	public void testDeepCopyContructorForGrid() {
		Grid grid = new Grid(2, 2, 7, 7);
		
		grid.setBacteriumAlive(0, 0);
		
		// Test initial alive or non existent status for the bacteria is set correctly.
		assertTrue(grid.cellAlive(0, 0));
		assertFalse(grid.cellAlive(0, 1));
		
		// Create a deep copy of grid.
		Grid deepCopyGrid = new Grid(grid);
		
		// Test deepCopyGrid has the correct size.
		assertEquals(2, deepCopyGrid.getGridHeight());
		assertEquals(2, deepCopyGrid.getGridWidth());
		
		// Change values in deep copy and check that it doesn't change the values in the original object.
		deepCopyGrid.setBacteriumAlive(0, 1);
		deepCopyGrid.setBacteriumEmpty(0, 0);
		
		assertTrue(grid.cellAlive(0, 0));
		assertFalse(grid.cellAlive(0, 1));
	}
	
	// Tests that the deep copy constructor correctly creates a deep copy of the cell passed to it.
	@Test
	public void testDeepCopyContructorForCell() {
		Cell cell = new Cell(5, 5, Color.WHITE);
		Cell deepCopyCell = new Cell(cell);
		
		// Change colour of the cell and make sure that the original cell hasn't changed colour.
		deepCopyCell.setColorOfCell(53, 0.5, 1);
		assertEquals(Color.WHITE, cell.getCellAreaColor());
	}
}
