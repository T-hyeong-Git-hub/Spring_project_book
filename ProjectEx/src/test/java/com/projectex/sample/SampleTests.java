package com.projectex.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class SampleTests {

@Autowired
private Restaurant restaurant;

@Test
public void textExist() {

assertNotNull(restaurant);

log.info(restaurant);
log.info("--------------------------------------");
log.info(restaurant.getChef());

}
//@Runwit는 해당 클래스에 있는 코드(테스트코드)가 스프링을 실행하는 역할이라는 것을 표시합니다.
//
//@ContextConfiguration는 지정된 클래스나 문자열을 이용해서 필요한 객체들을 스프링 내에 객체(빈, Bean)로 등록합니다.
//
//@Log4j는 Lombok을 이용해서 로그를 기록하는 Logger를 변수로 생성합니다.
//
//@Test는 JUnit에서 테스트 대상을 표시합니다.

}

