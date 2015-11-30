package com.facebook.likes;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FacebookReducer extends Reducer<LongWritable,Text , Text, LongWritable> {

	@Override
	public void reduce(LongWritable key, Iterable<Text> users, Context context)
			throws IOException, InterruptedException {
		//long likesCount = 0;
		// LongWritable likes = new LongWritable();
		for (Text user : users) {	
			context.write(user, key);
		}

		
	}
}
