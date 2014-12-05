Implementation of PageRank algorithm

pagerank
========

This is implementation of PageRank algorithm using Java 

Driver.java
  
  The Main() function of the program is located in this file. The execution starts from here. It collects the command line arguments and calls the subsequent jobs from the main function. Initially Graph properties job is called first.

Graph.java
  
  This class creates a job, Mapper & Reducer for calculating graph properties like Minimum out-degree, Maximum out- degree, Average out-degree, Number of nodes and Number of edges. It creates the job in GraphPropertiesJob() function.

 

steps to run
============

The pagerank.jar file takes 3 command line arguments They are:

1. input directory name

2. output directory starting name (example: if you give output directory as "/output", then you will see the output in "/outputGraph" directory).

3. Damping Factor (0.15)

Example:
===========

hdfs dfs -put sample-small.txt /input

hadoop jar pagerank.jar Driver /input /output 0.15

hdfs dfs -cat /outputGraph/part-r-00000

14/12/04 21:48:35 WARN util.NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable

number of edges 	195

number of nodes 	93

Minimum out-degree  0

Maximum out-degree  5

Average out-degree  2.0

