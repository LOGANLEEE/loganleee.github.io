package com.logan.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.yarn.logaggregation.AggregatedLogFormat.LogWriter;

public class DcMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

	private final static LongWritable one = new LongWritable(1);
	private Text word = new Text();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		DcParser parser = new DcParser(value);
		// StringTokenizer itr = new StringTokenizer(value.toString());
		// while (itr.hasMoreTokens()) {
		// word.set(itr.nextToken());
		// context.write(word, one);
		// }

		// Getting Rate of Content Existed Thread.
		// if(parser.isContentExist()){

		// word.set(parser.getYear() + ",");
		// context.write(word, one);
		// }
		Long day_std = Long.parseLong(parser.getDay());
		Long mon_std = Long.parseLong(parser.getMonth());
		Long min_std = Long.parseLong(parser.getMinute());
			
		if (min_std > 0) {
			
			word.set("0;;"+parser.getYear() +";;"+ parser.getMonth() + ";;"+parser.getDay() + ";;"+parser.getHour() +";;"+ parser.getMinute()+";;");
			context.write(word, one);
		}

	}
}