package cn.feuler.bs.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Create by: Feuler
 * Date: 2019/3/19
 **/

@SpringBootApplication
@ComponentScan("cn.feuler.bs.basic")
public class BasicApplication {

    public static void main(String[] args) {
        SpringApplication.run(BasicApplication.class, args);
    }

}
