package ksmart36.mybatis.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ksmart36.mybatis.domain.Member;
import ksmart36.mybatis.mapper.MemberMapper;
import ksmart36.mybatis.service.MemberService;

@Controller
public class MemberController {
	
	@Autowired private MemberService memberService;
	@Autowired private MemberMapper memberMapper;
	//@GetMapping("/") 아래와 동일한 방법
	@RequestMapping(value="/getMemberList",method = RequestMethod.GET)
	public String getMemberList(Model model) {
		
		List<Member> memberList = memberService.getMemberList();
		System.out.println(memberList);
		
		model.addAttribute("memberList", memberList);
		model.addAttribute("title", "회원목록조회");
		//model.addAttribute("memberList", memberService.getMemberList());
		return "member/memberList";
	}
	@GetMapping("/addMember")
	public String addMember(Model model) {
		
		model.addAttribute("title", "회원등록");
		return "member/addMember";
	}
	@PostMapping("/addMember")
	//내가 원하는 값을 던졌을 때 dto 에 없을 경우 받는 방법.
	public String addMember(Member member
							,@RequestParam(value="memberId",required = false) String memberId
							,@RequestParam(value="memberPw",required = false) String memberPw
							,@RequestParam(value="memberName",required = false) String memberName
							,@RequestParam(value="memberEmail",required = false) String memberEmail
							,@RequestParam(value="memberAddr",required = false) String memberAddr) {
		System.out.println("회원등록정보 -> " + member);
		System.out.println("아이디 : " + memberId);
		System.out.println("비밀번호 : " + memberPw);
		System.out.println("이름 : " + memberName);
		System.out.println("이메일 : " + memberEmail);
		System.out.println("주소 : " + memberAddr);
		memberService.addMember(member);
		return "redirect:/getMemberList";
	}
	@GetMapping("/modifyMember")
	public String modifyMember(@RequestParam(value="memberId",required = false) String memberId,Model model) {
		Member m = memberService.getMember(memberId);
		System.out.println(m.getMemberId());
		System.out.println(m.getMemberLevelName());
		model.addAttribute("member", m);
		model.addAttribute("title", "회원수정화면");
		return "member/updateMember";
	}
	@PostMapping("/modifyMember")
	public String modifyMember(Member member) {
		System.out.println("회원수정정보 -> " + member);
		memberService.modifyMember(member);
		return "redirect:/getMemberList";
	}
	@GetMapping("/deleteMember")
	public String deleteMember(Member member,Model model) {
		if(member.getMemberId() != null && !"".equals(member.getMemberId())) {
			model.addAttribute("member", member);
			model.addAttribute("title", "회원삭제화면");
		}
		return "member/deleteMember";
	}
	@PostMapping("/deleteMember")
	public String deleteMember(Member member) {
		if(member.getMemberId() != null && !"".equals(member.getMemberId())
			&& member.getMemberPw() != null && !"".equals(member.getMemberPw())	) {
			memberService.deleteMember(member.getMemberId(), member.getMemberPw());		
		}
		return "redirect:/getMemberList";
	}
	@RequestMapping(value="/searchMember",method = RequestMethod.GET)
	public String searchMember(Model model) {
		
		List<Member> memberList = memberService.getMemberList();
		System.out.println(memberList);
		
		model.addAttribute("memberList", memberList);
		model.addAttribute("title", "회원검색");
		//model.addAttribute("memberList", memberService.getMemberList());
		return "member/searchMember";
	}
	@PostMapping("/searchMember")
	public String searchMember(@RequestParam(value="col",required = false) String col,
							   @RequestParam(value="val",required = false) String val,Model model) {
		System.out.println(col);
		System.out.println(val);
		List<Member> memberList = memberService.searchMember(col, val);
		System.out.println(memberList);
		
		model.addAttribute("memberList", memberList);
		return "member/memberList";
	}
	@PostMapping("/memberLogin")
	public String memberLogin(Model model,
							@RequestParam(value="memberId",required= false) String memberId,
							@RequestParam(value="memberPw",required= false) String memberPw,
							HttpSession session) {
		
		Member member = memberService.getMember(memberId);
		if(member != null) {
			if(member.getMemberPw().equals(memberPw)) {
				session.setAttribute("SID", member.getMemberId());
				session.setAttribute("SNAME", member.getMemberName());
				session.setAttribute("SLEVEL", member.getMemberLevelName());
			}
		}
			model.addAttribute("title", "main 화면입니당");
			return "redirect:/main";
	}
	@GetMapping("logout")
	public String logout(HttpSession session,Model model) {
		session.invalidate();
		model.addAttribute("title", "main 화면입니당");
		return "redirect:/main";
	}
	@GetMapping("/getLoginHistory")
	public String getLoginHistory(Model model
								 ,@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
		
		Map<String, Object> resultMap 
				= memberService.getLoginHistory(currentPage);
		System.out.println("login 이력 ->" + resultMap.get("loginHistory").toString());
		
		model.addAttribute("title", "로그인이력조회");
		model.addAttribute("lastPage", resultMap.get("lastPage"));
		model.addAttribute("loginHistory", resultMap.get("loginHistory"));
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("startPageNum", resultMap.get("startPageNum"));
		model.addAttribute("lastPageNum", resultMap.get("lastPageNum"));
		return "login/loginHistory";
	}
}
