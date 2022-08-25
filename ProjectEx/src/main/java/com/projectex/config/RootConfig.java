package com.projectex.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages= {"com.projectex.sample"})
public class RootConfig {

}

//- @Configuration 은 스프링 IOC Container에게 해당 클래스를 Bean 구성 Class임을 알려주는 어노테이션입니다.

//- @ComponentScan 은[root-context xml 설정 방식 "2)"] 에서 추가한 <context:component-scan base-package="패키지 경로"> 와 같을 역할을 수행합니다.