package com.logan.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

public class DcReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
	private LongWritable result = new LongWritable();

	public void reduce(Text key, Iterable<LongWritable> values, Context context)
			throws IOException, InterruptedException {
		long sum = 0;
		for (LongWritable val : values) {
			sum += val.get();
		}
		result.set(sum);
		context.write(key, new LongWritable(sum));
	}
}