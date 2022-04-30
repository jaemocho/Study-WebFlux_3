package com.cos.fluxdemo.domain;

//R2DBC의 Repository 를 사용해야한다 not jpa 
//flux는 비동기 방식을 사용하기 때문에 jpa를 사용하면 동기화 되므로 비동기를 지원하는 r2dbc사용 
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {

    @Query("SELECT * FROM customer WHERE last_name = :lastname")
    Flux<Customer> findByLastName(String lastName);   

}