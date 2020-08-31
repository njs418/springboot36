package ksmart36.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart36.mybatis.domain.Goods;

@Mapper
public interface GoodsMapper {
	
	//회원 상품 전체 행의 갯수
	public int getGoodsCount();

	public int addGoods(Goods goods);
	
	public List<Goods> goodsList(Map<String,Object> prameterMap);
	
	public Goods modifyGoods(String goodsCode);
	
	public int updateGoods(Goods goods);
	
	public int deleteGoods(String goodsCode);
}
