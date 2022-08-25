package com.projectex.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Setter;

@Component
@Data
public class Restaurant {

@Setter(onMethod_ = @Autowired)
private Chef chef;


}
///**
// * @Component는 스프링에게 해당 클래스가 스프링에서 관리해야 하는 대상임을 표시하는 어노테이션입니다.
// *
// *
// * @Data는 클래스안의 모든 private 필드에 대해 @Getter(Getter 메서드 생성)와 @Setter(Setter 메서드
// *        생성)를 적용하여 세터/게터를 만들어주고 클래스내에 @ToString(toString 메서드 생성)
// *        과 @EqualsAndHashCode(equals 메서드, hashcode메서드 생성)를 적용시켜 메소드를 오버라이드
// *        해주며 @RequiredArgsConstructor(생성자 자동 생성, null chek 실행)를 지정해
// *        줍니다.(Lombok라이브러리에서 제공)
// *
// *
// * @Autowired는 스프링 내부에서 자신이 특정한 객체에 의존적이므로 자신에게 해당 타입의 빈을 주입해주라는 표시입니다.
// *
// * @Setter(onMethod_ = @Autowired)는 setter 메서드의 생성 시 메서드에 추가할 어노테이션을 지정합니다. 해당
// *                   어노테이션에서는 @Autowired가 추가됩니다.