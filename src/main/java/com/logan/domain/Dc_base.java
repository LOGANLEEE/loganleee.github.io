package com.logan.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "dc_base")
@ToString
@Setter
@Getter
@EqualsAndHashCode(of="dbno")
public class Dc_base {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long dbno;
	private Long tno;
	private String title;
	@Column(length=9999999)
	private String content;
	private String user;
	private String regDate;
	private String ipAddress;

	@CreationTimestamp
	private Timestamp creationDate;
}
