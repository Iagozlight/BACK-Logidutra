package br.com.uniamerica.logidutra;

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
