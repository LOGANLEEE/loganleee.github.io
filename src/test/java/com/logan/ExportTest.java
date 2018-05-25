package com.logan;

import java.io.IOException;
import java.net.URL;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.google.inject.Inject;
import com.logan.persistence.DcbaseRepository;

import lombok.extern.java.Log;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log
@Commit
@Transactional
public class ExportTest {
	@Inject
	DcbaseRepository repo;

	@Test
	public void test1(){
		repo.ExportDataToCsv();
		System.out.println("exported");
	}
	FileSystem local;
	
	@Test
	public void accessTest1(){
		System.out.println("accessTest1 implemented...");
		Configuration conf = new Configuration();
			System.out.println("try begin");
			try {
//				local = FileSystem.getLocal(conf);
				local = FileSystem.get(conf);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("1");
			Path target = new Path("file:///Users/Logan/dataexpo/mysql_export/dc_base.csv");
			System.out.println("2");
			
			try {
				if(local.exists(target)){
					System.out.println("exist");
					local.delete(target,true);
				} else{
					System.out.println("not exist");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("4");
			
		
		System.out.println("accessTest1 ended..");
	}
	

}
