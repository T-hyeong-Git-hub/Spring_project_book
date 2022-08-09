package com.projectex.service;

import java.util.List;

import com.projectex.model.OrderCancelDTO;
import com.projectex.model.OrderDTO;
import com.projectex.model.OrderPageItemDTO;

public interface OrderService {
	
	/* 주문 정보 */
	public List<OrderPageItemDTO> getGoodsInfo(List<OrderPageItemDTO> orders);
	
	/* 주문 */
	public void  order(OrderDTO orw);
	
	/* 주문 취소 */
	public void orderCancle(OrderCancelDTO dto);
}
