//This file takes in a CSV with only numerical or true/false answers, and turns them into a .lp file for gurobi.
//Usage: java CSVToGurobi input_file output_file test_set
import java.io.*;
import java.io.IOException;
import java.util.*;


public class CSVToGurobi {
	
	public static void main(String[] args) {
		ArrayList<Instance> instances = new ArrayList<Instance>();
		LinkedList<String> constraints = new LinkedList<String>();
		LinkedList<String> bounds = new LinkedList<String>();
		String objective = "";
		final double TRAINING_PERCENT = .9;
		
		
		final String USAGE = "Usage: java CSVToGurobi input output test_set";

		if(args.length != 3){
			System.out.println(USAGE);
			System.exit(1);
		}
		BufferedReader br = null;
		FileWriter fw = null;
		String line = "";
		String cvsSplitBy = ",";
		String[] colLabels;

		try {

			//this reads the file
		    br = new BufferedReader(new FileReader(args[0]));
		    line = br.readLine();
		    colLabels = line.split(cvsSplitBy);

		    int y = 0;
		    while ((line = br.readLine()) != null) {
		        // use comma as separator
		        String[] cols = line.split(cvsSplitBy);
		        instances.add(new Instance(cols));
		    }
		    instances.trimToSize();

		    Collections.shuffle(instances);

		    int numBots = 0;
		    int numHumans = 0;
		    //this builds the constraints
		    for(int x = 0; x < instances.size() * TRAINING_PERCENT; x++){
		    	Instance i = instances.get(x);

		    	char botOrNot, operator;
		    	int index;
		    	if(i.getLabel() == 1) { //If I is a bot
		    		botOrNot = 'B';
		    		index = numBots++;
		    		operator = '+';
		    	}
		    	else{
		    		botOrNot = 'H';
		    		index = numHumans++;
		    		operator = '-';
		    	}
		    	index++;
		    	constraints.add("" + botOrNot + index +  i.getConstriantDotProduct(operator) + " " + operator + " BETA >= 1\n");
				bounds.add("" + botOrNot + index + " >= 0\n");
		    }

		    //sets the objective string.
		    for(int x = 0; x < numBots; x++){
		    	objective += " + " + (1./numBots) + " B" + x;
		    }
		    for(int x = 0; x < numHumans; x++){
		    	objective += " + " + (1./numHumans) + " H" + x;
		    }

		    fw = new FileWriter(args[1]);
		    fw.write("Minimize\n");
		    fw.write(objective + "\n");
		    fw.write("Subject To\n");

		    for(String s : constraints) {
		    	fw.write(s);
		    }
		    fw.write("Bounds\n");
		    for(String s : bounds){
		    	fw.write(s);
		    }
		    fw.write("End\n");
		    fw.close(); 

		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    if (br != null) {
		        try {
		            br.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}

		try {
			fw = new FileWriter(args[2]);
			for(int x = (int) Math.floor(instances.size() * TRAINING_PERCENT); x < instances.size(); x++){
				 fw.write(instances.get(x).toString() + '\n');
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
