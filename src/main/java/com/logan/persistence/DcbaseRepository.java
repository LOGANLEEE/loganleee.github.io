package com.logan.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.logan.domain.Dc_base;

public interface DcbaseRepository extends JpaRepository<Dc_base, Long>, QuerydslPredicateExecutor<Dc_base> {

	@Query("SELECT d.dbno, d.user, d.title, d.content, d.regDate,d.ipAddress FROM Dc_base d where dbno = ?1 order by db_no asc")
	public List<Dc_base> MoveListToView(Long number);

	// @Query(value ="SELECT * FROM dc_base INTO OUTFILE
	// '/Users/Logan/dataexpo/mysql_export/dc_base.csv' FIELDS TERMINATED BY ','
	// LINES TERMINATED BY '\n'", nativeQuery=true)
	@Query(value ="SELECT d.dbno, d.content,d.ip_address,d.reg_date,d.title,d.tno,d.user FROM	dc_base d INTO OUTFILE'/Users/Logan/dataexpo/mysql_export/dc_base.csv' FIELDS TERMINATED BY'££' LINES TERMINATED BY'\n'",nativeQuery=true)
			public Object ExportDataToCsv();

}
