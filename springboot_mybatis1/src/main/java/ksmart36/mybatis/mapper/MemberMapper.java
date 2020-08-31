package ksmart36.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import ksmart36.mybatis.domain.Member;

@Mapper
public interface MemberMapper {

	//회원 목록 조회
	public List<Member> getMemberList();
	
	//회원 정보 등록
	public int addMember(Member member);
	
	//id로 회원정보 조회
	public Member getMember(String memberId);
	
	//회원정보 수정
	public int modifyMember(Member member);
	
	//회원정보 삭제
	public int deleteMember(String memberId);
	//로그인테이블 삭제
	public int removeLogin(String memberId);
	//상품코드 조회
	public List<Map<String,Object>> getGoodsCodeById(String memberId);
	//주문테이블 삭제
	public int removeOrder(List<Map<String,Object>> goodsCodeList);
	//상품테이블 삭제
	public int removeGoods(String memberId);
	//회원목록조건검색
	public List<Member> searchMember(String col,String val);
	
	//회원 로그인이력 전체 행의 갯수
	public int getLoginCount();
	
	//회원 로그인이력 조회
	public List<Map<String, Object>> getLoginHistory(Map<String, Object> parameterMap);
}
