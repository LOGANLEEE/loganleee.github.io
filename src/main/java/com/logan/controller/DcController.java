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
import org.hibernate.annotations.Parameter;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.logan.db.fileChecker;
import com.logan.domain.Dc_base;
import com.logan.domain.MR_result;
import com.logan.hadoop.DcJob;
import com.logan.hadoop.DcJob_Word;
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
		List<Object[]> result_list = mrepo.SumResultPerHours();
		List<Object> receiver_hour = new ArrayList<>();
		List<Object> receiver_result = new ArrayList<>();
		List<Object> temp = new ArrayList<>();
		for (Object[] d : result_list) {
			temp = Arrays.asList(d);
			receiver_hour.add(temp.get(0));
			receiver_result.add(temp.get(1));
		}
		model.addAttribute("data_hour",receiver_hour);
		model.addAttribute("data_result",receiver_result);

	}

	@GetMapping("/min_result_chart")
	public void min_result_chart( Model model) {
		List<Object[]> result_list = mrepo.SumResultPerMinute();
		List<Object> receiver_minute = new ArrayList<>();
		List<Object> receiver_result = new ArrayList<>();
		List<Object> temp = new ArrayList<>();
		for(Object[] r : result_list){
			temp = Arrays.asList(r);
			receiver_minute.add(temp.get(0));
			receiver_result.add(temp.get(1));
		}
		model.addAttribute("minute",receiver_minute);
		model.addAttribute("result",receiver_result);
	}

	@GetMapping("/day_result_chart")
	public void day_result_chart(Model model) {
		List<Object[]> result_list= mrepo.SumResultPerDay();
		List<Object> receiver_day = new ArrayList<>();
		List<Object> receiver_result = new ArrayList<>();
		List<Object> temp = new ArrayList<>();
		for (Object[] r : result_list) {
			temp = Arrays.asList(r);
			receiver_day.add(temp.get(0));
			receiver_result.add(temp.get(1));
		}
		model.addAttribute("day",receiver_day);
		model.addAttribute("result",receiver_result);

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
			@PageableDefault(sort = "dbno", page = 0, direction = Direction.DESC, size = 40) Pageable page) {

		Page<Dc_base> result = repo.findAll(page);
		model.addAttribute("result", result);
	}

	@GetMapping("/crawl")
	public String crawl(Long count,Model model) throws IOException {
		System.out.println("List has called...");
		int page = DcGetPid();
		Long limit = count;
		Dc_content(page, limit);
		/////////////////
		return "redirect:/dc/list";
	}

	@GetMapping("/word")
	public String word() throws Exception {
		DcJob_Word word = new DcJob_Word();
		word.op();

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

	public void Dc_content(int title_no, Long limit) throws IOException {
		Long checker = title_no - limit;
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
