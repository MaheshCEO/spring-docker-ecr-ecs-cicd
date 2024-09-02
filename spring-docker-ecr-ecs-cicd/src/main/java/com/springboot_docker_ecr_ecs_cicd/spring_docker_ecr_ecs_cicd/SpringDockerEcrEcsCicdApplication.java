package com.springboot_docker_ecr_ecs_cicd.spring_docker_ecr_ecs_cicd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
public class SpringDockerEcrEcsCicdApplication {

	
	@GetMapping("/message")
	public String getMessage(String str)
	{
		return "Welcome to AWS practice for using Docker and  Ecr and ecs using cicd Example " + str;
		//return "Welcome to Docker Example "  test branch
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SpringDockerEcrEcsCicdApplication.class, args);
	}

}
