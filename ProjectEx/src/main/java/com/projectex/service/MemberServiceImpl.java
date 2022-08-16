package com.projectex.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.projectex.mapper.MemberMapper;
import com.projectex.model.KakaoDTO;
import com.projectex.model.MemberVO;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	MemberMapper membermapper;
	
	//회원가입
	@Override
	public void memberJoin(MemberVO member) throws Exception {
		membermapper.memberJoin(member);
	}
	
	//아이디 중복 검사
	@Override
	public int idCheck(String memberId) throws Exception {

		return membermapper.idCheck(memberId);
	}
	
	//로그인
	@Override
	public MemberVO memberLogin(MemberVO member) throws Exception {

		return membermapper.memberLogin(member);
	}
	
	// 주문자 정보 
	@Override
	public MemberVO getMemberInfo(String memberId) {
		
		return membermapper.getMemberInfo(memberId);
		
	}
	
	//카카오 로그인
	@Override
	public String getAccessToken(String authorize_code) {
		String access_Token = "";
		String refresh_Token = "";
		String reqURL = "https://kauth.kakao.com/oauth/token";
		
		try {
				URL url = new URL(reqURL);
				
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				
				// POST 요청을 위해 기본값이 false인 setDoOutput을 true로
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				
				//POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
				StringBuilder sb = new StringBuilder();
				sb.append("grant_type=authorization_code");
	            
				sb.append("&client_id=b7dc113b5727c514f1080e4035d8952c"); //본인이 발급받은 key
				sb.append("&redirect_uri=http://localhost:8081/member/kakaoLogin"); // 본인이 설정한 주소
	            
				sb.append("&code=" + authorize_code);
				bw.write(sb.toString());
				bw.flush();
	            
				// 결과 코드가 200이라면 성공
				int responseCode = conn.getResponseCode();
				System.out.println("responseCode : " + responseCode);
	            
				// 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				String result = "";
	            
				while ((line = br.readLine()) != null) {
					result += line;
				}
				System.out.println("response body : " + result);
	            
				// gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
				JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(result);
				            
				access_Token = element.getAsJsonObject().get("access_token").getAsString();
				refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
	            
				System.out.println("access_token : " + access_Token);
				System.out.println("refresh_token : " + refresh_Token);
	            
				br.close();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return access_Token;
		}
	@Override	
	public KakaoDTO getUserInfo(String access_Token) {
		HashMap<String, Object> userInfo = new HashMap<String, Object>();
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + access_Token);
			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String result = "";
			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println("response body : " + result);
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);
			JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
			JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
			String nickname = properties.getAsJsonObject().get("nickname").getAsString();
			String email = kakao_account.getAsJsonObject().get("email").getAsString();
			userInfo.put("nickname", nickname);
			userInfo.put("email", email);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		KakaoDTO result = membermapper.findKakao(userInfo);
		//위 코드는 먼저 정보가 저장 되어 있는 지를 확인하는 코드
		
		System.out.println("S:" + result);
		if(result == null) {
			//result가 null이면 정보가 저장이 안되어 있는 것이므로 정보를 저장
			membermapper.kakaoInsert(userInfo);
			//위 코드가 정보를 저장하기 위해 
			
			return membermapper.findKakao(userInfo);
			//위 코드는 정보 저장 후 컨트롤러에 정보를 보내는 코드임.
			//result를 리턴으로 보내면 null이 리턴 되므로 위 코드를 사용
		}else {
			return result;
			//정보가 이미 있으므로 result를 리턴
		}
	}
	
}

