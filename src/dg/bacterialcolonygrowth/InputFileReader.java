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
	
	// Sets the parameters of the CullularAutomataBacteriaRules object that is passed to this method
	// based on the values specified in the input file passed to this method.
	public void setParametersFromInputFile(CellularAutomataBacteriaRules rules, File inputFile) {
		// Used to store the contents of an individual line.
        String line = null;

        // Opens the file and reads each line.
        try {   	
        		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            
           rules.setGridHeight(10);
           rules.setGridWidth(10);

           reader.close();         
        }
        catch(IOException e) {
            System.out.println("Error: Input file " + inputFile.getName() + " cannot be read.");                  
        }
    }
}
