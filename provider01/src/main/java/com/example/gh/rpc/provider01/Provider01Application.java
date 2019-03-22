package com.example.gh.rpc.provider01;

import com.example.gh.rpc.demo.anno.EnableRpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRpc
@SpringBootApplication
public class Provider01Application {

	public static void main(String[] args) {
		SpringApplication.run(Provider01Application.class, args);
	}

}
