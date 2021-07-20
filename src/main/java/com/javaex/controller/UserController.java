package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;

@WebServlet("/user")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.print("유저 컨트롤러: ");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		UserDao userDao = new UserDao();

		String action = request.getParameter("action");

		if ("joinForm".equals(action)) {
			
			System.out.println("[회원가입 폼]");
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
			
		} else if ("join".equals(action)) {
			
			System.out.println("[회원가입]");
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo(id, password, name, gender);
			System.out.println(userVo);
			
			int count = userDao.Insert(userVo);
			
			if(count>0) {
				System.out.println("[회원가입 완료]");
				WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
			} else {
				System.out.println("[회원가입 실패]");
			}
		} else if("loginForm".equals(action)) {
			System.out.println("[로그인 폼]");
			
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
		} else if("login".equals(action)) {
			System.out.println("[로그인]");
			
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			System.out.println(id+", "+password);
			
			UserVo userVo = userDao.getUser(id, password);
			System.out.println(userVo);
			
			if(userVo != null) {
				System.out.println("[로그인 성공]");
				session.setAttribute("authUser", userVo);
				WebUtil.redirect(request, response, "/mysite/main");
			} else {
				System.out.println("[로그인 실패]");
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
			}
			
			
		} else if("logout".equals(action)) {
			System.out.println("[로그아웃]");
			
			session.removeAttribute("authUser");
			session.invalidate();
			
			WebUtil.redirect(request, response, "/mysite/main");
			
		} else if("modifyForm".equals(action)) {
			System.out.println("[회원정보 수정 폼]");
			
			
			// 불러오기
			UserVo authUser = (UserVo) session.getAttribute("authUser");
			UserVo userVo = userDao.getUser(authUser.getId(), authUser.getPassword());
			
			request.setAttribute("userVo", userVo);
		
			WebUtil.forward(request, response, "/WEB-INF/views/user/modifyForm.jsp");
		} else if("modify".equals(action)) {
			System.out.println("[회원정보 수정]");
			
			int no = Integer.parseInt(request.getParameter("no"));
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			
			UserVo userVo = new UserVo(no, id, password, name, gender);
			
			int count = userDao.Update(userVo);
			
			if(count>0) {
				System.out.println("[수정 성공]");
				session.setAttribute("authUser", userVo);
			} else {
				System.out.println("[수정 실패]");
			}
			
			WebUtil.redirect(request, response, "/mysite/main");
			
		} 
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
