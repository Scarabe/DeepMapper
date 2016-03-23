package br.com.deepmapper.deeprobots.robots;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@EnableEurekaClient
@ComponentScan( "br.com.deepmapper" )
@EnableFeignClients
@EnableCircuitBreaker
public class DeepInit{

	public static void main( String[ ] args ) {
		SpringApplication.run( DeepInit.class, args );
	}

}