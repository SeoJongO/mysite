package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

public class BoardDao {

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";

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
	
	public int write(BoardVo boardVo) {
		int count = 0;
		getConnection();
		try {

			String query = "";
			query += " insert into board ";
			query += " values (seq_board_no.nextval, ?, ?, 0, sysdate, ?) ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getUser_no());

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	public List<BoardVo> List() {
		return List("");
	}

	public List<BoardVo> List(String keword) {
		List<BoardVo> boardList = new ArrayList<BoardVo>();

		getConnection();

		try {

			String query = "";
			query += " select  b.no, ";
			query += "         b.title, ";
			query += "         u.name, ";
			query += "         b.hit, ";
			query += "         b.reg_date, ";
			query += "         b.user_no ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			

			if (keword != "" || keword == null) {
				query += " and title like ? ";
				query += " order by b.no desc ";
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, '%'+keword+'%');
				
			} else {
				query += " order by b.no desc ";
				pstmt = conn.prepareStatement(query);
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				int no = rs.getInt("no");
				String title = rs.getString("title");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String date = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");

				BoardVo boardVo = new BoardVo(no, title, name, hit, date, userNo);
				boardList.add(boardVo);
			}

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();

		return boardList;

	}
	
	public int delete(int no) {
		int count = -1;
		getConnection();

		try {
			String query = "";
			query += " delete from board ";
			query += " where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
	
	public BoardVo read(int no) {
		BoardVo boardVo = null;
		getConnection();
		try {

			String query = "";
			query += " select b.no, ";
			query += " 	      u.name, ";
			query += " 		  b.hit, ";
			query += " 		  b.reg_date, ";
			query += " 		  b.title, ";
			query += " 		  b.content, ";
			query += " 		  b.user_no ";
			query += " from board b, users u ";
			query += " where b.user_no = u.no ";
			query += " and b.no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			rs = pstmt.executeQuery();

			// 4.결과처리
			while (rs.next()) {
				int bno = rs.getInt("no");
				String title = rs.getString("title");
				String content = rs.getString("content");
				String name = rs.getString("name");
				int hit = rs.getInt("hit");
				String date = rs.getString("reg_date");
				int userNo = rs.getInt("user_no");
				
				boardVo = new BoardVo(bno, title, content, name, hit, date, userNo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		close();
		return boardVo;

	}
	
	public int modify(String title, String content, int no) {
		int count = -1;
		getConnection();

		try {

			String query = "";
			query += " update board ";
			query += " set title = ? , ";
			query += "     content = ?  ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);
			
			pstmt.setString(1, title);
			pstmt.setString(2, content);
			pstmt.setInt(3, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}

	public int Delete(int no) {
		int count = 0;
		getConnection();

		try {
			
			String query = "";
			query += " delete from users ";
			query += " where no = ? ";
			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
	
	public int hit(int no) {
		int count = -1;
		getConnection();

		try {

			String query = "";
			query += " update board ";
			query += " set hit = hit+1 ";
			query += " where no = ? ";

			pstmt = conn.prepareStatement(query);
			
			pstmt.setInt(1, no);

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return count;
	}
	
}
