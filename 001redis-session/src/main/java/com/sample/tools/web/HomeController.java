package com.sample.tools.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomeController {
	

	@RequestMapping("/get.do")
	public Object sessionGet(Model mod, HttpServletRequest request, HttpServletResponse response) {

		HttpSession session = request.getSession();
		Object username = session.getAttribute("username");
		Object rolename = session.getAttribute("rolename");
		System.out.println(username + "-" + rolename);
		
		return "session/get";
	}
	
	@RequestMapping("/set.do")
	public Object sessionSet(String name, HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		session.setMaxInactiveInterval(60);
		
		session.setAttribute("username", name);
		session.setAttribute("rolename", "manager");

		return "session/set";
	}
	
}
