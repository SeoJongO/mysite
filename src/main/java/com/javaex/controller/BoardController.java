package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.print("게시판 컨트롤러: ");
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		BoardDao boardDao = new BoardDao();

		String action = request.getParameter("action");
		
		if("board".equals(action)) {
			System.out.println("[게시판]");
			
			List<BoardVo> boardList;
			String keyword = request.getParameter("keyword");
			
			if(keyword != null) {
				System.out.println("not null");
				boardList = boardDao.List(keyword);
			} else {
				System.out.println("is null");
				boardList = boardDao.List();
			}
			
			request.setAttribute("bList", boardList);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/board.jsp");
			
		} else if("writeForm".equals(action)) {
			System.out.println("[글쓰기폼]");
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		
		}else if("write".equals(action)) {
			System.out.println("[글쓰기]");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = ((UserVo)session.getAttribute("authUser")).getNo();
			
			BoardVo boardVo = new BoardVo(title, content, userNo);
			
			boardDao.write(boardVo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=board");
		
		} else if("delete".equals(action)) {
			System.out.println("[글삭제}");
			
			int userNo = Integer.parseInt(request.getParameter("no"));
			
			boardDao.delete(userNo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=board");
			
			
		} else if("read".equals(action)) {
			System.out.println("[글읽기]");
			
			int userNo = Integer.parseInt(request.getParameter("no"));
			boardDao.hit(userNo);
			
			BoardVo boardVo = boardDao.read(userNo);
			
			request.setAttribute("read", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
			
		} else if("modifyForm".equals(action)) {
			System.out.println("[글수정폼]");
			
			int userNo = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = boardDao.read(userNo);
			
			request.setAttribute("modify", boardVo);
			
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
		} else if("modify".equals(action)) {
			System.out.println("[수정]");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int bNo = Integer.parseInt(request.getParameter("no"));
			
			boardDao.modify(title, content, bNo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=board");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
