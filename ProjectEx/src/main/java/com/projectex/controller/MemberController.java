package com.projectex.controller;

import java.util.Random;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projectex.model.KakaoDTO;
import com.projectex.model.MemberVO;
import com.projectex.service.MemberService;

@Controller
@RequestMapping(value="/member")
public class MemberController {
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

	@Autowired
	private MemberService memberservice;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
    private BCryptPasswordEncoder pwEncoder;

	//회원가입 페이지 이동
	@RequestMapping(value ="/join", method = RequestMethod.GET)
	public void loginGET() {

		logger.info("회원가입 페이지 진입");

	}
	//회원가입
		@RequestMapping(value="/join", method=RequestMethod.POST)
		public String joinPOST(MemberVO member) throws Exception{

			//logger.info("join 진입");
			String rawPw = "";            // 인코딩 전 비밀번호
		    String encodePw = "";        // 인코딩 후 비밀번호

		     rawPw = member.getMemberPw();            // 비밀번호 데이터 얻음
		     encodePw = pwEncoder.encode(rawPw);        // 비밀번호 인코딩
		     member.setMemberPw(encodePw);            // 인코딩된 비밀번호 member객체에 다시 저장

		     /* 회원가입 쿼리 실행 */
		     memberservice.memberJoin(member);

		     return "redirect:/main";

		}

	//로그인 페이지 이동
	@RequestMapping(value ="/login", method = RequestMethod.GET)
	public void joinGET() {

		logger.info("로그인 페이지 진입");

	}

	//아이디 중복 검사
	@RequestMapping(value = "/memberIdChk", method = RequestMethod.POST)
	@ResponseBody
	//@Responsebody 어노테이션을 사용하면 http요청 body를 자바 객체로 전달받을 수 있다.
	public String memberIdChkPOST(String memberId) throws Exception{

		/* logger.info("memberIdChk() 진입"); */

		logger.info("memberIdChk() 진입");

		int result = memberservice.idCheck(memberId);

		logger.info("결과값 = " + result);

		if(result != 0) {

			return "fail";	// 중복 아이디가 존재

		} else {

			return "success";	// 중복 아이디 x

		}

	}//memberIdChkPOST() 종료

	/*이메일 인증*/
	@RequestMapping(value = "/mailCheck", method = RequestMethod.GET)
	@ResponseBody
	public String mailCheckGET(String email) throws Exception{

		/* 뷰(view)로부터 넘어온 데이터 확인*/
		logger.info("이메일 데이터 전송 확인");
		logger.info("인증 번호:" + email);

		/*인증번호 난수 생성*/
		Random random = new Random();
		int checkNum = random.nextInt(888888) + 111111;
		logger.info("인증 번호:" + checkNum);

		/* 이메일 보내기 */
        String setFrom = "kkth0705@naver.com";
        //- setForm : root-context.xml에 삽입한 자신의 이메일 계정의 이메일 주소입니다. (아이디만 입력하는 것이 아니라 이메일 주소를 입력해야 합니다.)
        String toMail = email;
        String title = "회원가입 인증 이메일 입니다.";
        String content =
                "홈페이지를 방문해주셔서 감사합니다." +
                "<br><br>" +
                "인증 번호는 " + checkNum + "입니다." +
                "<br>" +
                "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";

        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(setFrom);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true);
            mailSender.send(message);

        }catch(Exception e) {
            e.printStackTrace();
        }

        String num = Integer.toString(checkNum);

        return num;
	}

    /* 로그인 */
    @RequestMapping(value="login.do", method=RequestMethod.POST)
    public String loginPOST(HttpServletRequest request, MemberVO member, RedirectAttributes rttr) throws Exception{

    	HttpSession session = request.getSession();
        //request의 getSession() 메서드는 서버에 생성된 세션이 있다면 세션을 반환하고, 없다면 새 세션을 생성하여 반환한다. (인수 default가 true)
        String rawPw = "";
        String encodePw = "";

        MemberVO lvo = memberservice.memberLogin(member);    // 제출한아이디와 일치하는 아이디 있는지
        //memberLogin 메서드를 요청하게 되면 MemberMapper.java를 거쳐서 로그인 쿼리가 실행이 되게 되고
        //그 결과 값이 담긴 MemberVO 객체를 반환받아서 변수 lvo에 저장되게 된다.

        if(lvo != null) {            // 일치하는 아이디 존재시

            rawPw = member.getMemberPw();        // 사용자가 제출한 비밀번호
            encodePw = lvo.getMemberPw();        // 데이터베이스에 저장한 인코딩된 비밀번호

            if(pwEncoder.matches(rawPw, encodePw)) {        // 비밀번호 일치여부 판단

                lvo.setMemberPw("");                    // 인코딩된 비밀번호 정보 지움
                session.setAttribute("member", lvo);     // session에 사용자의 정보 저장
                return "redirect:/main";        // 메인페이지 이동


            } else {

                rttr.addFlashAttribute("result", 0);
                return "redirect:/member/login";    // 로그인 페이지로 이동

            }

        } else {                    // 일치하는 아이디가 존재하지 않을 시 (로그인 실패)

            rttr.addFlashAttribute("result", 0);
            return "redirect:/member/login";    // 로그인 페이지로 이동

        }
  }

    /* 메인페이지 로그아웃 */
    @RequestMapping(value="logout.do", method=RequestMethod.GET)
    public String logoutMainGET(HttpServletRequest request) throws Exception{

    	logger.info("logoutMainGET메서드 진입");

    	HttpSession session = request.getSession();

    	session.invalidate();

        return "redirect:/main";

    }
    /* 비동기방식 로그아웃 메서드 */
    @RequestMapping(value="logout.do", method=RequestMethod.POST)
    @ResponseBody
    public void logoutPOST(HttpServletRequest request) throws Exception{

        logger.info("비동기 로그아웃 메서드 진입");

        HttpSession session = request.getSession();

        session.invalidate();

    }
    
    @RequestMapping(value="/kakaoLogin", method=RequestMethod.GET)
    public String kakaoLogin(@RequestParam(value ="code" , required = false) String code,HttpServletRequest request, RedirectAttributes rttr) throws Exception{
    	
    	System.out.println("#########" + code);
		/*
		 * 리턴값의 testPage는 아무 페이지로 대체해도 괜찮습니다.
		 * 없는 페이지를 넣어도 무방합니다.
		 * 404가 떠도 제일 중요한건 #########인증코드 가 잘 출력이 되는지가 중요하므로 너무 신경 안쓰셔도 됩니다.
		 */
		
		//위에서 만든 코드 아래에 코드 추가
		String access_Token = memberservice.getAccessToken(code);
		KakaoDTO userInfo = memberservice.getUserInfo(access_Token);
		System.out.println("###access_Token#### : " + access_Token);
		System.out.println("###nickname#### : " + userInfo.getK_name());
		System.out.println("###email#### : " + userInfo.getK_email());
		
		HttpSession session = request.getSession();
		
		
		session.setAttribute("kakaoN", userInfo.getK_name());
		session.setAttribute("kakaoE", userInfo.getK_email());
		// 위 2개의 코드는 닉네임과 이메일을 session객체에 담는 코드
		// jsp에서 ${sessionScope.kakaoN} 이런 형식으로 사용할 수 있다.
	    
		return "redirect:/kakaoMain";
    	
    }
}
