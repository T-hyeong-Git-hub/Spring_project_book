package com.projectex.controller;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectex.model.AttachImageVO;
import com.projectex.model.AuthorVO;
import com.projectex.model.BookVO;
import com.projectex.model.Criteria;
import com.projectex.model.MemberVO;
import com.projectex.model.OrderCancelDTO;
import com.projectex.model.OrderDTO;
import com.projectex.model.PageDTO;
import com.projectex.service.AdminService;
import com.projectex.service.AuthorService;
import com.projectex.service.MemberService;
import com.projectex.service.OrderService;

import net.coobird.thumbnailator.Thumbnails;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AdminService adminService;
    
    @Autowired
	private OrderService orderService;
    
    @Autowired
	private MemberService memberService;

    /* 관리자 메인 페이지 이동 */
    @RequestMapping(value="main", method = RequestMethod.GET)
    public void adminMainGET() throws Exception{

        logger.info("관리자 페이지 이동");

    }
    /* 상품 관리(상품 목록) 페이지 접속 */
    @RequestMapping(value = "goodsManage", method = RequestMethod.GET)
    public void goodsManageGET(Criteria cri,Model model) throws Exception{
        logger.info("상품 관리(상품목록) 페이지 접속");

        /* 상품 리스트 데이터 */
		List list = adminService.goodsGetList(cri);

		if(!list.isEmpty()) {
			model.addAttribute("list", list);
		} else {
			model.addAttribute("listCheck", "empty");
			return;
		}

		/* 페이지 인터페이스 데이터 */
		model.addAttribute("pageMaker", new PageDTO(cri, adminService.goodsGetTotal(cri)));
    }

    /* 상품 등록 페이지 접속 */
    @RequestMapping(value = "goodsEnroll", method = RequestMethod.GET)
    public void goodsEnrollGET(Model model) throws Exception{
        logger.info("상품 등록 페이지 접속");

        ObjectMapper objm = new ObjectMapper();

        List list = adminService.cateList();

        String cateList = objm.writeValueAsString(list);
        //Java 객체를 String타입의 JSON형식 데이터로 변환해줍니다.

        model.addAttribute("cateList",cateList);
        // 뷰(view)로 데이터를 넘겨주기 위해서 url 매핑 메서드의 파라미터에 Model를 부여해준 후
        //addAttribute()를 사용하여 "cateList"속성에 String타입의 'cateList' 변수의 값을 저장시킵니다.

//        logger.info("변경 전.........." + list);
//		  logger.info("변경 후.........." + cateList);
    }

    /* 상품 조회 페이지 */
	@GetMapping({"/goodsDetail", "/goodsModify"})
	public void goodsGetInfoGET(int bookId, Criteria cri, Model model) throws JsonProcessingException {

		logger.info("goodsGetInfo()........." + bookId);

		ObjectMapper mapper = new ObjectMapper();

		//카테고리 리스트 데이터
		model.addAttribute("cateList", mapper.writeValueAsString(adminService.cateList()));

		/* 목록 페이지 조건 정보 */
		model.addAttribute("cri", cri);

		/* 조회 페이지 정보 */
		model.addAttribute("goodsInfo", adminService.goodsGetDetail(bookId));

	}

	/* 상품 정보 수정 */
	@PostMapping("/goodsModify")
	public String goodsModifyPOST(BookVO vo, RedirectAttributes rttr) {

		logger.info("goodsModifyPOST.........." + vo);

		int result = adminService.goodsModify(vo);

		rttr.addFlashAttribute("modify_result", result);

		return "redirect:/admin/goodsManage";

	}

	/* 상품 정보 삭제 */
	@PostMapping("/goodsDelete")
	public String goodsDeletePOST(int bookId, RedirectAttributes rttr) {

		logger.info("goodsDeletePOST................");

		List<AttachImageVO> fileList = adminService.getAttachInfo(bookId);

		if(fileList != null) {

			List<Path> pathList = new ArrayList();

			fileList.forEach(vo ->{

				// 원본 이미지
				Path path = Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName());
				pathList.add(path);

				// 섬네일 이미지
				path = Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid()+"_" + vo.getFileName());
				pathList.add(path);

			});

			pathList.forEach(path ->{
				path.toFile().delete();
			});

		}

		int result = adminService.goodsDelete(bookId);

		rttr.addFlashAttribute("delete_result",result);

		return "redirect:/admin/goodsManage";
	}

    /* 작가 등록 페이지 접속 */
    @RequestMapping(value = "authorEnroll", method = RequestMethod.GET)
    public void authorEnrollGET() throws Exception{
        logger.info("작가 등록 페이지 접속");
    }

    /* 작가 관리 페이지 접속 */
    @RequestMapping(value = "authorManage", method = RequestMethod.GET)
    public void authorManageGET(Criteria cri, Model model) throws Exception{
    	//매개변수로 cri를 넣어서 url에 paging 정보를 알 수 있는 것
    	//예를 들어 authorManage?pageNum=30&amount=10&keyword=
    	logger.info("작가 관리 페이지 접속.........." + cri);

        /* 작가 목록 출력 데이터 */
        List list = authorService.authorGetList(cri);

        if(!list.isEmpty()) {
        	model.addAttribute("list", list);   //작가 존재 경우
        }else {
        	model.addAttribute("listCheck","empty"); //작가 존재하지 않을 경우
        }

        /* 페이지 이동 인터페이스 데이터*/
        int total = authorService.authorGetTotal(cri);

        PageDTO pageMaker = new PageDTO(cri, total);

        model.addAttribute("pageMaker",pageMaker);

        /* model.addAttribute("pageMaker", new PageDTO(cri, authorService.authorGetTotal(cri)));
           위에 페이지 이동 인터페이스 데이터 코딩 3줄을 한 줄에 쓸 수도 있음
         */
    }

    /* 작가 등록 */
    @RequestMapping(value = "authorEnroll.do", method = RequestMethod.POST)
    public String authorEnrollPOST(AuthorVO author, RedirectAttributes rttr) throws Exception{
    	logger.info("authorEnroll:" + author);

    	authorService.authorEnroll(author);    //작가 등록 쿼리 수행
    	rttr.addFlashAttribute("enroll_result", author.getAuthorName());         //등록 성공 메시지(작가 이름)
    	//뷰(View)로 전송된 데이터가 일회성으로 사용되도록 addFlashAttriubte 메서드를 사용하였다.

    	return "redirect:/admin/authorManage";
    }

    //작가 상세 페이지
    @GetMapping({"/authorDetail", "/authorModify"})
    public void authorGetInfoGET(int authorId, Criteria cri, Model model) throws Exception {

    	logger.info("authorDetail...." + authorId);

    	//작가 관리 페이지 정보
    	model.addAttribute("cri", cri);

    	//선택 작가 정보
    	model.addAttribute("authorInfo", authorService.authorGetDetail(authorId));
    }

    //작가 정보 수정
    @PostMapping("/authorModify")
    public String authorModifyPOST(AuthorVO author, RedirectAttributes rttr) throws Exception{

    	logger.info("authorModifyPOST......." + author);

		int result = authorService.authorModify(author);

		rttr.addFlashAttribute("modify_result", result);

		return "redirect:/admin/authorManage";
    }

    //작가 정보 삭제
    @PostMapping("/authorDelete")
    public String authorDeletePOST(int authorId, RedirectAttributes rttr) {

    	logger.info("authorDeletePOST.........");

    	int result = 0;

    	try {

    		result = authorService.authorDelete(authorId);
    	}catch(Exception e) {//작가 테이블을 참조하고 있는 상품 테이블이 있어 참조 되고 있는 작가의 행을 지우려고 하면
    		 				 // '무결성 제약 조건을 위반'하여 예외가 발생 따라서 try-catch문으로 예외 처리

    		e.printStackTrace();
    		result = 2;
    		rttr.addFlashAttribute("delete_result", result);

    		return "redirect:/admin/authorManage";
    	}

    	rttr.addFlashAttribute("delete_result", result);

		return "redirect:/admin/authorManage";
    }

    //상품 등록
	@PostMapping("/goodsEnroll")
	public String goodsEnrollPOST(BookVO book, RedirectAttributes rttr) {

		logger.info("goodsEnrollPOST......" + book);

		adminService.bookEnroll(book);

		rttr.addFlashAttribute("enroll_result", book.getBookName());

		return "redirect:/admin/goodsManage";
	}

	// 작가 검색 팝업창
	@GetMapping("/authorPop")
	public void authorPopGET(Criteria cri, Model model) throws Exception{

		logger.info("authorPopGET.......");

		cri.setAmount(5);
		//Criteria에 기본적으로 초기화되어 있는 amount 변수 값 10을 5로 변경

		/* 게시물 출력 데이터 */
		List list = authorService.authorGetList(cri);

		if(!list.isEmpty()) {
			model.addAttribute("list",list);	// 작가 존재 경우
		} else {
			model.addAttribute("listCheck", "empty");	// 작가 존재하지 않을 경우
		}

		/* 페이지 이동 인터페이스 데이터 */
		model.addAttribute("pageMaker", new PageDTO(cri, authorService.authorGetTotal(cri)));

	}

	//첨부 파일 업로드
	@PostMapping(value="/uploadAjaxAction",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<AttachImageVO>> uploadAjaxActionPOST(MultipartFile[] uploadFile) {

		logger.info("uploadAjaxActionPOST............");

		/* 이미지 파일 체크 */
		for(MultipartFile multipartFile: uploadFile) {

			File checkfile = new File(multipartFile.getOriginalFilename());
			String type = null;

			try {
				type = Files.probeContentType(checkfile.toPath());
				logger.info("MIME TYPE : " + type);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if(!type.startsWith("image")) {

				List<AttachImageVO> list = null;
				return new ResponseEntity<>(list, HttpStatus.BAD_REQUEST);

			}

		}

		String uploadFolder = "C:\\upload";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String str = sdf.format(date);
		String datePath = str.replace("-", File.separator);

		//폴더 생성
		File uploadPath = new File(uploadFolder, datePath);

		if(!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		//이미지 정보 담는 객체
		List<AttachImageVO> list = new ArrayList();

		//향상된 for문
		for(MultipartFile multipartFile : uploadFile) {

			//이미지 정보 객체
			AttachImageVO vo = new AttachImageVO();

			/* 파일 이름 */
			String uploadFileName = multipartFile.getOriginalFilename();
			vo.setFileName(uploadFileName);
			vo.setUploadPath(datePath);

			/* uuid 적용 파일 이름 */
			String uuid = UUID.randomUUID().toString();
			vo.setUuid(uuid);

			uploadFileName = uuid + "_" + uploadFileName;

			/* 파일 위치, 파일 이름을 합친 File 객체 */
			File saveFile = new File(uploadPath, uploadFileName);

			/* 파일 저장 */
			try {
				multipartFile.transferTo(saveFile);

				//썸네일 생성(ImageIO)(방법1)
//				File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);
//
//				BufferedImage bo_image = ImageIO.read(saveFile);
//
//				//비율
//				double ratio = 3;
//				//넓이 높이
//				int width = (int)(bo_image.getWidth() / ratio);
//				int height = (int)(bo_image.getHeight() / ratio);
//
//				BufferedImage bt_image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//
//				Graphics2D graphic = bt_image.createGraphics();
//
//				graphic.drawImage(bo_image, 0, 0, width, height, null);
//
//				ImageIO.write(bt_image, "jpg", thumbnailFile);

				//썸네일 생성(thumbnailaotor 라이브러리 사용) 방법2
				File thumbnailFile = new File(uploadPath, "s_" + uploadFileName);

				BufferedImage bo_image = ImageIO.read(saveFile);

				//비율
				double ratio = 3;
				//넓이 높이
				int width = (int)(bo_image.getWidth() / ratio);
				int height = (int)(bo_image.getHeight() / ratio);

				Thumbnails.of(saveFile)
				.size(width,height)
				.toFile(thumbnailFile);


			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(vo);
		} //for

		ResponseEntity<List<AttachImageVO>> result = new ResponseEntity<>(list, HttpStatus.OK);

		return result;
	}

	//이미지 파일 삭제
	@PostMapping("/deleteFile")
	public ResponseEntity<String> deleteFile(String fileName){
		logger.info("deleteFile............" + fileName);

		File file = null;

		try {
			//썸네일 파일 삭제
			file = new File("c:\\upload\\" + URLDecoder.decode(fileName,"UTF-8"));
			file.delete();

			//원본 파일 삭제
			String originFileName= file.getAbsolutePath().replace("s_", "");
			logger.info("originFileName:" + originFileName);
			file = new File(originFileName);
			file.delete();

		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("fail", HttpStatus.NOT_IMPLEMENTED);
		}

		return new ResponseEntity<>("sucess", HttpStatus.OK);
	}
	
	//주문 현황 페이지
	@GetMapping("/orderList")
	public String orderListGET(Criteria cri, Model model) {
		List<OrderDTO> list = adminService.getOrderList(cri);
		
		if(!list.isEmpty()) {
			model.addAttribute("list", list);
			model.addAttribute("pageMaker", new PageDTO(cri, adminService.getOrderTotal(cri)));
		}else {
			model.addAttribute("listCheck", "empty");
		}
		
		return "/admin/orderList";
	}
	
	// 주문삭제 
	@PostMapping("/orderCancle")
	public String orderCanclePOST(OrderCancelDTO dto, HttpServletRequest request) {
		
		orderService.orderCancle(dto);
		
		HttpSession session = request.getSession();
		
		MemberVO member = new MemberVO();
		member.setMemberId(dto.getMemberId());
		
		try {
			MemberVO memberLogin = memberService.memberLogin(member);
			memberLogin.setMemberPw("");
			session.setAttribute("member", memberLogin);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return "redirect:/admin/orderList?keyword=" + dto.getKeyword() + "&amount=" + dto.getAmount() + "&pageNum=" + dto.getPageNum();
		// 리다이렉트 방식으로 '주문현황' 페이지를 반환, 사용자가 머물고 있었던 페이지에 이동할 수 있도록 쿼리 파라미터를 부여해줌.
	}

}