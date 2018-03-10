package dg.bacterialcolonygrowth;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestingCellularAutomataRules {

//	@Before
//	public void setUp() throws Exception {	
//	}

//	@After
//	public void tearDown() throws Exception {
//	}
	
	// Test nutrient levels are set correctly.
	@Test
	public void testNutrientLevelsAreSetCorrectly() {
		int width = 3;
	    int height = 3;
	    double delta = 0.4;
	    double[] initialNutrientLevels = new double[] {0,10.5,45.8,100.0,0.3,98,100,64.2,25};
	    CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules(width, height, initialNutrientLevels, delta);
	    
	    assertEquals(rules.getNutrientLevelOfCell(0), 0, 0);
	    assertEquals(rules.getNutrientLevelOfCell(1), 10.5, 0);
	    assertEquals(rules.getNutrientLevelOfCell(2), 45.8, 0);
	    assertEquals(rules.getNutrientLevelOfCell(3), 100, 0);
	    assertEquals(rules.getNutrientLevelOfCell(4), 0.3, 0);
	    assertEquals(rules.getNutrientLevelOfCell(5), 98, 0);
	    assertEquals(rules.getNutrientLevelOfCell(6), 100, 0);
	    assertEquals(rules.getNutrientLevelOfCell(7), 64.2, 0);
	    assertEquals(rules.getNutrientLevelOfCell(8), 25, 0);
	}
	
	// Test diffusion after one time step.
	@Test
	public void testNutrientDiffusesCorrectlyForPeriodicBoundary() {
		int width = 3;
		int height = 3;
		double delta = 0.5;
		double[] initialNutrientLevels = new double[] {0,0,0,0,90};
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules(width, height, initialNutrientLevels, delta);
		
		// After one time step
		rules.updateNutrientLevelsAfterDiffusion();
		assertEquals(rules.getNutrientLevelOfCell(0), 0, 0);
		assertEquals(rules.getNutrientLevelOfCell(1), 11.25, 0);
		assertEquals(rules.getNutrientLevelOfCell(2), 0, 0);
		assertEquals(rules.getNutrientLevelOfCell(3), 11.25, 0);
		assertEquals(rules.getNutrientLevelOfCell(4), 45, 0);
		assertEquals(rules.getNutrientLevelOfCell(5), 11.25, 0);
		assertEquals(rules.getNutrientLevelOfCell(6), 0, 0);
		assertEquals(rules.getNutrientLevelOfCell(7), 11.25, 0);
		assertEquals(rules.getNutrientLevelOfCell(8), 0, 0);
		
		// After two time steps
		rules.updateNutrientLevelsAfterDiffusion();
		assertEquals(rules.getNutrientLevelOfCell(0), 2.8125, 0);
		assertEquals(rules.getNutrientLevelOfCell(1), 12.65625, 0);
		assertEquals(rules.getNutrientLevelOfCell(2), 2.8125, 0);
		assertEquals(rules.getNutrientLevelOfCell(3), 12.65625, 0);
		assertEquals(rules.getNutrientLevelOfCell(4), 28.125, 0);
		assertEquals(rules.getNutrientLevelOfCell(5), 12.65625, 0);
		assertEquals(rules.getNutrientLevelOfCell(6), 2.8125, 0);
		assertEquals(rules.getNutrientLevelOfCell(7), 12.65625, 0);
		assertEquals(rules.getNutrientLevelOfCell(8), 2.8125, 0);
		
		// After 100 time steps
		for (int i=0; i<100; i++) {
			rules.updateNutrientLevelsAfterDiffusion();
		}
		
		assertEquals(rules.getNutrientLevelOfCell(0), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(1), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(2), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(3), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(4), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(5), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(6), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(7), 10, 0);
		assertEquals(rules.getNutrientLevelOfCell(8), 10, 0);
		
	}
	
	// Tests the number of alive neighbours function works correctly.
	@Test
	public void testNumberOfAliveNeighboursFunctionForPeriodicBoundary() {
		int width = 3;
		int height = 3;
		double delta = 0.5;
		double[] initialNutrientLevels = new double[] {0,0,0,0,90};
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules(width, height, initialNutrientLevels, delta);
		
		Grid grid = new Grid(width, height, 7, 7);
	
		// No bacteria should initially be present, so check each cell has 0 live neighbours.
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 0), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 1), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 2), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 0), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 1), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 2), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 0), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 1), 0, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 2), 0, 0);
		
		// Add the cells to the grid.
		for (int x=0; x<width; x++) {
    			for (int y=0; y<height; y++) {
		        grid.setBacteriumAlive(x, y);
    			}
		}
		
		// Check each cell now has 8 live neighbours.
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 0), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 1), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 0, 2), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 0), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 1), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 1, 2), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 0), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 1), 8, 0);
		assertEquals(rules.returnNumberOfAliveNeighboursForPeriodicGrid(grid, 2, 2), 8, 0);
	}
	
	// Test nutrient levels correctly update for bacteria consumption of nutrient.
	@Test
	public void testNutrientLevelsUpdateCorrectlyForBacteriaConsumption() {
		int width = 3;
		int height = 3;
		double delta = 0.5;
		double[] initialNutrientLevels = new double[] {50,50,50,50,50,50,50,50,50};
		CellularAutomataBacteriaRules rules = new CellularAutomataBacteriaRules(width, height, initialNutrientLevels, delta);
		
		Grid grid = new Grid(width, height, 7, 7);
		
		// Add the cells to the grid.
		for (int x=0; x<width; x++) {
    			for (int y=0; y<height; y++) {
		        grid.setBacteriumAlive(x,y);
    			}
		}
		
		grid.setBacteriumDead(0, 1);
		grid.setBacteriumDead(1, 2);
		grid.setBacteriumDead(2, 2);
		
		Grid copyOfGrid = new Grid(grid);
		
		rules.updateBacteriaAndNutrientAfterConsumption(grid, copyOfGrid);
		
		assertEquals(rules.getNutrientLevelOfCell(0), 40, 0);
		assertEquals(rules.getNutrientLevelOfCell(1), 40, 0);
		assertEquals(rules.getNutrientLevelOfCell(2), 40, 0);
		assertEquals(rules.getNutrientLevelOfCell(3), 50, 0);
		assertEquals(rules.getNutrientLevelOfCell(4), 40, 0);
		assertEquals(rules.getNutrientLevelOfCell(5), 40, 0);
		assertEquals(rules.getNutrientLevelOfCell(6), 40, 0);
		assertEquals(rules.getNutrientLevelOfCell(7), 50, 0);
		assertEquals(rules.getNutrientLevelOfCell(8), 50, 0);
	}
}
