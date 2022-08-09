package com.projectex.service;

import java.util.List;

import com.projectex.model.BookVO;
import com.projectex.model.CateFilterDTO;
import com.projectex.model.CateVO;
import com.projectex.model.Criteria;
import com.projectex.model.SelectDTO;

public interface BookService {

	/* 상품 검색 */
	public List<BookVO> getGoodsList(Criteria cri);

	/* 상품 총 갯수 */
	public int goodsGetTotal(Criteria cri);
	
	/* 국내 카테고리 리스트 */
	public List<CateVO> getCateCode1();
	
	/* 외국 카테고리 리스트 */
	public List<CateVO> getCateCode2();
	
	/* 검색결과 카테고리 필터 정보 */
	public List<CateFilterDTO> getCateInfoList(Criteria cri);
	
	/* 상품 정보 */
	public BookVO getGoodsInfo(int bookId);
	
	/* 상품 id 이름 */
	public BookVO getBookIdName(int bookId);
	
	/* 평점 순 상품 정보 */
	public List<SelectDTO> likeSelect();

}
