package com.sscs.apitest.pinpad;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(scanBasePackages = {"com.sscs.apitest.pinpad"})
@SpringBootApplication
@ComponentScan(basePackages ={"com.sscs.apitest.pinpad"})
@Slf4j
public class MainApp {

    public static void main(String[] args) {

//		log.info("version - sorted all image file");
//		log.info("version - moved file background");
        log.info("version - poc");
        SpringApplication.run(MainApp.class, args);
    }
}
