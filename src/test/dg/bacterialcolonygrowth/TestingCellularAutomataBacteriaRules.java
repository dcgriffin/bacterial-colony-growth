package dg.bacterialcolonygrowth;

import static org.junit.Assert.*;
import org.junit.Test;

import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;

public class TestingCellularAutomataBacteriaRules {
	
	// Test the default constructor creates a grid with initial nutrient levels set to 100.
	@Test
	public void testDefaultConstructor() {
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
		
		// Get size of grid.
		Grid grid = rules.getCellularAutomataGrid();
		int height = grid.getGridHeight();
		int width = grid.getGridWidth();
		
		// Test first cell, last cell and a middle cell are all set to 100 nutrient level.
		assertEquals(100, rules.getNutrientLevelOfCell(0), 0);
		assertEquals(100, rules.getNutrientLevelOfCell((height*width - 1)/2), 0);
		assertEquals(100, rules.getNutrientLevelOfCell(height*width - 1), 0);
	}
	
	// Test diffusion after one time step.
	@Test
	public void testNutrientDiffusesCorrectlyForPeriodicBoundary() {
		double delta = 0.5;
		double[] initialNutrientLevels = new double[] {0,0,0,0,90.0,0,0,0,0};
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
		
		// Make cellular automata 3 x 3.
		rules.setGridHeight(3);
		rules.setGridWidth(3);
		
		// Set inital nutirent and diffusion rate.
		rules.setNutrientLevelsToSpecifiedValues(initialNutrientLevels);
		rules.setDiffusionRate(delta);
		rules.setBoundaryCondition("periodic");
		
		// Test after one time step.
		rules.updateNutrientLevelsAfterDiffusion();
		assertEquals(0, rules.getNutrientLevelOfCell(0),  0);
		assertEquals(11.25, rules.getNutrientLevelOfCell(1), 0);
		assertEquals(0, rules.getNutrientLevelOfCell(2), 0);
		assertEquals(11.25, rules.getNutrientLevelOfCell(3), 0);
		assertEquals(45, rules.getNutrientLevelOfCell(4), 0);
		assertEquals(11.25, rules.getNutrientLevelOfCell(5), 0);
		assertEquals(0, rules.getNutrientLevelOfCell(6), 0);
		assertEquals(11.25, rules.getNutrientLevelOfCell(7), 0);
		assertEquals(0, rules.getNutrientLevelOfCell(8), 0);
		
		// After two time steps
		rules.updateNutrientLevelsAfterDiffusion();
		assertEquals(2.8125, rules.getNutrientLevelOfCell(0), 0);
		assertEquals(12.65625, rules.getNutrientLevelOfCell(1), 0);
		assertEquals(2.8125, rules.getNutrientLevelOfCell(2), 0);
		assertEquals(12.65625, rules.getNutrientLevelOfCell(3), 0);
		assertEquals(8.125, rules.getNutrientLevelOfCell(4), 20);
		assertEquals(12.65625, rules.getNutrientLevelOfCell(5), 0);
		assertEquals(2.8125, rules.getNutrientLevelOfCell(6), 0);
		assertEquals(12.65625, rules.getNutrientLevelOfCell(7), 0);
		assertEquals(2.8125, rules.getNutrientLevelOfCell(8), 0);
		
		// After 100 time steps
		for (int i=0; i<100; i++) {
			rules.updateNutrientLevelsAfterDiffusion();
		}
		
		assertEquals(10, rules.getNutrientLevelOfCell(0), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(1), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(2), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(3), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(4), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(5), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(6), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(7), 0);
		assertEquals(10, rules.getNutrientLevelOfCell(8), 0);
		
	}
	
