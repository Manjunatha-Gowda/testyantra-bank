package com.te.testyantrabank.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.te.testyantrabank" })
@EnableJpaRepositories(basePackages = "com.te.testyantrabank.dao")
public class JpaConfiguration {

}
