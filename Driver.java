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
		 
	 }
}
