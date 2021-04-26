package com.gof.springcloud.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

	private int cid;
	private String cname;
	private String cphone;
	private Double plat;
	private Double plon;

}
