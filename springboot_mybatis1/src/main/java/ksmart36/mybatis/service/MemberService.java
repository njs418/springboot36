package ksmart36.mybatis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ksmart36.mybatis.domain.Member;
import ksmart36.mybatis.mapper.MemberMapper;

@Service
@Transactional
public class MemberService {

	@Autowired private MemberMapper memberMapper; //서비스 매퍼 컨피그레이션 컴퍼넌트 등 어노텐션이 붙은 것은 Autowired 를 사용 할 수 있다.
	//회원정보
	public List<Member> getMemberList() {
		
		//List 객체가 null이 아닌 경우 = 조회 성공
		List<Member> mlist = memberMapper.getMemberList();
		if(mlist != null) {
			for(int i=0; i<mlist.size();i++) {
				Member m = mlist.get(i);
				//변수(회원레벨) 초기화
				int level = 0;
				level = m.getMemberLevel();
				if(level > 0) {					
					if(level == 1) {
						m.setMemberLevelName("관리자");
					}else if(level == 2) {
						m.setMemberLevelName("판매자");
					}else {
						m.setMemberLevelName("구매자");
					}
				}
			}
		}
		return mlist;
	}
	public int addMember(Member member) {
		int result = memberMapper.addMember(member);
		
		return result;
	};
	public Member getMember(String memberId) {
		Member member = memberMapper.getMember(memberId);
		
		//레벨에 따라 memberLevelName 값을 setter
		if(member != null) {
			int memberLevel = 0;
			memberLevel = member.getMemberLevel();
			if(memberLevel > 0) {
				if(memberLevel == 1) {
					member.setMemberLevelName("관리자");	
				}else if(memberLevel == 2) {
					member.setMemberLevelName("판매자");
				}else if(memberLevel == 3){
					member.setMemberLevelName("구매자");
				}else {
					member.setMemberLevelName("일반회원");
				}
			}
		}
		return member;
	}
	//회원정보수정
	public int modifyMember(Member member) {
		return memberMapper.modifyMember(member);
	};
	
	//회원정보삭제
	public int deleteMember(String memberId,String memberPw) {
		//delete 성공 결과 변수 초기화
		int result = 0;
		
		Member member = memberMapper.getMember(memberId);
		//모든 조건이 일치하는 경우 삭제메서드를 호출한다.
			if(member != null && memberPw != null && !"".equals(memberPw)) {
				if(memberPw.equals(member.getMemberPw())) {
					//1. 로그인 테이블 삭제
					result += memberMapper.removeLogin(memberId);
					//2-1. 상품테이블 통해 g_code 조회
					List<Map<String,Object>> goodsCodeList = memberMapper.getGoodsCodeById(memberId);
					//2-2. 주문 테이블 삭제
					result += memberMapper.removeOrder(goodsCodeList);
					//3. 상품테이블 삭제
					result += memberMapper.removeGoods(memberId);
					//4. 멤버테이블 삭제
					result = memberMapper.deleteMember(memberId);
				}
			}

		return result;
	}
	
	public List<Member> searchMember(String col,String val) {
		
		String valLevel = null;
		List<Member> lm = null;
		if("m_level".equals(col)) {
			if("관리자".equals(val)) {
				valLevel = "1";
				lm = memberMapper.searchMember(col, valLevel);
			}else if("판매자".equals(val)) {
				valLevel = "2";
				lm = memberMapper.searchMember(col, valLevel);
			}else if("구매자".equals(val)) {
				valLevel = "3";
				lm = memberMapper.searchMember(col, valLevel);
			}
		}else {
			lm = memberMapper.searchMember(col, val);
		}
		
		
		
		if(lm != null) {
			for(int i=0; i<lm.size();i++) {
				Member m = lm.get(i);
				//변수(회원레벨) 초기화
				int level = 0;
				level = m.getMemberLevel();
				if(level > 0) {					
					if(level == 1) {
						m.setMemberLevelName("관리자");
					}else if(level == 2) {
						m.setMemberLevelName("판매자");
					}else {
						m.setMemberLevelName("구매자");
					}
				}
			}
		}
		return lm;
	};
	//회원로그인이력조회
	public Map<String, Object> getLoginHistory(int currentPage){
		
		//보여줄 행의 갯수
		final int ROW_PER_PAGE = 10;
		
		//보여줄 행의 시작점 초기화
		int startRow = 0;
		
		//시작페이지번호,끝페이지번호
		int lastPageNum = ROW_PER_PAGE;
		int startPageNum = 1;
		
		//6번째 가운데 위치
		if(currentPage > (ROW_PER_PAGE/2)) {
			startPageNum = currentPage - ((lastPageNum/2)-1);
			lastPageNum += (startPageNum-1);
		}
		
		// 페이징 알고리즘
		startRow = (currentPage - 1) * ROW_PER_PAGE; 
		
		Map<String, Object> parameterMap = new HashMap<String, Object>();
		parameterMap.put("startRow", startRow);
		parameterMap.put("rowPerPage", ROW_PER_PAGE);
		
		double totalRowCount = memberMapper.getLoginCount();
		
		int lastPage = (int) Math.ceil((totalRowCount / ROW_PER_PAGE));
		
		List<Map<String, Object>> loginHistory 
					= memberMapper.getLoginHistory(parameterMap);
		
		if(currentPage >= (lastPage-4)) {
			lastPageNum = lastPage;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("loginHistory", loginHistory);
		resultMap.put("lastPage", lastPage);
		resultMap.put("startPageNum", startPageNum);
		resultMap.put("lastPageNum", lastPageNum);
		
		return resultMap;
	}
		
}

