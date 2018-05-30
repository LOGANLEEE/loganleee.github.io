package com.logan.persistence;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.logan.domain.MR_result;

public interface MR_resultRepository extends JpaRepository<MR_result, Long>, QuerydslPredicateExecutor<MR_result> {

	@Query(value ="LOAD DATA INFILE '/Users/Logan/dataexpo/mysql_export/result.txt' INTO TABLE result FIELDS TERMINATED BY ';;' LINES TERMINATED BY '\n'",nativeQuery=true)
	public Object ImportLocalFromDB();
	
	@Query(value="SELECT * FROM result",nativeQuery=true)
	public List<Object[]> result();
}
