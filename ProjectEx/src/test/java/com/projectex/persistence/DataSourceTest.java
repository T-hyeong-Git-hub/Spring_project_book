package com.projectex.persistence;

import java.sql.Connection;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//진행할 테스트는 스프링에 빈(Bean)으로 등록된 DataSource를 이용해서
//Connection을 제대로 처리할 수 있는지를 확인하기 위한 용도입니다.

//1) 기존 DataSourceTest.java 클래스에 테스트할 코드를 추가합니다.
//- SqlSessionFacory 객체를 주입시킵니다.
//- try문에 SqlSession 객체를 인스턴스화 하는 코드와 출력문 코드를 추가합니다.

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class DataSourceTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Test
	public void testConnection() {
		try(
			Connection con = dataSource.getConnection();
			SqlSession session = sqlSessionFactory.openSession();
		){

		System.out.println("con=" + con);
		System.out.println("session=" + session);

		}catch(Exception e) {
			e.printStackTrace();
		}

	}

}