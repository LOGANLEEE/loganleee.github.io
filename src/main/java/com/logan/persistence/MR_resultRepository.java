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
	
	@Query(value="select hour,sum(result) from result group by hour;",nativeQuery=true)
	public List<Object[]> SumResultPerHours();
	
	@Query(value="SELECT day,sum(result) FROM result GROUP BY day",nativeQuery=true)
	public List<Object[]> SumResultPerDay();
	
	@Query(value="SELECT minute,sum(result) FROM result GROUP by minute",nativeQuery=true)
	public List<Object[]> SumResultPerMinute();
}
