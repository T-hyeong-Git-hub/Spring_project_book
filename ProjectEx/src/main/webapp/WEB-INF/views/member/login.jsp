<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
<link rel="stylesheet" href="/resources/css/member/login.css">
</head>
<body>

<div class="wrapper">
	<div class = "top_gnb_area">
            <ul class="list">
                <li >
                    <a href="/member/login">로그인</a>
                </li>
                <li>
                    <a href="/member/join">회원가입</a>
                </li>
                <li>
                    고객센터
                </li>            
            </ul>        
		</div>
		<div class ="top_area">
			<div class = "logo_area">
				<a href ="/main"><img src="/resources/image/logo.png"></a>
			</div>
			
			<div class = "clearfix"></div>
		</div>
	
	<div class="wrap">
	 <form id="login_form" method="post">
		<div class="logo_wrap">
			<span>GUHAEBOOK</span>
		</div>
		<div class="login_wrap"> 
			<div class="id_wrap">
					<div class="id_input_box">
					 <input class="id_input" name ="memberId" placeholder ="아이디">
				</div>
			</div>
			<div class="pw_wrap">
				<div class="pw_input_box">
					<input class="pw_iput" type = "password" name ="memberPw" placeholder ="비밀번호">
				</div>
			</div>			
			 <c:if test = "${result == 0 }">
                <div class = "login_warn">사용자 ID 또는 비밀번호를 잘못 입력하셨습니다.</div>
            </c:if>
			<div class="login_button_wrap">
				<input type="button" class="login_button" value="로그인">
			</div>
			<div class = "kakao_button_wrap">
			<!-- b7dc113b5727c514f1080e4035d8952c-->
				<a class="p-2" href="https://kauth.kakao.com/oauth/authorize?client_id=b7dc113b5727c514f1080e4035d8952c&
					redirect_uri=http://localhost:8081/member/kakaoLogin&response_type=code">
					<img src ="https://www.gb.go.kr/Main/Images/ko/member/certi_kakao_login.png" style = "height: 60px; width:39%;"/>
				</a>
			</div>			
		</div>
		</form>
		
		
	</div>
		
		<br><br><br><br><br><br>
</div>

<%@include file="../includes/admin/footer.jsp" %>
 
<script>
 
    /* 로그인 버튼 클릭 메서드 */
    $(".login_button").click(function(){
        
        //alert("로그인 버튼 작동");
        
        /* 로그인 메서드 서버 요청 */
        $("#login_form").attr("action", "/member/login.do");
        $("#login_form").submit();
        
    });
 
</script>

</body>
</html>