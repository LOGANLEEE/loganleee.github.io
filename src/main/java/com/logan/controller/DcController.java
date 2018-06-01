package com.logan.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.logan.db.fileChecker;
import com.logan.domain.Dc_base;
import com.logan.domain.MR_result;
import com.logan.hadoop.DcJob;
import com.logan.persistence.DcbaseRepository;
import com.logan.persistence.MR_resultRepository;

import lombok.extern.java.Log;

@Controller
@RequestMapping("/dc")
@Log
public class DcController {
	@Inject
	DcbaseRepository repo;
	@Inject
	MR_resultRepository mrepo;

	@GetMapping("/hour_result_chart")
	public void hour_result_chart(Model model) {
		List<Object[]> datas = mrepo.AnalByHours();
		List<Object> receiver_hour = new ArrayList<>();
		List<Object> receiver_result = new ArrayList<>();
		List<Object> temp = new ArrayList<>();
		for (Object[] d : datas) {
			temp = Arrays.asList(d);
			receiver_hour.add(temp.get(0));
			receiver_result.add(temp.get(1));
		}
		model.addAttribute("data_hour",receiver_hour);
		model.addAttribute("data_result",receiver_result);

	}

	@GetMapping("/min_result_chart")
	public void min_result_chart( Model model) {
		List<MR_result> list_result = mrepo.findAll();
		List<Object> minute = new ArrayList<>();
		List<Object> result = new ArrayList<>();
		for(MR_result list : list_result){
			minute.add(list.getEMinute());
			result.add(list.getFResult()	);
		}
		model.addAttribute("minute",minute);
		model.addAttribute("result",result);
	}

	@GetMapping("/day_result_chart")
	public void day_result_chart(Model model) {
		List<Object[]> result_list= mrepo.SumResultPerDay();
		List<Object> day = new ArrayList<>();
		List<Object> result = new ArrayList<>();
		List<Object> temp = new ArrayList<>();
		for (Object[] r : result_list) {
			temp = Arrays.asList(r);
			day.add(temp.get(0));
			result.add(temp.get(1));
		}
		model.addAttribute("day",day);
		model.addAttribute("result",result);

	}

	@GetMapping("/export")
	public String export() throws IOException {
		fileChecker checker = new fileChecker();
		checker.FileChecker(new Path("/Users/Logan/dataexpo/mysql_export/"), "dc_base.csv");
		try {
			repo.ExportDataToCsv();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Please check your method again");
		}
		System.out.println("export has processed.");
		fileChecker loader = new fileChecker();
		loader.TransferLocaltoHdfs();
		System.out.println("Loader Done.");
		return "redirect:/dc/list";
	}

	@GetMapping("/view")
	public void view(Long db_num, Dc_base dc, Model model) {
		Optional<Dc_base> result = repo.findById(db_num);
		model.addAttribute("result", result.get());
		log.info("has called ");
	}

	@GetMapping("/list")
	public void list(Dc_base dc, Model model,
			@PageableDefault(sort = "dbno", page = 0, direction = Direction.DESC, size = 20) Pageable page) {

		Page<Dc_base> result = repo.findAll(page);
		model.addAttribute("result", result);
	}

	@GetMapping("/crawl")
	public String crawl() throws IOException {
		System.out.println("List has called...");
		//////////////
		// DcCrawl dc = new DcCrawl();
		// int page = dc.DcGetPid();
		// int limit = 20;
		// dc.Dc_content(page, limit);
		//////////////
		int page = DcGetPid();
		// limit도 웝상에서 유저가 원하는 만큼 가져올수있게 해주고싶음.
		int limit = 10;
		Dc_content(page, limit);
		/////////////////
		return "redirect:/dc/list";
	}

	@GetMapping("/test")
	public String test() throws IOException {

		return "redirect:/dc/list";
	}

	@GetMapping("/hadoop")
	public String hadoop() throws Exception {
		DcJob job = new DcJob();
		job.op();
		System.out.println("MR Done.");
		fileChecker fc = new fileChecker();
		fc.hdfsToLocal();
		System.out.println("hdfsToLocal Done");
		try {
			mrepo.deleteAll();
			mrepo.ImportLocalFromDB();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ImportLocalFromDB Done");
		return "redirect:/dc/list";
	}

	////////////////

	public void Dc_content(int title_no, int count) throws IOException {
		int checker = title_no - count;
		while (title_no > checker) {
			try {
				Dc_base dc = new Dc_base();
				String url = "http://gall.dcinside.com/board/view/?id=baseball_new7&no=" + title_no;
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
				}
				System.out.println(title_no + " has done");
				repo.save(dc);
				title_no--;
			} catch (Exception e) {
				System.err.println("this board has deleted.");
				title_no--;
			}
		}
		System.out.println("Job DOne!");
	}

	public int DcGetPid() throws IOException {
		// 이전 야갤주소.
		// String main_url =
		// "http://gall.dcinside.com/board/lists/?id=baseball_new6&page=1";
		String main_url = "http://gall.dcinside.com/board/lists/?id=baseball_new7&page=1";
		Document doc_main = Jsoup.connect(main_url).get();
		Elements get_pid = doc_main.select("td.t_notice").eq(4);
		int pid = Integer.parseInt(get_pid.text());
		return pid;
	}
	/////////
}
