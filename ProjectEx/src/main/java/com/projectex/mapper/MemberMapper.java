package com.projectex.mapper;

import java.util.HashMap;

import com.projectex.model.KakaoDTO;
import com.projectex.model.MemberVO;

public interface MemberMapper {

	//회원가입
	public void memberJoin(MemberVO member);

	// 아이디 중복 검사
	public int idCheck(String memberId);

    /* 로그인 */
    public MemberVO memberLogin(MemberVO member);
    
    /* 주문자 주소 정보 */
	public MemberVO getMemberInfo(String memberId);
	
	//카카오 정보 저장
	public void kakaoInsert(HashMap<String, Object> userInfo);
	
	//카카오 정보 확인
	public KakaoDTO findKakao(HashMap<String, Object> userInfo);

}
