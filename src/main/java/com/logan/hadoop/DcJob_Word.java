package com.logan.hadoop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Progressable;

public class DcJob_Word {
  public static void op() throws Exception {
    Configuration conf = new Configuration();
    conf.addResource(new Path("/Users/Logan/rhadoop_repo/hadoop/etc/hadoop/core-site.xml"));
    conf.addResource(new Path("/Users/Logan/rhadoop_repo/hadoop/etc/hadoop/hdfs-site.xml"));
    Job job = Job.getInstance(conf, "word count");
    FileSystem hdfs = FileSystem.get(conf);
	Path in = new Path("hdfs://localhost:9000/user/Logan/dc_in/in/dc_base.csv");
	Path out = new Path("hdfs://localhost:9000/user/Logan/dc_in/out_word/");
	if(hdfs.exists(out)){
		hdfs.delete(out, true);
	}
    job.setJarByClass(DcJob_Word.class);
    job.setMapperClass(DcMapper_Word.class);
    job.setCombinerClass(DcReducer.class);
    job.setReducerClass(DcReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(LongWritable.class);
    FileInputFormat.addInputPath(job, in);
    FileOutputFormat.setOutputPath(job, out);
//    System.exit(job.waitForCompletion(true) ? 0 : 1);
    job.waitForCompletion(true);
  }
}