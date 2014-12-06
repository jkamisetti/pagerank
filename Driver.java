/*
 * Author @Jagadish Kamisetti
 * Driver.java
 */

/*
 * Main function in this class. Flow starts here
 */
public class Driver {
	public static void main(String[] args) throws Exception {
		   
		String input = args[0];
		String output = args[1];
		String dampFactor = args[2];

	    /*
	     * Graph Properties job
	    */
		Graph g = new Graph();
	    g.GraphPropertiesJob(input,output+"Graph");
	    
	    /*
		 * Call InitRank job
		 */
		InitRanks ir = new InitRanks();
		ir.InitRankJob(input,output+"1");
		
		/*
		  * Call PageRank Job
		  */
		 PageRank pr = new PageRank();
		 int i;
		 for( i=2;i<=60;i++){
			 int counter = pr.PageRankJob(output+(i-1)+"/",output+(i)+"/",dampFactor);
			 if(counter==0){
				 break;
			 }
		 }
		 
		 /*
		  * List the Top 10 nodes
		  */
		 Top10 t10 = new Top10();
		 t10.getTop10Nodes(output+i+"/", output+"top10");
		 
		 /*
		  * print the times 
		  */
	 }
}
