package com.logan.domain;


import java.sql.Timestamp;

import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.core.annotation.Order;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "rno")
@Table(name = "result")
@Entity
public class MR_result {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rno;
	@Column(name="year",nullable=false)
	private int aYear;
	@Column(name="month",nullable=false)
	private int bMonth;
	@Column(name="day",nullable=false)
	private int cDay;
	@Column(name="hour",nullable=false)
	private int dHour;
	@Column(name="minute",nullable=false)
	private int eMinute;
	@Column(name="result",nullable=false)
	private int fResult;

}