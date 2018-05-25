package com.logan;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.hadoop.io.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.logan.domain.Dc_base;
import com.logan.persistence.DcbaseRepository;

import lombok.extern.java.Log;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log
@Commit
public class DcTest {
	@Inject
	DcbaseRepository repo;

	@Test
	public void parser() {
		Long dbno;// 92302 , [0]
		String content; // String, [1]
		String ipAddress; // ?.?.?.? ,[2]
		 String[] regDate;// 2018-05-09 16:49:00,[3]
		 String[] Time;
		 String[] Date;
		// split regdate.
		 String year;
		 String month;
		 String day;
		 String hour;
		 String minute;
		 String title; // String
		 Long tno;// 8618475
		 String user; // String
		 Text text = new Text("555,\\N,59.12.*.*,2018-05-09 16:58:56,엄마성\\, 아빠성 둘다 쓰는거 좋다이거야,8618598,ㅇㅇ");
//		 Text text = new Text("559,\\N,\\N,2018-05-09 16:58:20,야념간 지식인 노처녀 팩트폭행범 다른댓글모음ㄷㄷㄷ,8618591,맑은고딕");
		 boolean isContentExist;
		 boolean isIpAddressExist;
		// dbno*,con,ip,regdate****time****,title,***tno,user
		try {
			
		String[] colums = text.toString().split(",");

		dbno = Long.parseLong(colums[0]);
		System.out.println("dbno = "+dbno);
		content = colums[1];
		System.out.println("concent = "+content.toString());
		ipAddress = colums[2];
		System.out.println("ip = "+ipAddress.toString());
		regDate = colums[3].split(" ");
		System.out.println("regdate = "+ regDate.length);
		Date = regDate[0].split("-");
		for(String s : Date){
			System.out.println("Date's indexs = " + s);
		}
		System.out.println("Date = "+ Date.length);
		year = Date[0];
		System.out.println("year = "+ year);
		month = Date[1];
		System.out.println("month = "+ month);
		day = Date[2];
		System.out.println("day = "+day);
		Time = regDate[1].split(":");
		System.out.println("time = "+Time.length);
		hour = Time[0];
		System.out.println("hour = "+hour);
		minute = Time[1];
		System.out.println("minute = "+minute);
		title = colums[4];
		if(title.contains("\\")){
			title.replaceAll("\\", "/");
		}
		System.out.println("title = "+title);
		tno = Long.parseLong(colums[5]);
		System.out.println("tno = "+tno);
		user = colums[6];
		System.out.println("user = " +user);
		
		if(!content.equals("NULL") || (!content.equals("\\N"))){
			isContentExist = true;
			System.out.println("content exist");
			System.out.println(content);
		} else {
			isContentExist = false;
			System.out.println("content doesn't exist");
		}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getStackTrace());
		}
	}

	@Test
	public void test1() throws IOException {
		int page = DcGetPid();
		int limit = 10;
		Dc_content(page, limit);
	}

	public void Dc_content(int title_no, int count) throws IOException {
		int checker = title_no - count;
		while (title_no > checker) {
			try {
				Dc_base dc = new Dc_base();
				String url = "http://gall.dcinside.com/board/view/?id=baseball_new6&no=" + title_no;
				Document doc_title = Jsoup.connect(url).get();
				Element title = doc_title.select("dl.wt_subject dd").first();
				System.out.println("Title = " + title.text());
				dc.setTitle(title.text());
				Element user = doc_title.select("span.user_nick_nm").first();
				System.out.println("User = " + user.text());
				dc.setUser(user.text());
				Element date = doc_title.select("div.w_top_right ul li b").first();
				System.out.println("Date = " + date.text());
				dc.setRegDate(date.text());
				Element ip = doc_title.select("li.li_ip").first();
				if (ip.hasText()) {
					System.out.println("Ip = " + ip.text());
					dc.setIpAddress(ip.text());
				}
				Elements content = doc_title.select("div.s_write p");
				if (content.hasText()) {
					System.out.println("Context = " + content.text());
					dc.setContent(content.text());
				} else {
					System.out.println("No Cotent");
				}
				System.out.println(title_no + " has done");
				repo.save(dc);
				title_no--;
			} catch (Exception e) {
				System.err.println("d has deleted.");
				title_no--;
			}
		}
		System.out.println("Job DOne!");
	}

	public int DcGetPid() throws IOException {
		String main_url = "http://gall.dcinside.com/board/lists/?id=baseball_new6&page=1";
		Document doc_main = Jsoup.connect(main_url).get();
		Elements get_pid = doc_main.select("td.t_notice").eq(4);
		int pid = Integer.parseInt(get_pid.text());
		return pid;
	}

}
