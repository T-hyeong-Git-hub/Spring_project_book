package com.projectex.service;

import java.util.List;

import com.projectex.model.AttachImageVO;

public interface AttachService {

	/* 이미지 데이터 반환 */
	public List<AttachImageVO> getAttachList(int bookId);

}
