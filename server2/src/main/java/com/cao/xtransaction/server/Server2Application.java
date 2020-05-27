package com.cao.xtransaction.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ComponentScan({"com.cao.xtransaction"})
public class Server2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Server2Application.class, args);

    }
}
