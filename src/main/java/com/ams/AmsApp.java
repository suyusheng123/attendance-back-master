package com.ams;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan("com.ams.dao")

public class AmsApp {
	public static void main(String[] args) {
		SpringApplication.run(AmsApp.class, args);
	}
}
