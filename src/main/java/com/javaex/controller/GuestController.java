package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestVo;

@WebServlet("/guest")
public class GuestController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		request.setCharacterEncoding("UTF-8");
		GuestDao guestDao = new GuestDao();
		String action = request.getParameter("action");
		System.out.println(action);
		System.out.print("방명록 컨트롤러: ");
		
		// 리스트
		if("addList".equals(action)) {
			System.out.println("[방명록 등록 폼]");
			
			List<GuestVo> guestList = guestDao.guestList();
			
			request.setAttribute("gList", guestList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
		
		// 추가
		} else if("add".equals(action)) {
			System.out.println("[방명록 추가]");
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			GuestVo guestVo = new GuestVo(name, password, content);
			
			int count = guestDao.guestInsert(guestVo);
			
			if(count>0) {
				System.out.println("등록 성공");
			} else {
				System.out.println("등록 실패");
			}
			
			WebUtil.redirect(request, response, "/mysite/guest?action=addList");
			
		// 삭제폼	
		} else if("deleteForm".equals(action)) {
			System.out.println("[방명록 삭제 폼]");
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
			
		// 삭제	
		} else if("delete".equals(action)) {
			System.out.println("[방명록 삭제]");
			
			int guestNo = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");
		
			int count = guestDao.guestDelete(guestNo, password);
			
			if(count>0) {
				System.out.println("삭제 성공");
			} else {
				System.out.println("삭제 실패");
			}
			
			WebUtil.redirect(request, response, "/mysite/guest?action=addList");
		}
		
		
	
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
