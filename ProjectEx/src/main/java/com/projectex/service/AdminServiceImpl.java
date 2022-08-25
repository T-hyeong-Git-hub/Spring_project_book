package com.projectex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectex.mapper.AdminMapper;
import com.projectex.model.AttachImageVO;
import com.projectex.model.BookVO;
import com.projectex.model.CateVO;
import com.projectex.model.Criteria;
import com.projectex.model.OrderDTO;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class AdminServiceImpl implements AdminService{

	@Autowired
	AdminMapper adminMapper;

	//상품 등록
	@Transactional
	@Override
	public void bookEnroll(BookVO book) {

		log.info("(srevice)bookEnroll........");

		adminMapper.bookEnroll(book);

		if(book.getImageList() == null || book.getImageList().size() <= 0) {
			return;
		}

		// 향상된 for문
		for(AttachImageVO attach : book.getImageList()) {

			attach.setBookId(book.getBookId());
			adminMapper.imageEnroll(attach);

		}

	}
	//카테고리 리스트
	@Override
	public List<CateVO> cateList() {

		log.info("(srevice)cateList........");

		return  adminMapper.cateList();
	}
	//상품 리스트
	@Override
	public List<BookVO> goodsGetList(Criteria cri) {

		log.info("goodsGetList()........");
		return  adminMapper.goodsGetList(cri);
	}
	//상품 총 개수
	@Override
	public int goodsGetTotal(Criteria cri) {
		log.info("goodsGetTotal().......");
		return  adminMapper.goodsGetTotal(cri);
	}
	//상품 조회 페이지
	@Override
	public BookVO goodsGetDetail(int bookId) {
		log.info("goodsGetDetail()......." + bookId);

		return adminMapper.goodsGetDetail(bookId);
	}
	//상품 정보 수정
	@Transactional
	@Override
	public int goodsModify(BookVO vo) {

		int result = adminMapper.goodsModify(vo);

		if(result == 1 && vo.getImageList() !=null && vo.getImageList().size()>0) {

			adminMapper.deleteImageAll(vo.getBookId());

			vo.getImageList().forEach(attach ->{

				attach.setBookId(vo.getBookId());
				adminMapper.imageEnroll(attach);
			});

		}
		return result;
	}
	//상품 삭제
	@Override
	@Transactional
	public int goodsDelete(int bookId) {
		log.info("goodsDelete()......");

		adminMapper.deleteImageAll(bookId);

		return adminMapper.goodsDelete(bookId);
	}

	//지정 상품 이미지 정보 얻기
	@Override
	public List<AttachImageVO> getAttachInfo(int bookId){
		log.info("getAttachInfo..................");

		return adminMapper.getAttachInfo(bookId);
	}
	
	// 주문 상품 리스트 
	@Override
	public List<OrderDTO> getOrderList(Criteria cri) {
		return adminMapper.getOrderList(cri);
	}
	
	// 주문 총 갯수 
	@Override
	public int getOrderTotal(Criteria cri) {
		return adminMapper.getOrderTotal(cri);
	}

}
