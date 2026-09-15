package br.com.uniamerica.Logidutra;

import org.aspectj.weaver.ast.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class  LogidutraApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogidutraApplication.class, args);
	}
}
