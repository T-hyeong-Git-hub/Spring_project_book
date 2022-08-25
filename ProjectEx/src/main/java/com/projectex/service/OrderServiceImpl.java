package com.projectex.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectex.mapper.AttachMapper;
import com.projectex.mapper.BookMapper;
import com.projectex.mapper.CartMapper;
import com.projectex.mapper.MemberMapper;
import com.projectex.mapper.OrderMapper;
import com.projectex.model.AttachImageVO;
import com.projectex.model.BookVO;
import com.projectex.model.CartDTO;
import com.projectex.model.MemberVO;
import com.projectex.model.OrderCancelDTO;
import com.projectex.model.OrderDTO;
import com.projectex.model.OrderItemDTO;
import com.projectex.model.OrderPageItemDTO;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired 
	private AttachMapper attachMapper;
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private BookMapper bookMapper;
	
	@Override
	public List<OrderPageItemDTO> getGoodsInfo(List<OrderPageItemDTO> orders){
		
		List<OrderPageItemDTO> result = new ArrayList<OrderPageItemDTO>();
		
		for(OrderPageItemDTO ord : orders) {
			
			OrderPageItemDTO goodsInfo = orderMapper.getGoodsInfo(ord.getBookId());
			
			goodsInfo.setBookCount(ord.getBookCount());
			
			goodsInfo.initSaleTotal();
			
			List<AttachImageVO> imageList = attachMapper.getAttachList(goodsInfo.getBookId());
			
			goodsInfo.setImageList(imageList);
			
			result.add(goodsInfo);	
		}
		
		return result;
	}

	@Override
	@Transactional 
	public void order(OrderDTO orw) {
		
		// 사용할 데이터 가져오기
			//회원 정보
			MemberVO member = memberMapper.getMemberInfo(orw.getMemberId());
			
			//주문 정보
			List<OrderItemDTO> ords = new ArrayList<>();
			for(OrderItemDTO oit : orw.getOrders()) {
				OrderItemDTO orderItem = orderMapper.getOrderInfo(oit.getBookId());
				//수량 셋팅
				orderItem.setBookCount(oit.getBookCount());
				//기본정보 셋팅
				orderItem.initSaleTotal();
				//List객체 추가
				ords.add(orderItem);
			}
			
			//OrderDTO 셋팅
			orw.setOrders(ords);
			orw.getOrderPriceInfo();
			
			//DB 주문,주문상품(,배송정보) 넣기
			
			// orderId만들기 및 OrderDTO객체 orderId에 저장
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("_yyyyMMddmm");
			String orderId = member.getMemberId() + format.format(date);
			orw.setOrderId(orderId);
			
			//DB 넣기
			orderMapper.enrollOrder(orw);	//gu_order 등록
			for(OrderItemDTO oit : orw.getOrders()) {		//gu_orderItem 등록
				oit.setOrderId(orderId);
				orderMapper.enrollOrderItem(oit);				
			}
			
			//비용 포인트 변동 적용
			
			//비용 차감 & 변동 돈(money) Member객체 적용
			int calMoney = member.getMoney();
			calMoney  -= orw.getOrderFinalSalePrice();
			member.setMoney(calMoney);
			
			//포인트 차감, 포인트 증가 & 변동 포인트(point) Member객체 적용
			int calPoint = member.getPoint();
			calPoint = calPoint - orw.getUsePoint() + orw.getOrderSavePoint();  // 기존 포인트 - 사용 포인트 + 획득 포인트
			member.setPoint(calPoint);
			
			//변동 된 돈, 포인트 DB 적용
			orderMapper.deductMoney(member);
			
			//재고 변동 적용
			for(OrderItemDTO oit : orw.getOrders()) {
				// 변동 재고 값 구하기 
				BookVO book = bookMapper.getGoodsInfo(oit.getBookId());
				book.setBookStock(book.getBookStock() - oit.getBookCount());
				// 변동 값 DB 적용 
				orderMapper.deductStock(book);
			}
			
			//장바구니 제거
			for(OrderItemDTO oit : orw.getOrders()) {
				CartDTO dto = new CartDTO();
				dto.setMemberId(orw.getMemberId());
				dto.setBookId(oit.getBookId());
				
				cartMapper.deleteOrderCart(dto);
			}
			
	}
	
	// 주문취소 
	@Override
	@Transactional
	public void orderCancle(OrderCancelDTO dto) {
		// 주문, 주문상품 객체
		//회원
			MemberVO member = memberMapper.getMemberInfo(dto.getMemberId());
		//주문상품
			List<OrderItemDTO> ords = orderMapper.getOrderItemInfo(dto.getOrderId());
			for(OrderItemDTO ord : ords) {
				ord.initSaleTotal();
			}
		//주문 
			OrderDTO orw = orderMapper.getOrder(dto.getOrderId());
			orw.setOrders(ords);
			
			orw.getOrderPriceInfo();
			
		// 주문상품 취소 DB
			orderMapper.orderCancle(dto.getOrderId());
			
			// 돈, 포인트, 재고 변환
			// 돈 
			int calMoney = member.getMoney();
			calMoney += orw.getOrderFinalSalePrice();
			member.setMoney(calMoney);
			
			// 포인트
			int calPoint = member.getPoint();
			calPoint = calPoint + orw.getUsePoint() - orw.getOrderSavePoint();
			member.setPoint(calPoint);	
			
			//DB적용
			orderMapper.deductMoney(member);
			
			// 재고 
			for(OrderItemDTO ord : orw.getOrders()) {
				BookVO book = bookMapper.getGoodsInfo(ord.getBookId());
				book.setBookStock(book.getBookStock() + ord.getBookCount());
				orderMapper.deductStock(book);
			}
		
	}
}
