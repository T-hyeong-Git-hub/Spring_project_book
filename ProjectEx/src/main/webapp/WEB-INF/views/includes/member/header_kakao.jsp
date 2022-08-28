<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<script src="https://kit.fontawesome.com/1986c6b16c.js" crossorigin="anonymous"></script>
<link rel="stylesheet" href="../resources/css/member/header.css">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.3/font/bootstrap-icons.css">

<div class="wrapper">
	<div class="wrap">
		<div class="top_gnb_area">
			<a class="home_logo" href="/main">
				<i class="fa-solid fa-house"></i>
		    </a>
			<ul class="list">
				<c:if test = "${kakaoN == null}">	<!-- 비 로그인 -->
					<li>
						<a href="/member/login">로그인</a>	
					</li>
					<li>
						<a href="/member/join">회원가입</a>
					</li>
				</c:if>
				
				<c:if test = "${kakaoN != null}"> 	<!-- 로그인 -->
					<c:if test = "${member.adminCk == 1 }">	<!-- 관리자 계정 -->
                        <li><a href="/admin/main">관리자 페이지</a></li>
                    </c:if>
					<li>
						<a id="gnb_logout_button">로그아웃</a>
					</li>
					<li>
						<a href="/cart/${member.memberId}">장바구니</a>
					</li>
				</c:if>
			</ul>
		</div>
		
		<div class="top_area">
			<!-- 로고 영역 -->
			<div class="logo_area">
				<a href="/main"><img src="resources/image/BookShop.png"></a>
			</div>
			<div class="search_area">
				<div class="search_wrap">
               		
               		<form id="searchForm" action="/search" method="get">
               			<div class="search_input">
           					<select name="type">
               					<option value="T"> 책 제목 </option>
               					<option value="A"> 작 가 </option>
            				</select>
               				<input type="text" name="keyword">
                   			<button class='btn search_btn'>
								<i class="fa-solid fa-magnifying-glass"></i>
							</button>                				
               			</div>
               		</form>
               	
               	</div>
			</div>
			
			<div class="login_area">
			<!-- 로그인 하지 않은 상태 -->
			<c:if test = "${kakaoN == null}">
				<!-- <div class="login_button"><a href="/member/login">로그인</a></div> -->
						<div class="slick_banner">
				
					<div>
						<a>
							<img src="../resources/image/25SALE.jpg">
						</a>
					</div>
					<div>
						<a>
							<img src="../resources/image/50SALE.jpg">
						</a>
					</div>
					<div>
						<a>
							<img src="../resources/image/SUMMER_SALE.jpg">
						</a>
					</div>				
				</div>
			</c:if>
			
			<!-- 로그인한 상태 -->
	        <c:if test="${ kakaoN != null }">
                <div class="login_success_area">
                    <span><i class="bi bi-person-fill"></i> 회원 : ${kakaoN}</span>
                    <span><i class="bi bi-currency-dollar"></i> 충전금액 : <fmt:formatNumber value="0" pattern="\#,###.##"/></span>
                    <span><i class="bi bi-coin"></i> 포인트 : <fmt:formatNumber value="0" pattern="#,###" /></span>
                    	 <i class="bi bi-x-circle-fill"><a href="/member/logout.do">로그아웃</a></i>
                </div>
            </c:if>		
			
			</div>
			
			<script>
			
			$(document).ready(function(){
				
				/* 베너 슬릭 */
				$(".slick_banner").slick(
					{
						dots : false,
						autoplay : true,
						autoplaySpeed : 3000,
						arrows : false
					}		
				);
				
			});
			
			</script>