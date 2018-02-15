package dg.bacterialcolonygrowth;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javafx.scene.paint.Color;

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
	    CellularAutomataRules rules = new CellularAutomataRules(width, height, initialNutrientLevels, delta);
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
		CellularAutomataRules rules = new CellularAutomataRules(width, height, initialNutrientLevels, delta);
		
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
}
