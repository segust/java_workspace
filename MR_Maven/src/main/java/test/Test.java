package test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Test {
public static void main(String[] args) throws Exception{
	Configuration conf=new Configuration();
	
	conf.set("fs.defaultFS", "hdfs://172.22.145.1:9000");
	
	conf.set("mapreduce.job.jar", "target/MR_Maven-0.0.1-SNAPSHOT.jar");
	conf.set("mapreduce.framework.name", "yarn");
	conf.set("yarn.resourcemanager.hostname", "master1");
	conf.set("mapreduce.app-submission.cross-platform", "true");
	
	Job job=Job.getInstance(conf);
	
	job.setMapperClass(WordMapper.class);//设定mapper的class
	job.setReducerClass(WordReducer.class);//设定reducer的class
	
	job.setMapOutputKeyClass(Text.class);
	job.setMapOutputValueClass(IntWritable.class);

	job.setOutputKeyClass(Text.class);
	job.setOutputValueClass(LongWritable.class);
	
	FileInputFormat.setInputPaths(job, "/input/");
	FileOutputFormat.setOutputPath(job,new Path("/output1/"));
	
	job.waitForCompletion(true);
}
}
