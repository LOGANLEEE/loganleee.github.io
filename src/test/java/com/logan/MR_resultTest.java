package com.logan;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.inject.Inject;
import com.logan.domain.Dc_base;
import com.logan.domain.MR_result;
import com.logan.persistence.DcbaseRepository;
import com.logan.persistence.MR_resultRepository;

import groovy.util.logging.Log;

@SpringBootTest
@RunWith(SpringRunner.class)
@Log
@Commit
public class MR_resultTest {

	@Inject
	MR_resultRepository mrepo;
	@Inject
	DcbaseRepository repo;

	@Test
	public void list() {
		try {

			List<MR_result> result = mrepo.findAll();
			for (MR_result re : result) {
				System.out.println(re);
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			// TODO: handle exception
		}

	}

	@Test
	public void nothing() {
	}

}
