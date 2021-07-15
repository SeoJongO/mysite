package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestVo;

public class GuestDao {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "guestbook";
	private String pw = "guestbook";

	private void getConnection() {
		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public int guestInsert(GuestVo guestVo) {
		int count = -1;
		getConnection();

		try {

			String query = "";
			query += " insert into guestbook ";
			query += " values (seq_guest_no.nextval, ?, ?, ?, sysdate)";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, guestVo.getName());
			pstmt.setString(2, guestVo.getPassword());
			pstmt.setString(3, guestVo.getContent());

			count = pstmt.executeUpdate();


		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	public List<GuestVo> guestList() {
		
		List<GuestVo> addList = new ArrayList<GuestVo>();

		getConnection();

		try {

			String query = "";
			query += " select  no, ";
			query += "         name, ";
			query += "         password, ";
			query += "         content, ";
			query += "         reg_date ";
			query += " from guestbook ";
			query += " order by no asc ";

			
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int guestNo = rs.getInt("no");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String content = rs.getString("content");
				String reg_date = rs.getString("reg_date");

				GuestVo guestVo = new GuestVo(guestNo, name, password, content, reg_date);
				addList.add(guestVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return addList;

	}

	public int guestDelete(int guestNo, String guestPassword) {
		int count = -1;
		getConnection();

		try {
			String query = "";
			query += " delete from guestbook ";
			query += " where no = ? ";
			query += " and password = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, guestNo);
			pstmt.setString(2, guestPassword);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
	
	public GuestVo getGuest(int guestNo) {
		GuestVo guestVo = null;
		getConnection();
		try {
			String query = "";
			query += " select	no, ";
			query += " 			name, ";
			query += " 			password, ";
			query += " 			content, ";
			query += " 			reg_date ";
			query += " from guestbook ";
			query += " where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, guestNo);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int gNo = rs.getInt("no");
				String gName = rs.getString("Name");
				String gPassword = rs.getString("password");
				String gContent = rs.getString("content");
				String gDate = rs.getString("reg_date");

				guestVo = new GuestVo(gNo, gName, gPassword, gContent, gDate);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return guestVo;

	}
}
