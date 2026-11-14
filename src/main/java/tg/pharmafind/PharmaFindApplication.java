package tg.pharmafind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PharmaFindApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmaFindApplication.class, args);
    }
}
