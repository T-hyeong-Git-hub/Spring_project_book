<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel='stylesheet' href='https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.5.0/css/bootstrap.min.css'>
<!-- font awesome -->
<script src="https://kit.fontawesome.com/1986c6b16c.js" crossorigin="anonymous"></script>
<!-- <link rel='stylesheet' href='https://unicons.iconscout.com/release/v2.1.9/css/unicons.css'><link rel="stylesheet" href="./style.css"> -->
<script
  src="https://code.jquery.com/jquery-3.4.1.js"
  integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
  crossorigin="anonymous"></script>
<link rel="stylesheet" href="/resources/css/member/login.css">
</head>
<body>

<form id="login_form" method="post">
	<!-- <div class="section"> -->
		<div class="container">
			<div class="top_gnb_area">
			  <a class="home_logo" href="/main">
				<i class="fa-solid fa-house"></i>
			  </a>
					<ul class="list">
						<c:if test = "${member == null }">	<!-- 비 로그인 -->
							<li>
								<a href="/member/login">로그인</a>	
							</li>
							<li>
								<a href="/member/join">회원가입</a>
							</li>
						</c:if>
					</ul>
		     </div>
			<div class="row full-height justify-content-center">
				<div class="col-12 text-center align-self-center py-5">
					<div class="section pb-5 pt-5 pt-sm-2 text-center">
						<div class="card-3d-wrap mx-auto">
							<div class="card-3d-wrapper">
								<div class="card-front">
									<div class="center-wrap">
										<div class="section text-center">
											<h4 class="mb-4 pb-3">Log In</h4>
											<div class="form-group">
												<input type="text" name="memberId" class="form-style" placeholder="Your Id" id="logemail" autocomplete="off">
												<i class="input-icon uil uil-at"></i>
											</div>	
											<div class="form-group mt-2">
												<input type="password" name="memberPw" class="form-style" placeholder="Your Password" id="logpass" autocomplete="off">
												<i class="input-icon uil uil-lock-alt"></i>
													<c:if test = "${result == 0 }">
														<div class="login_warn">사용자 ID 또는 비밀번호를 잘못 입력하셨습니다.</div>
													</c:if>
											</div>
											<div class="login_button_wrap">
												<input type="button" class="login_button" value="로그인">
											</div>
											<div class = "kakao_button_wrap">
												<a class="p-2" href="https://kauth.kakao.com/oauth/authorize?
																client_id=<spring:eval expression="@config.getProperty('KAKAO_API_KEY')"/>&
																redirect_uri=http://localhost:8081/member/kakaoLogin&response_type=code">
												<img src ="https://www.gb.go.kr/Main/Images/ko/member/certi_kakao_login.png" style = "height: 60px; width:39%;"/>
												</a>
											</div>	
                            				<p class="mb-0 mt-4 text-center"><a href="/member/join" class="link">회원가입을 하시겠습니까?</a></p>
				      					</div>
			      					</div>
			      				</div>
			      				
			      			</div>
			      		</div>
			      	</div>
		      	</div>
	      	</div>
	    </div>
	<!-- </div> -->
</form>

<!-- <script  src="resources/js/script.js"></script> -->
<script>
 
    /* 로그인 버튼 클릭 메서드 */
    $(".login_button").click(function(){
        
        /* alert("로그인 버튼 작동"); */
        
        /* 로그인 메서드 서버 요청 */
        $("#login_form").attr("action", "/member/login.do");
        $("#login_form").submit();
        
    });
 
</script>

</body>
 
</html>