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
	public void setParametersFromInputFile() throws IOException, Exception {
		// Used to store the contents of an individual line.
        String line = null;

        
    		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
    		
    		// Reads and interprets each line of the file. Calls the appropriate method to then set the value
    		// of the parameter.
        while((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            
            if (parts.length == 2) {
            		parts[1] = parts[1].replaceAll("\\s","");
            		
                if (parts[0].equals("grid height"))
                		rules.setGridHeight(Integer.parseInt(parts[1]));
                else if (parts[0].equals("grid width"))
                		rules.setGridWidth(Integer.parseInt(parts[1]));
                else if (parts[0].equals("cell height"))
            			rules.setCellHeight(Integer.parseInt(parts[1]));
                else if (parts[0].equals("cell width"))
            			rules.setCellWidth(Integer.parseInt(parts[1]));
                else if (parts[0].equals("rate of diffusion"))
            			rules.setDiffusionRate(Double.parseDouble((parts[1])));
                else if (parts[0].equals("nutrient for sustenance"))
        				rules.setNutrientForSustenance(Integer.parseInt(parts[1]));
                else if (parts[0].equals("nutrient for growth"))
    					rules.setNutrientForGrowth(Integer.parseInt(parts[1]));
                else if (parts[0].equals("threshold for cell division"))
    					rules.setThresholdForCellDivision(Integer.parseInt(parts[1]));
                else if (parts[0].equals("number of timesteps for cell division"))
						rules.setNumberOfTimestepsForCellDivision(Integer.parseInt(parts[1]));
                else if (parts[0].equals("crowding function"))
						this.checkCrowdingFunctionIsCorrectFormat(parts[1]);
            }
        }

       reader.close();         
    }
	
	// Checks the crowding function specified in the input file is the correct format.
	private void checkCrowdingFunctionIsCorrectFormat(String crowdingFunctionInput) {
		String values [] = crowdingFunctionInput.split(",");
		
		// Check input is correct length i.e. 8, if it is create array of integers of the values and
		// then call function to set those values.
		if (values.length != 9) {
			System.out.println("Error");
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
