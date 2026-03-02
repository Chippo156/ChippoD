package org.interview.projectinterview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ProjectInterviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectInterviewApplication.class, args);
    }

}
