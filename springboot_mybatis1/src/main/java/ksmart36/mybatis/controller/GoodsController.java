package ksmart36.mybatis.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ksmart36.mybatis.domain.Goods;
import ksmart36.mybatis.service.GoodsService;

@Controller
public class GoodsController {
	@Autowired private GoodsService goodsService;

	@GetMapping("/addGoods")
	public String addGoods(Model model) {
		model.addAttribute("title", "상품등록화면");
		return "goods/addGoods";
	}
	@PostMapping("/addGoods")
	public String addGoods(Goods goods,HttpSession session) {
		goods.setGoodsSellerId((String)session.getAttribute("SID"));
		goodsService.addGoods(goods);
		return "redirect:/main";
	}
	@GetMapping("/goodsList")
	public String goodsList(Model model,@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {
		
		Map<String,Object> goodsList = goodsService.goodsList(currentPage);

		model.addAttribute("title", "상품리스트");
		model.addAttribute("goodsList", goodsList.get("goodsList"));
		model.addAttribute("lastPage", goodsList.get("lastPage"));
		model.addAttribute("currentPage", currentPage);
		System.out.println(goodsList);
		return "goods/goodsList";
	}
	@GetMapping("/modifyGoods")
	public String modifyGoods(String goodsCode,Model model) {
		Goods goods = goodsService.modifyGoods(goodsCode);
		model.addAttribute("title", "상품수정화면");
		model.addAttribute("goods", goods);
		
		return "goods/updateGoods";
	}
	@PostMapping("/modifyGoods")
	public String modifyGoods(Goods goods) {
		goodsService.updateGoods(goods);
		return "redirect:/goodsList";
	}
	@GetMapping("/deleteGoods")
	public String deleteGoods(@RequestParam(value="goodsCode",required = false) String goodsCode) {
		goodsService.deleteGoods(goodsCode);
		return "redirect:/goodsList";
	}
}
