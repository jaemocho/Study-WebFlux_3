package com.cos.fluxdemo.web;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.cos.fluxdemo.domain.Customer;
import com.cos.fluxdemo.domain.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//@WebFluxTest 
// controller를 메모리에 띄워준다. 현재 pjt는 controller만 있고 service가 없기 때문에 interface 인 CustomerRepository 를 사용할 방법이 없다.
// WebFluxTest는 CustomerRepository 를 안 띄워놓기 때문에 autowired 도 불가 
// MockBean으로 CustomerRepository 를 선언하면 실제 객체가 아니라 사용 불가 

//@SpringBootTest // 통합테스트 spring에 대한 모든 context를 띄워주는 test annotation
//@AutoConfigureWebTestClient
@WebFluxTest
public class CustomerControllerTest {
	
	 @MockBean
	 CustomerRepository customerRepository;
	 
	 @Autowired
	 private WebTestClient webClient;
	 	 
	 
	 @Test
	 public void 한건찾기_테스트2() {
		 //stub -> 행동 지시 
		 when(customerRepository.findById(1L)).thenReturn(Mono.just(new Customer("Jack","Bauer")));
		 
		 webClient.get().uri("/customer/{id}",1L)
		 .exchange()
		 .expectBody()
		 .jsonPath("$.firstName").isEqualTo("Jack")
		 .jsonPath("$.lastName").isEqualTo("Bauer");
		 
	 }
	
}
