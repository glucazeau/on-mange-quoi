package com.sasagui.onmangequoi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OnMangeQuoiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnMangeQuoiApplication.class, args);
    }
}
