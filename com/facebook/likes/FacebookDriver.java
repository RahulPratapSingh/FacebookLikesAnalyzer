package com.facebook.likes;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FacebookDriver extends Configured implements Tool{
	public int run(String[] args) throws Exception{
		if(args.length != 2){
			System.out.println("Usage:CountWordsDriver <input path> <output path>");
			System.exit(-1);
		}
		
		Job job = new Job();
		job.setJarByClass(FacebookDriver.class);
		job.setJobName("Facebook likes analytics");
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(FacebookMapper.class);
		job.setReducerClass(FacebookReducer.class);
		
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);
		job.setSortComparatorClass(LongWritable.DecreasingComparator.class);
		
		System.exit(job.waitForCompletion(true) ? 0:1);
		boolean success = job.waitForCompletion(true);
		
		return success ? 0:1;
		
	}
	
	public static void main(String[] args) throws Exception{
		FacebookDriver driver = new FacebookDriver();
		int exitcode = ToolRunner.run(driver, args);
		System.exit(exitcode);
	}
}
