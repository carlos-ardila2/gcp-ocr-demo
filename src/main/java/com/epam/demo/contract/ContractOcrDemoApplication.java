package com.epam.demo.contract;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** This class serves as an entry point for the Spring Boot app. */
@SpringBootApplication
public class ContractOcrDemoApplication {

  private static final Logger logger = LoggerFactory.getLogger(ContractOcrDemoApplication.class);

  public static void main(final String[] args) throws Exception {
    // Start the Spring Boot application.
    SpringApplication.run(ContractOcrDemoApplication.class, args);
    logger.info("Cloud Run ready! The container started successfully and is listening for HTTP requests");
  }
}
