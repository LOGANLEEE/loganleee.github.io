package com.logan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

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
public class Baseball {
	@Inject
	DcbaseRepository repo;

	@Test
	public void test2() {
		Optional<Dc_base> re = repo.findById(1L);
		System.out.println(re);
	}

	@Test
	public void test1() throws IOException {
		int page = DcGetPid();
		int limit = 20000;
		Dc_content(page, limit);
	}

	public void Dc_content(int title_no, int count) throws IOException {
		int checker = title_no - count;
		while (title_no > checker) {
			try {
				Dc_base dc = new Dc_base();
				String url = "http://gall.dcinside.com/board/view/?id=baseball_new6&no=" + title_no;
				Document doc_title = Jsoup.connect(url).get();
				dc.setTno((long) title_no);
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
