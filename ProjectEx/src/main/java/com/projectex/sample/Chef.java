package com.projectex.sample;

import org.springframework.stereotype.Component;

import lombok.Data;

/*- @Component를 통해서 해당 객체는 스프링에서 관리해야하는 객체로 인식 되어집니다.

- @Data를 통해서 해당 객체의 Getter/Setter/toString이 자동 형성됩니다.(lombok라이브러리 사용자 해당)*/

@Component
@Data
public class Chef {

}
