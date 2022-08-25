package com.projectex.service;

import com.projectex.model.KakaoDTO;
import com.projectex.model.MemberVO;

public interface MemberService {
	//회원가입
	public void memberJoin(MemberVO member) throws Exception;

	// 아이디 중복 검사
	public int idCheck(String memberId) throws Exception;

	//로그인
	public MemberVO memberLogin(MemberVO member) throws Exception;
	
	// 주문자 정보 
	public MemberVO getMemberInfo(String memberId);
	
	//카카오 로그인 
	public String getAccessToken(String authorize_code);
	
	//카카오 토근
	public KakaoDTO getUserInfo(String access_Token);
	
}
