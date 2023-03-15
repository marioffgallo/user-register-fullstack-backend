package com.marioffgallo.userregisterfullstackbackend.userregisterfullstackbackend;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class UserRegisterFullstackBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserRegisterFullstackBackendApplication.class, args);
    }

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

}
