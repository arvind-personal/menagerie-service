package com.menagerie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class MenagerieServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MenagerieServiceApplication.class, args);
    }

}
