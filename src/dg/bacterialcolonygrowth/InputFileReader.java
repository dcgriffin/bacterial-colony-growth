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
	public void setParametersFromInputFile() {
		// Used to store the contents of an individual line.
        String line = null;

        // Opens the file and reads each line.
        try {   	
        		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            while((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                
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
            }

           reader.close();         
        }
        catch(IOException e) {
            System.out.println("Error: Input file " + inputFile.getName() + " cannot be read.");                  
        }
    }
}
