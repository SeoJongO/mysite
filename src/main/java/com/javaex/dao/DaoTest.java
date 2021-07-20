package com.javaex.dao;

import com.javaex.vo.BoardVo;

public class DaoTest {

	public static void main(String[] args) {

//		BoardVo boardVo = new BoardVo("실험용 제목","실험용 내용",4);
		BoardDao boardDao = new BoardDao();
		
//		boardDao.write(boardVo);
		
//		System.out.println(boardDao.List());
		System.out.println(boardDao.read(5));
	}

}
