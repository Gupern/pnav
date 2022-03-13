package com.gupern.pnav;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.gupern.pnav.**.mapper")
@ServletComponentScan
@EnableScheduling // 开启定时任务
@EnableCaching  //开启缓存
@SpringBootApplication
public class PnavApplication {
	public static void main(String[] args) {
		SpringApplication.run(PnavApplication.class, args);
	}

}
