package com.rviewer.skeletons;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
public class RviewerSkeletonApplication {

	public static void main(String[] args) {
		SpringApplication.run(RviewerSkeletonApplication.class, args);
	}

}
