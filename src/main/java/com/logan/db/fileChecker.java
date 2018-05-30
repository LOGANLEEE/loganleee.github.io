package com.logan.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.io.SetFile.Reader;
import org.apache.hadoop.io.Text;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class fileChecker {

	Configuration conf = new Configuration();

	public void FileChecker(Path output, String file_name) throws IOException {

		FileSystem local = FileSystem.get(conf);
		Path mysql_export = new Path("file://" + output + "/" + file_name);
		System.out.println(mysql_export);

		if (local.exists(mysql_export)) {
			local.delete(mysql_export, true);
			System.out.println(mysql_export + " already exist.");
			System.out.println(mysql_export + " has deleted");
		} else {
			System.out.println("Not exist check the location of file.");
		}

	}

	public void TransferLocaltoHdfs() throws IOException {
		conf.addResource(new Path("/Users/Logan/rhadoop_repo/hadoop/etc/hadoop/core-site.xml"));
		FileSystem local = FileSystem.getLocal(conf);
		FileSystem fs = FileSystem.get(conf);

		String filename = "/dc_base.csv";
		Path from = new Path("file:///Users/Logan/dataexpo/mysql_export/" + filename);
		Path to = new Path("hdfs://localhost:9000/user/Logan/dc_in/in/");
		Path des = new Path(to + filename);

		try {
			if (local.exists(from)) {
				fs.copyFromLocalFile(false, true, from, to);
				if (fs.exists(des)) {
					System.out.println(filename + " uploaded well.");
				} else {
					System.out.println(des);
					System.out.println("Error : Wasn't properly uploaded.");
				}
			} else {
				System.out.println("not exist");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void hdfsToLocal() throws IOException {
		FileSystem fs = FileSystem.get(conf);
		conf.addResource(new Path("/Users/Logan/rhadoop_repo/hadoop/etc/hadoop/core-site.xml"));
		FileSystem local = FileSystem.get(conf);
		Path target = new Path("hdfs://localhost:9000/user/Logan/dc_in/out/part-r-00000");
		Path des = new Path("/Users/Logan/dataexpo/mysql_export/result.txt");
		local.copyToLocalFile(false, target, des, true);

	}



}
