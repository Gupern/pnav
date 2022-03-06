package com.gupern.pnav;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@MapperScan("com.gupern.pnav.**.mapper")
@ServletComponentScan
@SpringBootApplication
public class PnavApplication {
	public static void main(String[] args) {
		SpringApplication.run(PnavApplication.class, args);
	}

}
