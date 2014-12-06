/*
 * Author @Jagadish Kamisetti
 * Top10.java
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class Top10 {

	/*
	 * Mapper class for getting top10 nodes
	 */
	public static class Map extends Mapper<LongWritable, Text, Text, Text> {

		/*
		 * Map function 
		 * 
		 */
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line, "\n");
			//Iterate through tokens
			while (tokenizer.hasMoreTokens()) {
				
				String str = tokenizer.nextToken();
				StringTokenizer tokens = new StringTokenizer(str);		
				String nodeID = tokens.nextToken();
				
				String pageRank = tokens.nextToken();
				
				//Add a '#' after the rank such that to differentiate
				String nodeRank = nodeID + "#" + pageRank;
				Text word = new Text("dummy");
				Text nodeRankFinal = new Text(nodeRank);
				
				//Emit the node
				context.write(word, nodeRankFinal);
			}
		}
	}
	
	/*
	 * Reducer class for getting top 10 nodes
	 */
	public static class Reduce extends Reducer<Text, Text, Text, FloatWritable> {

		private Text nodeID = new Text();
		private FloatWritable rank = new FloatWritable();

		/*
		 * reduce function
		 * 
		 */
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			
			// using arraylists to store top10 page ranks and top10 nodes
			ArrayList<Float> top10PageRanks = new ArrayList<Float>();
			ArrayList<String> result10Nodes = new ArrayList<String>();
			
			float minRank = Float.MAX_VALUE;
			int currMinIndex = 0;
			
			// iterate on all values
			for (Text val : values) {
				String str = val.toString();
				String[] strArray = str.split("#");
				String nodeId = strArray[0];
				float pageRank = Float.parseFloat(strArray[1]);
				// if size exceeds 10 remove min
				if (top10PageRanks.size() >= 10) {

					if (pageRank > minRank) {
						result10Nodes.remove(currMinIndex);
						top10PageRanks.remove(currMinIndex);
						result10Nodes.add(nodeId);
						top10PageRanks.add(pageRank);
						minRank = Float.MAX_VALUE;
						for (int i = 0; i < top10PageRanks.size(); i++) {
							if (top10PageRanks.get(i) < minRank) {
								minRank = top10PageRanks.get(i);
								currMinIndex = i;
							}
						}
					}
				} else {
					result10Nodes.add(nodeId);
					top10PageRanks.add(pageRank);
					// update current min
					if (pageRank < minRank) {
						minRank = pageRank;
						currMinIndex = top10PageRanks.size() - 1;
					}

				}
			}

			for (int i = 0; i < 10; i++) {
				Float max = Float.MIN_VALUE;
				for (int j = 0; j < top10PageRanks.size(); j++) {
					if (top10PageRanks.get(j) > max) {
						max = top10PageRanks.get(j);
					}
				}
				int maxIndex = top10PageRanks.indexOf(max);
				nodeID.set(result10Nodes.remove(maxIndex));
				rank.set(top10PageRanks.remove(maxIndex));
				
				//Write to context
				//context.write(nodeID, rank);
				context.write(nodeID,rank);
			}
		}
	}
	
	  public static class AvgRatingMap extends Mapper<LongWritable, Text, Text, Text> 
		{
			public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
			{
				String some = value.toString();
				StringTokenizer tokenizer = new StringTokenizer(some, "\t");
				String nodeId="",rank="";
				if (tokenizer.hasMoreTokens()) 
					nodeId = tokenizer.nextToken();

				if (tokenizer.hasMoreTokens()) 
					rank = tokenizer.nextToken();
					    
				
				context.write(new Text(nodeId), new Text("LIST1#"+nodeId));
			}
			
		} 
		
		public static class MovieNameMap extends Mapper<LongWritable, Text, Text, Text> 
		{
			public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
			{
				String some = value.toString();
				String[] data=some.split(" ");
				String Data = data[0].trim();
				String nodeId = data[1].trim();
				context.write(new Text(nodeId), new Text("LIST2#"+Data));
			}
			
		} 

	  
	 

	      public static class AvgRatingMovieNameReducer extends Reducer<Text,Text,Text,Text>
	  	{
	  		String nodeId=null;
	  		String Data=null;
	  		public void reduce(Text key, Iterable<Text> values, Context context)
	  		{
	  			for(Text value: values)
	  			{
	  				String[] tokens = value.toString().split("#");
	  				
	  				if(tokens[0].trim().equals("LIST1"))
	  				{
	  					nodeId=tokens[1].trim();
	  				}
	  				else if(tokens[0].trim().equals("LIST2"))
	  				{
	  					Data=tokens[1].trim();
	  				}
	  			}
	  			try{
	  			if(nodeId!=null && Data!=null)
	  			{
	  				context.write(new Text(nodeId), new Text(Data));
	  				nodeId=null;
	  				Data=null;
	  			}
	  			}catch (Exception e) {
                  e.printStackTrace();
              }
	  		
	  		}
	  	}

	
	
public  void getTop10Nodes(String input, String output) throws Exception {
		//Create configuration
		Configuration conf = new Configuration();
		Job job = new Job(conf, "top10Ranks");
		
		//set key ,value classes for output
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//set the class types for mapper and reducer
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		//set main class
		job.setJarByClass(Top10.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		job.waitForCompletion(true);
	    if (job.isSuccessful())
        {
	    	Job job3 = new Job(conf, "toptenratedmovies");
	    	org.apache.hadoop.mapreduce.lib.input.MultipleInputs.addInputPath(job3, new Path("/outputtop10"), TextInputFormat.class,
	    			 AvgRatingMap.class);
	    	 org.apache.hadoop.mapreduce.lib.input.MultipleInputs.addInputPath(job3, new Path("/inputdata/sample-data.txt"), TextInputFormat.class,
	    			 MovieNameMap.class);
	    	 job3.getConfiguration().set("join.type", "inner");
	   
	    	 job3.setReducerClass(AvgRatingMovieNameReducer.class);
	    	 job3.setMapOutputKeyClass(Text.class);
	    	 job3.setMapOutputValueClass(Text.class);
	    	 job3.setOutputKeyClass(Text.class);
	    	 job3.setOutputValueClass(Text.class);
	    	 job3.setJarByClass(Top10.class);
	    	 job3.setOutputFormatClass(TextOutputFormat.class);
	         FileOutputFormat.setOutputPath(job3, new Path("/outputsearchresult"));
					
	         job3.waitForCompletion(true);			
      } 	
	}


}
