package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.UserVo;

public class UserDao {

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

		public int Insert(UserVo userVo) {
			int count = 0;
			getConnection();
			try {

				String query = "";
				query += " insert into users ";
				query += " values (seq_user_no.nextval, ?, ?, ?, ?) ";

				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, userVo.getId());
				pstmt.setString(2, userVo.getPassword());
				pstmt.setString(3, userVo.getName());
				pstmt.setString(4, userVo.getGender());

				count = pstmt.executeUpdate();

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
			close();
			return count;
		}

		public List<UserVo> getUserList() {
			return getUserList("");
		}

		public List<UserVo> getUserList(String keword) {
			List<UserVo> userList = new ArrayList<UserVo>();

			getConnection();

			try {

				String query = "";
				query += " select  no, ";
				query += "         id, ";
				query += "         password, ";
				query += "         name, ";
				query += "         gender ";
				query += " from users ";
				query += " order by no asc ";

				if (keword != "" || keword == null) {
					query += " where name like ? ";
					query += " or id like  ? ";
					query += " or password like ? ";
					pstmt = conn.prepareStatement(query);

					pstmt.setString(1, '%' + keword + '%');
					pstmt.setString(2, '%' + keword + '%');
					pstmt.setString(3, '%' + keword + '%');
				} else {
					pstmt = conn.prepareStatement(query);
				}

				rs = pstmt.executeQuery();

				while (rs.next()) {
					int no = rs.getInt("no");
					String id = rs.getString("id");
					String password = rs.getString("password");
					String name = rs.getString("name");
					String gender = rs.getString("gender");

					UserVo userVo = new UserVo(no, id, password, name, gender);
					userList.add(userVo);
				}

			} catch (SQLException e) {
				System.out.println("error:" + e);
			}

			close();

			return userList;

		}

		public int Update(UserVo userVo) {
			int count = 0;
			getConnection();

			try {

				String query = "";
				query += " update users ";
				query += " set password = ? , ";
				query += "     name = ?,  ";
				query += "     gender = ?  ";
				query += " where id = ? ";

				pstmt = conn.prepareStatement(query);
				
				pstmt.setString(1, userVo.getPassword());
				pstmt.setString(2, userVo.getName());
				pstmt.setString(3, userVo.getGender());
				pstmt.setString(4, userVo.getId());

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

		public UserVo getUser(String id, String password) {
			UserVo userVo = null;
			getConnection();
			try {

				String query = "";
				query += " select no, ";
				query += " 		  id, ";
				query += " 		  password, ";
				query += " 		  name, ";
				query += " 		  gender ";
				query += " from users ";
				query += " where id = ? ";
				query += " and	 password = ? ";
				pstmt = conn.prepareStatement(query);

				pstmt.setString(1, id);
				pstmt.setString(2, password);

				rs = pstmt.executeQuery();

				// 4.결과처리
				while (rs.next()) {
					int no = rs.getInt("no");
					String uid = rs.getString("id");
					String upassword = rs.getString("password");
					String name = rs.getString("name");
					String gender = rs.getString("gender");

					userVo = new UserVo(no, uid, upassword, name, gender);
				}
			} catch (SQLException e) {
				System.out.println("error:" + e);
			}
			close();
			return userVo;

		}

	}