	// Tests the number of alive neighbours function works correctly for periodic boundary.
	@Test
	public void testNumberOfAliveNeighboursFunctionForPeriodicBoundary() {
		int height = 3;
		int width = 3;
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
		
		// Make cellular automata height x width.
		rules.setGridHeight(height);
		rules.setGridWidth(width);
		
		Grid grid = rules.getCellularAutomataGrid();
	
		// No bacteria should initially be present, so check each cell has 0 live neighbours.
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 0), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 1), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 2), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 0), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 1), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 2), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 0), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 1), 0);
		assertEquals(0, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 2), 0);
		
		// Set bacteria alive in each cell.
		for (int x=0; x<width; x++) {
    			for (int y=0; y<height; y++) {
		        grid.setBacteriumAlive(x, y);
    			}
		}
		
		// Check each cell now has 8 live neighbours.
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 0), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 1), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 2), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 0), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 1), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 2), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 0), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 1), 0);
		assertEquals(8, rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 2), 0);
	}
	
	// Test nutrient levels correctly update for bacteria consumption of nutrient.
	@Test
	public void testNutrientLevelsUpdateCorrectlyForBacteriaConsumption() {
		int width = 3;
		int height = 3;
		double delta = 0.5;
		double[] initialNutrientLevels = new double[] {50,50,50,50,50,50,50,50,50};
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
		
		// Make width x height cellular automata with specified nutrient levels and delta value.
		rules.setGridHeight(height);
		rules.setGridWidth(width);
		rules.setNutrientLevelsToSpecifiedValues(initialNutrientLevels);
		rules.setDiffusionRate(delta);
		
		Grid grid = new Grid(width, height, 7, 7);
		
		// Set bacteria alive in each cell.
		for (int x=0; x<width; x++) {
    			for (int y=0; y<height; y++) {
		        grid.setBacteriumAlive(x,y);
    			}
		}
		
		grid.setBacteriumDead(0, 1);
		grid.setBacteriumDead(1, 2);
		grid.setBacteriumDead(2, 2);
		
		Grid copyOfGrid = new Grid(grid);
		
		rules.updateBacteriaAndNutrientAfterConsumptionAndCellDivision(copyOfGrid);
		
		assertEquals(40, rules.getNutrientLevelOfCell(0), 0);
		assertEquals(40, rules.getNutrientLevelOfCell(1), 0);
		assertEquals(40, rules.getNutrientLevelOfCell(2), 0);
		assertEquals(50, rules.getNutrientLevelOfCell(3), 0);
		assertEquals(40, rules.getNutrientLevelOfCell(4), 0);
		assertEquals(40, rules.getNutrientLevelOfCell(5), 0);
		assertEquals(40, rules.getNutrientLevelOfCell(6), 0);
		assertEquals(50, rules.getNutrientLevelOfCell(7), 0);
		assertEquals(50, rules.getNutrientLevelOfCell(8), 0);
	}
	
	// Test function should cell division occur for cases when it definitely shoudn't. Can't test cases
	// when it may do because of the inherent randomness in the process.
	@Test
	public void testShouldCellDivisionOccur() {
		int width = 3;
	    int height = 3;
	    double[] initialNutrientLevels = new double[] {0,0,0,0,0,0,0,0,0};
	    
	    CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
	    
	    // Make width x height cellular automata with specified nutrient levels and delta value.
 		rules.setGridHeight(height);
 		rules.setGridWidth(width);
 		rules.setNutrientLevelsToSpecifiedValues(initialNutrientLevels);
	    
	    Grid grid = rules.getCellularAutomataGrid();
	    
	    // Set top row bacteria to be alive.
 		for (int x=0; x<width; x++) {
 			grid.setBacteriumAlive(x,0);
 		}
	    
	    // In the case with no nutrient, cell division should not occur even with 3 bacteria alive in the
 		// grid of 9 cell spaces.
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 0));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 2));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 1, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 2, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 2));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 1, 2));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 2, 2));
	    
	    // Create a new cellular automata with 100 nutrient.
	    rules = new CellularAutomataBacteriaRules();
	    
	    // Make the new cellularr automata width x height size with specified nutrient levels and delta value.
 		rules.setGridHeight(height);
 		rules.setGridWidth(width);
	    
	    grid = rules.getCellularAutomataGrid();
	    
	    // All bacteria in the new grid are initially dead, so cell division should not occur even with
	    // sufficient nutrient.
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 0));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 2));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 1, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 2, 1));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 0, 2));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 1, 2));
	    assertFalse(rules.shouldCellDivisionOccur(grid, 2, 2));
	}
	
	// Test a specific cells nutrient level can be set correctly.
	@Test
	public void testSetNutrientLevelOfCell() {
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
		
		rules.setNutrientLevelOfCell(0, 24);
		assertEquals(24, rules.getNutrientLevelOfCell(0), 0);
	}
	
	
	// Test correct position on 1D nutrient array is returned for a given x,y grid position.
	@Test
	public void testCorrect1DNutrientArrayPositionIsReturnedForAGridPosition() {
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules();
		
		// Make cellular automata 3 x 3.
		rules.setGridHeight(3);
		rules.setGridWidth(3);
		
		assertEquals(0, rules.returnPositionInNutrientMatrix(0, 0), 0);
		assertEquals(1, rules.returnPositionInNutrientMatrix(1, 0), 0);
		assertEquals(2, rules.returnPositionInNutrientMatrix(2, 0), 0);
		assertEquals(3, rules.returnPositionInNutrientMatrix(0, 1), 0);
		assertEquals(4, rules.returnPositionInNutrientMatrix(1, 1), 0);
		assertEquals(5, rules.returnPositionInNutrientMatrix(2, 1), 0);
		assertEquals(6, rules.returnPositionInNutrientMatrix(0, 2), 0);
		assertEquals(7, rules.returnPositionInNutrientMatrix(1, 2), 0);
		assertEquals(8, rules.returnPositionInNutrientMatrix(2, 2), 0);
	}
}
