package com.facebook.likes;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FacebookMapper extends Mapper<LongWritable,Text,LongWritable,Text>{
	public LongWritable userLikes = new LongWritable();

	public Text userName = new Text();
	
	public void map(LongWritable key, Text value, Context context) throws IOException,InterruptedException{
		String[] split = value.toString().split(",");
		userName.set((split[1]));
		if (split.length > 2){
			try{
				userLikes.set(Long.parseLong((split[2])));
				context.write(userLikes,userName);
			}catch(NumberFormatException e){
				// cannot parse
			}
		}
	}
}
