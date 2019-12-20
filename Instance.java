import java.util.*;

public class Instance{
	int[] cols;
	public Instance(String[] cols){
		makeCols(cols);
		//code falses as -1, trues as 1, no answer as 0 
	}
	private void makeCols(String[] cols){
		this. cols = new int[cols.length];
		for(int x = 0; x < cols.length; x++){
			if(cols[x].equals("TRUE"))
				this.cols[x] = 1;
			else if (cols[x].equals("FALSE"))
				this.cols[x] = -1;
			else if (cols[x].equals(""))
				this.cols[x] = 0;
			else
				this.cols[x] = Integer.parseInt(cols[x]);
		}

	}
	public int getLabel(){
		return cols[cols.length - 1];
	}

	public String getConstriantDotProduct(char operator){
		String output = " ";
		for(int x = 0; x < cols.length; x++){
			if(operator == '+'){
				output += "- ";
			} else {
				output += "+ ";
			}
			output += (cols[x] + " ALPHA" + x);
			output += " ";
		}
		return output;
	}
	public String toString(){
		String output = "";
		for(int x = 0; x < cols.length; x++){
			output += cols[x] + " ";
		}
		return output;
	}

	public boolean isClassifiedCorrectly(ArrayList<Double> alpha, Double beta){
		double sln = - beta;
		for(int x = 0; x < cols.length - 1; x++){
			sln += alpha.get(x) * (double) cols[x];
		}

		if(getLabel() == 1){
			return sln >= 0;
		} else{
			return sln <= 0;
		}

	}
}