package com.inet;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.inet.codebase.mapper")
public class SpringBootNetworkDiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootNetworkDiskApplication.class, args);
    }

}
