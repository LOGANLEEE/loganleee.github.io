package com.logan.hadoop;

import org.apache.hadoop.io.Text;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DcParser_Word {

	private String content; // String, [1]

	private boolean isContentExist;
	// 92302,\N, \N,2018-05-09 16:49:00,ㄱㄱㄱ,8618475,나연다현이
	// dbno*,con,ip,regdate****time****,title,***tno,user

	public DcParser_Word(Text text) {
		String[] colums = text.toString().split("££");
		content = colums[1];
		
		
		
		if(!content.equals("NULL")||!content.equals("\\N")){
			isContentExist = true;
		}
	}

}
