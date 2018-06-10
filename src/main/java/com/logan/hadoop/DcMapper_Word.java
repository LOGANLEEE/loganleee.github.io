package com.logan.hadoop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.yarn.logaggregation.AggregatedLogFormat.LogWriter;

public class DcMapper_Word extends Mapper<LongWritable, Text, Text, LongWritable> {

	private final static LongWritable one = new LongWritable(1);
	private Text word = new Text();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		DcParser_Word parser = new DcParser_Word(value);
		String deli = " ";

//		 Getting Rate of Content Existed Thread.
//		 복수 문자를 넣을수있을지? 아니면 일이커짐.
		String[] cont = parser.getContent().split(" |,|;|\\.|\\?|!|-|:|@|\\[|\\]|\\(|\\)|\\{|\\}|_|\\*|/|~|\"|\'");
		
		for(String c : cont){
		word.set(c+"££");
		context.write(word, one);
		}

	}
}