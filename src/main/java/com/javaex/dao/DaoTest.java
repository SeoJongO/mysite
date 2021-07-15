package com.javaex.dao;

import com.javaex.vo.UserVo;

public class DaoTest {

	public static void main(String[] args) {

//		UserVo userVo = new UserVo ("aaa", "bbb", "ccc", "male");
//		
		UserDao userDao = new UserDao();
//		userDao.Insert(userVo);
		
//		System.out.println(userDao.getUserList());
		
		UserVo userVo = userDao.getUser("1234", "1234");
		System.out.println(userVo);
		
			
	}

}
