package com.cos.fluxdemo.web;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.fluxdemo.domain.Customer;
import com.cos.fluxdemo.domain.CustomerRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;


@RestController
public class CustomerController {
	
	private final CustomerRepository customerRepository;
	private final Sinks.Many<Customer> sink;
	
	// A 요청 -> Flux -> Stream
	// B 요청 -> Flux -> Stream
	// -> Flux.merge -> sink 
	// 두개 의 stream flux 를 merge 할 수 있다
	
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
		this.sink = Sinks.many().multicast().onBackpressureBuffer(); //새로 푸시 된 데이터만 구독자에게 전달해주는 방식
		// 모들 client 의 flux 요청을 접근 할 수 있다. 
		
	}
	
	@GetMapping("/flux")
	public Flux<Integer> flux(){
		return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log(); //5초가 지나야 응답 
		
//		2022-04-30 20:06:40.853  INFO 43572 --- [ctor-http-nio-2] reactor.Flux.ConcatMap.1                 : onSubscribe(FluxConcatMap.ConcatMapImmediate)
//		2022-04-30 20:06:40.856  INFO 43572 --- [  restartedMain] .ConditionEvaluationDeltaLoggingListener : Condition evaluation unchanged
//		2022-04-30 20:06:40.856  INFO 43572 --- [ctor-http-nio-2] reactor.Flux.ConcatMap.1                 : request(unbounded)
//		2022-04-30 20:06:41.866  INFO 43572 --- [     parallel-3] reactor.Flux.ConcatMap.1                 : onNext(1)
//		2022-04-30 20:06:42.875  INFO 43572 --- [     parallel-4] reactor.Flux.ConcatMap.1                 : onNext(2)
//		2022-04-30 20:06:43.889  INFO 43572 --- [     parallel-5] reactor.Flux.ConcatMap.1                 : onNext(3)
//		2022-04-30 20:06:44.898  INFO 43572 --- [     parallel-6] reactor.Flux.ConcatMap.1                 : onNext(4)
//		2022-04-30 20:06:45.908  INFO 43572 --- [     parallel-7] reactor.Flux.ConcatMap.1                 : onNext(5)
//		2022-04-30 20:06:45.909  INFO 43572 --- [     parallel-7] reactor.Flux.ConcatMap.1                 : onComplete()
	}
	
	
	@GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Integer> fluxstream(){
		return Flux.just(1,2,3,4,5).delayElements(Duration.ofSeconds(1)).log(); //1번 onnext 가 될 때마다 buffer를 flush
		
//		2022-04-30 20:09:46.292  INFO 43572 --- [ctor-http-nio-2] reactor.Flux.ConcatMap.3                 : onSubscribe(FluxConcatMap.ConcatMapImmediate)
//		2022-04-30 20:09:46.292  INFO 43572 --- [ctor-http-nio-2] reactor.Flux.ConcatMap.3                 : request(1)
//		2022-04-30 20:09:47.292  INFO 43572 --- [     parallel-7] reactor.Flux.ConcatMap.3                 : onNext(1)
//		2022-04-30 20:09:47.294  INFO 43572 --- [ctor-http-nio-2] reactor.Flux.ConcatMap.3                 : request(31)
//		2022-04-30 20:09:48.307  INFO 43572 --- [     parallel-8] reactor.Flux.ConcatMap.3                 : onNext(2)
//		2022-04-30 20:09:49.318  INFO 43572 --- [     parallel-1] reactor.Flux.ConcatMap.3                 : onNext(3)
//		2022-04-30 20:09:50.328  INFO 43572 --- [     parallel-2] reactor.Flux.ConcatMap.3                 : onNext(4)
//		2022-04-30 20:09:51.339  INFO 43572 --- [     parallel-3] reactor.Flux.ConcatMap.3                 : onNext(5)
//		2022-04-30 20:09:51.339  INFO 43572 --- [     parallel-3] reactor.Flux.ConcatMap.3                 : onComplete()

	}
	
	//data 가 1건이면 mono 여러건이면 flux
	
	//@GetMapping("/customer")
	@GetMapping(value = "/customer", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Customer> findAll(){
		return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
		
		
		// customer 호출 시 onSubscribe -> onNext -> onComplete complete 가 되는 순간 응답이 되었다. 
		//2022-04-30 20:01:25.634  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onSubscribe(FluxUsingWhen.UsingWhenSubscriber)
		//2022-04-30 20:01:25.634  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : request(unbounded)
		//2022-04-30 20:01:25.636  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onNext(Customer(id=1, firstName=Jack, lastName=Bauer))
		//2022-04-30 20:01:25.636  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onNext(Customer(id=2, firstName=Chloe, lastName=O'Brian))
		//2022-04-30 20:01:25.636  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onNext(Customer(id=3, firstName=Kim, lastName=Bauer))
		//2022-04-30 20:01:25.636  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onNext(Customer(id=4, firstName=David, lastName=Palmer))
		//2022-04-30 20:01:25.637  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onNext(Customer(id=5, firstName=Michelle, lastName=Dessler))
		//2022-04-30 20:01:25.637  INFO 42316 --- [ctor-http-nio-2] reactor.Flux.UsingWhen.5                 : onComplete()
	}
	
	
	
	
	
	@GetMapping("/customer/{id}") // 한번만 
	public Mono<Customer> findById(@PathVariable long id){
		return customerRepository.findById(id).log();
		
//		[2m2022-04-30 20:12:52.239  INFO 43572 --- [ctor-http-nio-3] reactor.Mono.UsingWhen.4                 : onSubscribe(MonoUsingWhen.MonoUsingWhenSubscriber)
//		 2022-04-30 20:12:52.239  INFO 43572 --- [ctor-http-nio-3] reactor.Mono.UsingWhen.4                 : request(unbounded)
//		 2022-04-30 20:12:52.241  INFO 43572 --- [ctor-http-nio-3] reactor.Mono.UsingWhen.4                 : onNext(Customer(id=1, firstName=Jack, lastName=Bauer))
//		 2022-04-30 20:12:52.241  INFO 43572 --- [ctor-http-nio-3] reactor.Mono.UsingWhen.4                 : onComplete()
	}
	
	@GetMapping(value = "/customer/sse") // produces= MediaType.TEXT_EVENT_STREAM_VALUE)  생략 가능 
	public Flux<ServerSentEvent<Customer>> findallSSE(){
		//return customerRepository.findAll().delayElements(Duration.ofSeconds(1)).log();
		
		return sink.asFlux().map(c -> ServerSentEvent.builder(c).build()).doOnCancel(() ->{
			sink.asFlux().blockLast(); // 내부적으로 oncomplete 수행   doOnCancel blockLast 을 해줘야 해당 page(session) 에서 재호출 가능 
		});
	}
	
	@PostMapping("/customer") 
	public Mono<Customer> save(){
		return customerRepository.save(new Customer("jaemo", "cho")).doOnNext(c -> {
			sink.tryEmitNext(c); // publisher 의 data가 하나 추가 됨 
		});
	}
}


