package org.example.kiosk_manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KioskManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(KioskManageApplication.class, args);
    }

}
