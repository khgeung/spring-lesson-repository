package org.kosa.myproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.kosa.myproject.domain.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MyTestController {
	@GetMapping("/hello") // client가 get 방식으로 요청시 찾는 url pattern 지정
	public String hello(Model model) { // Model : 클라이언트에게 응답할 정보를 공유하기 위한 객체
		// view html 에 정보 공유 : request.setAttribute("info", "떡볶이");
		model.addAttribute("info", "떡볶이");
		// forward 방식 : ViewResolver 에 의해 src/main/resources/templates/result1.html
		// 로 제어가 이동되어 타임리프 템플릿 엔진에 의해 파싱되어 클라이언트에게 HTML로 응답한다
		return "result1";
	}

	@GetMapping("/paramTest") // HandlerMapping url 등록해서 찾아줌
	public String paramTest(String customerId, int customerAge, Model model) { // HandlerAdapter 가 호출
		String type = null;
		if (customerAge > 18) {
			type = "성인";

		} else {
			type = "미성년";
		}
		model.addAttribute("type", type);
		System.out.println("paramTest:" + customerId);
		return "result2";// ViewResolver
	}
	/**
	 * client 에서 post 방식으로 요청하여 @PostMapping 으로 처리
	 * client 의 form 에서 동일한 name (menu) 으로 여러 개의 value 를 전송한다
	 * 이 경우 Servlet/JSP 에서는 request.getParameterValues() : String [] 으로 요청을 처리
	 * 이것을 HandlerAdapter 가 요청 데이터에 대한 배열 객체를 생성해서 우리의 컨트롤러 메소드에 전달해준다
	 */
	@PostMapping("/paramTest2")
	public String paramTest2(String [] menu) {
		System.out.println("메뉴 수:"+menu.length);
		//redirect 방식은 클라이언트에게 url을 지정해서 응답받게 함
		return "redirect:/result3-view"; //Spring 환경에서 redirect 는 FrontController 인
		//DispatcherServlet 으로 가게 되어 있고 적절한 컨트롤러가 있을 때 응답할 수 있다
	}
	@GetMapping("/result3-view")
	public String parmTest2Result() {
		return "result3";
	}
	
	@PostMapping("/paramTest3")
	public String paramTest3(String menu [], Model model) {
		System.out.println("menu array:"+menu);
		model.addAttribute("myMenu",menu);
		return "result4";
	}
	@GetMapping("/customer-list")
	public String showCustomerList(Model model) {
		List<Customer> list = new ArrayList<>();
		list.add(new Customer("java","손흥민","런던"));
		list.add(new Customer("spring","아이유","종로"));
		list.add(new Customer("thymeleaf","이강인","파리"));
		model.addAttribute("customerList",list);
		return "result5";
	}
	
	@PostMapping("/has-a-test")
	public String hasATest(Customer customer, Model model) {//HandlerAdapter 가 만들어서 준다
		//System.out.println(customer);
		model.addAttribute("customer", customer);
		return "result6";
	}
	//스프링 컨트롤러의 메소드 매개변수로 세션을 받는 경우
	//request.getSession() 과 동일 : 세션이 없으면 새로 생성, 있으면 기존 세션 리턴
	//즉 세션은 존재하나, 로그인 정보를 가지고 있지 않을 수도 있다는 것을 잊지말기
	@GetMapping("sessionTest0")
	public String sessionTest0(HttpSession session) {
		return "result7";
	}
	
	//스프링 컨트롤러의 메소드 매개변수로 세션을 받는 경우
	//request.getSession() 과 동일 : 세션이 없으면 새로 생성, 있으면 기존 세션 리턴
	@GetMapping("/sessionTest1")
	public String sessionTest1(HttpSession session) {//HandlerAdapter 가 만들어서 준다
		session.setAttribute("customer", new Customer("java", "손흥민", "런던"));
		return "result7";
	}
	@GetMapping("/sessionTest2")
	public String sessionTest2(HttpServletRequest request, Model model) {
		//request.getSession(false) : 기존 세션이 없으면 null 반환, 기존 세션이 있으면 기존 세션 반환
		HttpSession session = request.getSession(false);
		String checkMessage = null;
		if(session == null) {
			checkMessage="세션이 존재하지 않습니다";
		}else if(session != null && session.getAttribute("customer") == null) {
			checkMessage ="인증 정보가 존재하지 않습니다";
		}else if(session != null && session.getAttribute("customer") != null) {
			checkMessage="로그인 상태입니다";
		}
		model.addAttribute("checkMessage",checkMessage);
		return "result7-2";
	}
	
}






