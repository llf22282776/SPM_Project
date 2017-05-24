<%@ page language="java" contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>失败提示信息</title>
</head>
<body>
	<div align="center"
		style="font-size: 20px; margin-top: 20px; margin-right: 20px; margin-left: 20px; margin-bottom: 50px;">
		<c:choose>
			<c:when test="${session.user == null }">
				<!-- 判断是Null的时候，再检查 是不是游客-->
				<c:choose>
					<c:when test="${session.common eq null}">
						您还没有登陆，请先<div style="color: #2200aa" id="login" onclick="justLogin();">登陆</div>
					</c:when>
					<c:otherwise>
						您的身份是游客,无权查看此功能,请先<div style="color: #2200aa" id="justLogout();">登陆</div>
					</c:otherwise>
				</c:choose>
			</c:when>
			<c:otherwise>
				<c:choose>
					<c:when test="${session.user.position eq '2'}">
						您是的身份是 教师 ，无权进行此项操作,请登出切换账号
					</c:when>
					<c:when test="${session.user.position eq '1'}">
						您是的身份是 管理员 ，无权进行此项操作,请登出切换账号
					</c:when>
					<c:when test="${session.user.position eq '3'}">
						您是的身份是 学生，无权进行此项操作,请登出切换账号
					</c:when>
					<c:otherwise>
						您是的身份是 未知 ，无权进行此项操作,请登出切换账号
					</c:otherwise>
				</c:choose>

			</c:otherwise>
		</c:choose>
	</div>
	<script type="text/javascript">
	 function justLogin () {
		//如果有父窗口，让父窗口进行这个工作
		if (parent != null && parent != undefined)
			parent.window.location.href = "${pageContext.request.contextPath}/login.do";
		else
			location.href = "${ctx}/login.do";

	};
	 function justLogout() {
		//如果有父窗口，让父窗口进行这个工作
		if (parent != null && parent != undefined)
			parent.window.location.href = "${pageContext.request.contextPath}/logOut.do";
		else
			location.href = "${ctx}/logOut.do";

	};
</script>
</body>

</html>
