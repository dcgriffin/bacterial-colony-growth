/* *****************************************************************************
* Description: A class used to read the user's input file.
*
* Author: Daniel Griffin
******************************************************************************/

package dg.bacterialcolonygrowth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class InputFileReader {
	
	private File inputFile;
	private CellularAutomataBacteriaRules rules;
	
	// Constructor.
	public InputFileReader(File input, CellularAutomataBacteriaRules rulesObject) {
		inputFile = input;
		rules = rulesObject;
	}
	
	// Sets the parameters of the CullularAutomataBacteriaRules object that is passed to this method
	// based on the values specified in the input file passed to this method.
	public void setParametersFromInputFile() throws IOException, IllegalArgumentException {
		// Used to store the contents of an individual line.
        String line = null;
        
        BufferedReader reader = null;

        // Uses try catch to ensure reader is closed even if exception is thrown.
        try {
    			reader = new BufferedReader(new FileReader(inputFile));
    		
	    		// Reads and interprets each line of the file. Calls the appropriate method to then set the value
	    		// of the parameter.
	        while((line = reader.readLine()) != null) {
	            String[] parts = line.split(":");
	            
	            if (parts.length == 2) {
	            		parts[1] = parts[1].replaceAll("\\s","");
	            		
	            		// Check input tag to determine what variable to change. Then check input is valid, before
	            		// calling a function to set that variable in the simulation. If input is not valid throw
	            		// illegal argument exception with a reason.
	                if (parts[0].equals("grid height")) {
	                		int gridHeight = Integer.parseInt(parts[1]);
	                		
	                		if (gridHeight >= 1) {
	                			rules.setGridHeight(gridHeight);
	                		}
	                		else throw new IllegalArgumentException("Grid height must be an integer greater than 0.");
	                }
	                else if (parts[0].equals("grid width")) {
		            		int gridWidth = Integer.parseInt(parts[1]);
		            		
		            		if (gridWidth >= 1) {
		            			rules.setGridWidth(gridWidth);
		            		}
		            		else throw new IllegalArgumentException("Grid width must be an integer greater than 0.");
	                }
	                else if (parts[0].equals("cell height")) {
	            			int cellHeight = Integer.parseInt(parts[1]);
	            			
	            			if (cellHeight >= 1) {
	            				rules.setCellHeight(cellHeight);
	            			}
	            			else throw new IllegalArgumentException("Cell height must be an integer greater than 0.");
	                }               
	                else if (parts[0].equals("cell width")) {
	            			int cellWidth = Integer.parseInt(parts[1]);
	            			
	            			if (cellWidth >= 1) {
	            				rules.setCellWidth(cellWidth);
	            			}
	            			else throw new IllegalArgumentException("Cell width must be an integer greater than 0.");
	                }                              
	                else if (parts[0].equals("rate of diffusion")) {
	            			double diffusionRate = Double.parseDouble(parts[1]);
	            			
	            			if (diffusionRate >= 0 && diffusionRate <= 1) {
	            				rules.setDiffusionRate(diffusionRate);
	            			}
	            			else throw new IllegalArgumentException("Rate of diffusion must be between 0 and 1.");
	                }
	                else if (parts[0].equals("nutrient for sustenance")) {
	            			int nutrientForSustenance = Integer.parseInt(parts[1]);
	            			
	            			if (nutrientForSustenance >= 0 && nutrientForSustenance <=100) {
	            				rules.setNutrientForSustenance(nutrientForSustenance);
	            			}
	            			else throw new IllegalArgumentException("Nutrient for sustenance must be an integer between 0 and 100.");
	                }                
	                else if (parts[0].equals("nutrient for growth")) {
	            			int nutrientForGrowth = Integer.parseInt(parts[1]);
	            			
	            			if (nutrientForGrowth >= 0 && nutrientForGrowth <= 100) {
	            				rules.setNutrientForGrowth(nutrientForGrowth);
	            			}
	            			else throw new IllegalArgumentException("Nutrient for growth must be an integer between 0 and 100.");
	                }
	                else if (parts[0].equals("threshold for cell division")) {
	            			int thresholdForCellDivision = Integer.parseInt(parts[1]);
	            			
	            			if (thresholdForCellDivision >= 0) {
	            				rules.setThresholdForCellDivision(thresholdForCellDivision);
	            			}
	            			else throw new IllegalArgumentException("Threshold for cell division must be a non negative integer.");
	                }
	                else if (parts[0].equals("number of timesteps for cell division")) {
	            			int timestepForCellDivision = Integer.parseInt(parts[1]);
	            			
	            			if (timestepForCellDivision >= 1) {
	            				rules.setNumberOfTimestepsForCellDivision(timestepForCellDivision);
	            			}
	            			else throw new IllegalArgumentException("Timestep for cell division must be an integer greater than 0.");
	                }
	                else if (parts[0].equals("boundary condition")) {
		        			String boundaryCondition = parts[1];
		        			
		        			if (boundaryCondition.equals("reflecting") || boundaryCondition.equals("absorbant") || boundaryCondition.equals("periodic")) {
		        				rules.setBoundaryCondition(boundaryCondition);
		        			}
		        			else throw new IllegalArgumentException("Timestep for cell division must be an integer greater than 0.");
	                }
	                else if (parts[0].equals("initial nutrient pattern")) {
		        			String pattern = parts[1];
		        			
		        			if (pattern.equals("missingmiddle") || pattern.equals("random")) {
		        				rules.setNutrientLevelPatternChoice(pattern);
		        			}
		        			else throw new IllegalArgumentException("Initial nutrient pattern can be either 'random' or 'default'.");
	                }
	                else if (parts[0].equals("probability of cell division")) {
	            			double probability = Double.parseDouble(parts[1]);
	            			
	            			if (probability >= 0 && probability <= 1) {
	            				rules.setProbabilityOfCellDivision(probability);
	            			}
	            			else throw new IllegalArgumentException("probability of cell division must be between 0 and 1.");
	                }
	                else if (parts[0].equals("crowding function"))
						this.checkCrowdingFunctionIsCorrectFormat(parts[1]);
	            }
	        }
        }
        finally {
        		if (reader != null) reader.close(); 
		}        
    }
	
	// Checks the crowding function specified in the input file is the correct format.
	private void checkCrowdingFunctionIsCorrectFormat(String crowdingFunctionInput) throws IllegalArgumentException {
		String values [] = crowdingFunctionInput.split(",");
		
		// Check input is correct length i.e. 8, if it is create array of integers of the values and
		// then call function to set those values.
		if (values.length != 9) {
			throw new IllegalArgumentException("There should be 9 values for the crowding function.");
		}
		else {
			int crowdingFunctionValues [] = new int[8];
			
			for (int i = 0; i < 8; i++) {
				crowdingFunctionValues[i] = Integer.parseInt(values[i]);
			}
			
			rules.setCrowdingFunctionValues(crowdingFunctionValues);
		}	
	}
}
