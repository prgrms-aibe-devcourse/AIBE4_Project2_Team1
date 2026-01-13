package kr.java.pr1mary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Pr1maryApplication {

    public static void main(String[] args) {
        SpringApplication.run(Pr1maryApplication.class, args);
    }

}
