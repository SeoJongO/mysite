<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.javaex.vo.UserVo" %>
<%
	UserVo authUser = (UserVo)session.getAttribute("authUser");
	System.out.println(authUser);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>



	<div id="header" class="clearfix">
		<h1>
			<a href="/mysite/main">MySite</a>
		</h1>

		<c:if test="${authUser != null }">
			<ul>
				<li>${authUser.name}님 안녕하세요^^</li>
				<li><a href="/mysite/user?action=logout" class="btn_s">로그아웃</a></li>
				<li><a href="/mysite/user?action=modifyForm" class="btn_s">회원정보수정</a></li>
			</ul>
		</c:if>
		
		<c:if test="${authUser == null }">
			<ul>
				<li><a href="/mysite/user?action=loginForm" class="btn_s">로그인</a></li>
				<li><a href="" class="btn_s">회원가입</a></li>
			</ul>
		</c:if>

	</div>
	<!-- //header -->

	<div id="nav">
		<ul class="clearfix">
			<li><a href="">입사지원서</a></li>
			<li><a href="/mysite/board?action=board">게시판</a></li>
			<li><a href="/mysite/board?action=gallery">갤러리</a></li>
			<li><a href="/mysite/guest?action=addList">방명록</a></li>
		</ul>
	</div>
	<!-- //nav -->

</body>
</html>