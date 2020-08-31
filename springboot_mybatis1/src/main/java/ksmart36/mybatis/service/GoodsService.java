package ksmart36.mybatis.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ksmart36.mybatis.domain.Goods;
import ksmart36.mybatis.mapper.GoodsMapper;

@Service
public class GoodsService {
	@Autowired private GoodsMapper goodsMapper;
	
	public int addGoods(Goods goods) {
		return goodsMapper.addGoods(goods);
	};
	public Map<String,Object> goodsList(int currentPage) {
		//보여줄 행의 갯수
		final int ROW_PER_PAGE = 10;
		//보여줄 행의 시작점 초기화
		int startRow = 0;
		
		startRow = (currentPage -1) * ROW_PER_PAGE;
		
		Map<String,Object> prameterMap = new HashMap<String,Object>();
		prameterMap.put("startRow", startRow);
		prameterMap.put("rowPerPage", ROW_PER_PAGE);
		
		double totalRowCount = goodsMapper.getGoodsCount();
		
		int lastPage = (int)Math.ceil((totalRowCount / ROW_PER_PAGE));
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("goodsList", goodsMapper.goodsList(prameterMap));
		resultMap.put("lastPage", lastPage);
		
		return resultMap;
	};
	
	public Goods modifyGoods(String goodsCode) {
		return goodsMapper.modifyGoods(goodsCode);
	};
	public int updateGoods(Goods goods) {
		return goodsMapper.updateGoods(goods);
	};
	public int deleteGoods(String goodsCode) {
		return goodsMapper.deleteGoods(goodsCode);
	};
}
