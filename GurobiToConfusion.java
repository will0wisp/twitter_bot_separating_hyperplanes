//This file builds a confusion matrix from the output of Gurobi
//Usage: java CSVToGurobi input_file output_file
import java.io.*;
import java.io.IOException;
import java.util.*;


public class GurobiToConfusion {
	private static final String USAGE = "Usage: java GurobiToConfusion soln_trimmed test_set \n";


	public static void main(String[] args) {
		if(args.length != 2){
			System.out.println(USAGE);
			System.exit(1);
		}

		BufferedReader br = null;
		try{
			 br = new BufferedReader(new FileReader(args[0]));

		} catch (Exception e){
			 System.out.println("Exception in GurobiToConfusion.java");
			 e.printStackTrace(); 
			 System.exit(1);
		} finally {
			try{
				br.close();
			} catch(Exception e){

			}
		}
		// reads solution from file
		ArrayList<Double> alpha = new ArrayList<Double>();
		String line;
		Double beta = 0.0;
		String objValue;
		try{
			br = new BufferedReader(new FileReader(args[1]));
			while ((line = br.readLine()) != null) {
			        String[] words = line.split(" ");
			        if(words[0].startsWith("ALPHA")){
			        	alpha.add(Double.parseDouble(words[1]));
			        }
			        else if(words[0].equals("BETA")){
			        	beta = Double.parseDouble(words[1]);
			        }
			        else if (words[0].equals(""));
			        else if (words[0].contentEquals("#")){
			        	objValue = words[4];
			        }
			}
		} catch (Exception e){
			e.printStackTrace(); 
			System.exit(1);
		}

 	//reads test set from file
		LinkedList<Instance> testSet = null;
		try{
			 br = new BufferedReader(new FileReader(args[0]));

			testSet = new LinkedList<Instance>();
			while ((line = br.readLine()) != null) {
			        // use comma as separator
			        String[] words = line.split(" ");
			        if(!words[0].equals("")){
			        	testSet.add(new Instance(words));
			        }
			}
		} catch (Exception e){
			 System.out.println("Exception in GurobiToConfusion.java\n");
			 e.printStackTrace();
			 System.exit(1);
		} finally {
			try{
				br.close();
			} catch(Exception e){

			}
		}

		//form: 
		//bots classified right, bots classified as humans;
		//humans classified as bots, humans classified as bots;
		int[][] confusionMatrix = {{0,0},{0,0}};
		for(Instance i : testSet){
			//System.out.println(i.isClassifiedCorrectly(alpha, beta) + " " + i.getLabel());
			if(i.isClassifiedCorrectly(alpha, beta)){
				if(i.getLabel() == 1){
					confusionMatrix[0][0]++;
				}
				else{
					confusionMatrix[1][1]++;
				}
			}else{
				if(i.getLabel() == 1){
					confusionMatrix[1][0]++;
				}
				else{
					confusionMatrix[0][1]++;
				}
			}
		}
		System.out.println("bot human");
		System.out.println(confusionMatrix[0][0] + " " + confusionMatrix[0][1] + " bot");
		System.out.println(confusionMatrix[1][0] + " " + confusionMatrix[1][1] + " human");
	}
}
