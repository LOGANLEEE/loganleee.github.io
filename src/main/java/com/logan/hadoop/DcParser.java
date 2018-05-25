package com.logan.hadoop;

import org.apache.hadoop.io.Text;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcParser {

	private Long dbno;// 92302 , [0]
	private String content; // String, [1]
	private String ipAddress; // ?.?.?.? ,[2]
	private String[] regDate;// 2018-05-09 16:49:00,[3]
	private String[] Time;
	private String[] Date;
	// split regdate.
	private String year;
	private String month;
	private String day;
	private String hour;
	private String minute;
	private String title; // String
	private Long tno;// 8618475
	private String user; // String

	private boolean isContentExist;
	private boolean isIpAddressExist;
	// 92302,\N, \N,2018-05-09 16:49:00,ㄱㄱㄱ,8618475,나연다현이
	// dbno*,con,ip,regdate****time****,title,***tno,user

	public DcParser(Text text) {
		String[] colums = text.toString().split("££");
		dbno = Long.parseLong(colums[0]);
		content = colums[1];
		ipAddress = colums[2];
		regDate = colums[3].split(" ");
		Date = regDate[0].split("-");
		year = Date[0];
//Caused by: java.lang.ArrayIndexOutOfBoundsException: 1
		month = Date[1];
		day = Date[2];
		Time = regDate[1].split(":");
		hour = Time[0];
		minute = Time[1];
		title = colums[4];
		tno = Long.parseLong(colums[5]);
		user = colums[6];
		
		if(!content.equals("NULL")||!content.equals("\\N")){
			isContentExist = true;
		}
		if(!ipAddress.equals("NULL")||!ipAddress.equals("\\N")){
			isIpAddressExist = true;
		}
	}

}
