package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
	
	private final InquiryService inquiryService;
	
	@Autowired
	public InquiryController(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}
	
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();

//		// 例外エラー確認のための存在しないデータ指定
//		Inquiry inquiry = new Inquiry();
//		inquiry.setId(100);
//		inquiry.setName("例外テスト");
//		inquiry.setEmail("exception@example.com");
//		inquiry.setContents("ERROR");
//		
//		inquiryService.update(inquiry);
//		// 個別に例外処理するケース
//		try {
//			inquiryService.update(inquiry);
//		} catch (InquiryNotFoundException e) {
//			model.addAttribute("message", e);
//			return "error/CustomPage";
//		}
		
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "お問い合わせ一覧");
		return "inquiry/index_boot";
	}
	
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm, Model model, @ModelAttribute("complete") String complete) {
		model.addAttribute("title", "お問い合わせページ");
		return "inquiry/form_boot";
	}
	@PostMapping("/form")
	public String formBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "お問い合わせページ");
		return "inquiry/form_boot";
	}

	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm, BindingResult result, Model model) {

		// エラーあり
		if(result.hasErrors()) {
			model.addAttribute("title", "お問い合わせページ");
			return "inquiry/form_boot";
		}
		model.addAttribute("title", "確認ページ");
		return "inquiry/confirm_boot";
	}
	
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm, 
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		
		// エラーあり
		if(result.hasErrors()) {
			model.addAttribute("title", "お問い合わせページ");
			return "inquiry/form_boot";
		}

		// 詰め替え処理
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		// DB登録
		inquiryService.save(inquiry);
		
		redirectAttributes.addFlashAttribute("complete", "お問い合わせありがとうございます。");
		return "redirect:/inquiry/form";

	}
	
//	// コントローラー内で共通に例外処理するケース
//    @ExceptionHandler(InquiryNotFoundException.class)
//    public String handleException(InquiryNotFoundException e, Model model) {
//    	model.addAttribute("message", e);
//    	return "error/CustomPage";
//    }

}