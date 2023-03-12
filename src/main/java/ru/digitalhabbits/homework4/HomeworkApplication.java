package ru.digitalhabbits.homework4;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class HomeworkApplication
        implements ApplicationRunner {
    private static final Logger logger = getLogger(HomeworkApplication.class);

    @Value("${homework.message}")
    private String message;

    public static void main(String[] args) {
        SpringApplication.run(HomeworkApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info(message);
    }
}
