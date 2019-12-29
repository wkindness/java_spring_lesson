package com.example.demo.app.inquiry;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
	
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm, Model model, @ModelAttribute("complete") String complete) {
		model.addAttribute("title", "お問い合わせページ");
		return "inquiry/form";
	}
	@PostMapping("/form")
	public String formBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "お問い合わせページ");
		return "inquiry/form";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm, BindingResult result, Model model) {

		// エラーあり
		if(result.hasErrors()) {
			model.addAttribute("title", "お問い合わせページ");
			return "inquiry/form";
		}
		model.addAttribute("title", "確認ページ");
		return "inquiry/confirm";
	}
	
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm, 
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		// エラーあり
		if(result.hasErrors()) {
			model.addAttribute("title", "お問い合わせページ");
			return "inquiry/form";
		}
		//TODO：DB登録
		redirectAttributes.addFlashAttribute("complete", "お問い合わせありがとうございます。");
		return "redirect:/inquiry/form";

	}
	
}