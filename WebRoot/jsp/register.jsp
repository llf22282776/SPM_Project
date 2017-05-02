<%@ page language="java" import="java.util.*" contentType="text/html;  charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%--   
//----------------------------------------------------------------
 //Project: SPM System (Client SubSystem) 
// JSP Name  : register.jsp 
// PURPOSE : 登入与注册界面的处理
// HISTORY：
//	Create：
//	Modify：Xue yifei    2015.10.27
//  Copyright  : BUPTSSE   
//----------------------------------------------------------------- 
--%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>"教育部-IBM精品课程建设项目”软件项目管理课程</title>
		<!-- 		插入外部样式表，使用DwrUtil -->
		<script type="text/javascript" src="./dwr/util.js"></script>
		<script type="text/javascript" src="./dwr/engine.js"></script>
		<script type="text/javascript" src="./dwr/interface/dwrUtil.js"></script>
		<script type="text/javascript" src="./js/jquery.js"></script>
		<script type="text/javascript" src="./js/bootstrap.js"></script>
		<script type="text/javascript">
		function register(){
/*			var registerUserName = document.getElementById("user");
			var registerPassWord = document.getElementById("passwd");
			var registerPassWord1 = document.getElementById("passwd2");
			dwrUtil.registerCheck(registerUserName.value,registerPassWord.value,registerPassWord1.value,callback);
			function callback(result){
				if(result == "success"){
					//$.messager.alert("提示信息","注册成功！");
					$("#msg").html("注册成功！");
				}else{
					$("#msg").html("注册失败，请重新注册！");
					//$.messager.alert("注册失败，请重新注册！");
				}
			}*/
			
            $('#registFm').form('submit',{
                url: "${ctx}/registerAction.do",
                success: function(result){
					$("#msgRegist").html(result);
					$('#registFm').form('clear');
                }
            });			
			
			
		}
		
		
		function login(){
			var loginUserName = document.getElementById("u");
			var loginPassWord = document.getElementById("p");
// 			alert("开始调用dwr进行登录校验");
			/* 下面一行实际上是没有意义的 */
			dwrUtil.loginCheck(loginUserName.value,loginPassWord.value,callback);
			function callback(result){
				if(result == "1"){
					//alert("登入成功！");
					document.getElementById("loginForm").submit();
				}else{
					//alert("登入s！");
					$("#msg").html("用户名或密码错误，请重新输入！");
				}
			}
		}
		
		function loginExistenceCheck(){
			var loginUserName = document.getElementById("u");
			dwrUtil.extenceCheck(loginUserName.value,callback);
			function callback(result){
				if(result == "extence"){
					document.getElementById('UserNameMsg').style.display="none";
				}else{
					var msg = document.getElementById("labelUserNameMsg");
					if(result == "unExtence"){
						msg.innerHTML = "用户名不存在";
					}else {
						msg.innerHTML = result;
					}
					document.getElementById('UserNameMsg').style.display="block";
				}
			}
		}
		
		function registerExtenceCheck(){
			var loginUserName = document.getElementById("user");
			dwrUtil.extenceCheck(loginUserName.value,callback);
			function callback(result){
				if(result == "unExtence"){
					document.getElementById('RegisterNameMsg').style.display="none";
				}else{
					var msg = document.getElementById("labelUserNameMsg1");
					if(result == "extence"){
						msg.innerHTML = "用户名已存在";
					}else {
						msg.innerHTML = result;
					}
					document.getElementById('RegisterNameMsg').style.display="block";
				}
			}
		}
		
		function registerPassWordCheck(){
			//密码没有做混合检查,修改如下
			var password = document.getElementById("passwd").value;//获得密码
			
			var passwordMsg = document.getElementById("PasswordMsg");//是个Div
			var labElement=passwordMsg.getElementsByTagName("label")[0];//获得
			dwrUtil.registerPwdCheck(password,callbackFuc);//检查密码
			function callbackFuc(result){
				passwordMsg.style.display="block";//显示出来，然后开始修改文字
				if(result == "ok"){
					//什么都不做,检查下密码强度吧
					var lv=strengthTest(password);
					switch(lv){
						case 1:labElement.textContent="密码强度:弱";
						case 2:labElement.textContent="密码强度:中";
						case 3:labElement.textContent="密码强度:强";
					
					}
				}else {
					labElement.textContent=result;//直接显示出来
				}
			}
		}
		function strengthTest(val){
			 	
			    var lv = 0;
			    var patt1=new RegExp("[0-9]+");
			    var patt2=new RegExp("[A-Z]+");
			    var patt3=new RegExp("[a-z]+");
			    if(patt1.test(val) == true) lv++;
			    if(patt2.test(val) == true) lv++;
			    if(patt3.test(val) == true) lv++;
			    
				return lv;
				
			
		}
		function registerPassWordCheck2(){
			var password1 = document.getElementById("passwd").value;
			var password2 = document.getElementById("passwd2").value;
			var passwordMsg1 = document.getElementById("PasswordMsg1");
			if(password1 != password2){
				passwordMsg1.style.display="block";
			}else{
				passwordMsg1.style.display="none";
			}
		}
		/*
		检查邮箱格式是否正确或邮箱是否存在
		
		**/
		function checkEmail(){
			var email=document.getElementById("email").value;
			
			
			dwrUtil.checkEmail(email,callBackFunc);
			function callBackFunc(result){
				//如果满足要求，就要显示按钮
				var labelEle= document.getElementById("emailCheck");//获取元素
				var divEle=document.getElementById("emailDiv");
				var sendButton=document.getElementById("sendMailButton");//
				if(result == "0"){
					//div 显示，label显示格式错误
					labelEle.textContent="错误:邮箱格式错误";
				}else if(result == "1"){
					//div显示 label显示已经使用
					labelEle.textContent="错误:邮箱已经被使用";
					
				}else if(result == "2"){
					//显示发送邮件按钮
					sendButton.style.display="block";
					
					
				}
				
				
			}
			
			
		}
		function buttonClick(){
			//点击sendMail，定时任务触发，就在这个函数里面写一个就好
			
			
		}
		function sendEmail(){
			//发送邮件到账户，服务器记录验证码,按钮倒计时解锁,字体变成再发送,输入验证码可用
			
		}
		function checkVaildText(){
			//验证验证码是否正确，改变小图标，变成xxxxx
			
			
		}
			
		</script>
		<link href="./css/login2.css" rel="stylesheet" type="text/css" />
		<link href="./css/bootstrap.css" rel="stylesheet" type="text/css" />
	</head>
	<body>
		<h1>
			北邮爱课堂系统
			<sup>
				V2015
			</sup>
		</h1>
<!-- background: #fff url(../images/1.jpg) 100% 0 no-repeat;-->
		
		<div class="login" style="margin-top: 50px;">

			<div class="header">
				<div class="switch" id="switch">
					<a class="switch_btn_focus" id="switch_qlogin"
						href="javascript:void(0);" tabindex="7">快速登录</a>
					<a class="switch_btn" id="switch_login" href="javascript:void(0);"
						tabindex="8">快速注册</a>
					<div class="switch_bottom" id="switch_bottom"
						style="position: absolute; width: 64px; left: 0px;"></div>
				</div>
			</div>
			<div class="web_qr_login" id="web_qr_login"
				style="display: block; height: 235px;">
				<form id="loginForm" name="loginForm" action="${pageContext.request.contextPath}/loginAction.do" method="post" >
					<!--登录-->
					<div class="web_login">
						<div class="login-box">
							<div class="login_form">
								<input type="hidden" name="to" value="log" />
								<div class="uinArea" id="uinArea">
									<label class="input-tips" for="u">
										帐号：
									</label>
									<div class="inputOuter" id="uArea">
										<input type="text" id="u" name="user.userName" onBlur="loginExistenceCheck();" 
											class="easyui-textbox" data-options="iconCls:'icon-man'" style="width:200px;height:38px;" />
									</div>
								</div>
								
								<div class="UserNameMsg" id="UserNameMsg" style="display:none;color:#F00">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<label id="labelUserNameMsg" class="text" for="m">
										用户名应为10位！
									</label>
								</div>
								
								<div class="pwdArea" id="pwdArea">
									<label class="input-tips" for="p">
										密码：
									</label>
									<div class="inputOuter" id="pArea">
										<input type="password" id="p" name="user.password"
											class="easyui-textbox" data-options="iconCls:'icon-lock'" style="width:200px;height:38px;"  />
									</div>
								</div>

								<div id="msg" align="center" style="color: red;"></div>
								
								<table border="0" align="left">

									<tr>
										<td>
											<div style="padding-left: 0px; margin-top: 20px;">
												<input type="button" onclick="login()" value="立即登录" style="width: 120px;"
													class="button_blue" />	
											</div>
											
										</td>
										<td>
											<div style="padding-left: 20px; margin-top: 20px;">
												<input type="submit" formaction="${ctx}/jsp/mainFrame.jsp" value="游客登陆"
													style="width: 120px;" class="button_blue" />
											</div>
										</td>
									</tr>
								</table>
							</div>

						</div>

					</div>
					<!--登录end-->
				</form>
			</div>

			<!--注册-->
			<div class="qlogin" id="qlogin" style="display: none;">
				<form id="registFm" action="" method="post">
					<div class="web_login">
					
						<ul class="reg_form" id="reg-ul">
						
							<li>
								<label class="input-tips2">
									用户名：
								</label>
								<div class="inputOuter2">
									<input type="text" id="user" maxlength="10" name="user.userName" onBlur="registerExtenceCheck();" 
										class="inputstyle2" />
								</div>

								<br>
								<br>
							</li>

							<div class="RegisterName" id="RegisterNameMsg" style="display:none;color:#F00">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<label id="labelUserNameMsg1" class="text" for="m">
									用户名应为10位！
								</label>
							</div>

							<li>
								<label class="input-tips2">
									密码：
								</label>
								<div class="inputOuter2">
									<input type="password" id="passwd" name="user.password" onBlur="registerPassWordCheck();" 
										maxlength="16" class="inputstyle2" />
								</div>
								<br>
								<br>
							</li>
							
							<div class="PasswordMsg" id="PasswordMsg" style="display:none;color:#F00">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<label class="text" for="pm">
									密码必须为6位以上，16位以下！
								</label>
							</div>
							
							<li>
								<label class="input-tips2">
									确认密码：
								</label>
								<div class="inputOuter2">
								<input type="password" id="passwd2" name="user.password1" onBlur="registerPassWordCheck2();"
										maxlength="16" class="inputstyle2" />
								</div>

								<br>
								<br>
							</li>
							
							<div class="PasswordMsg" id="PasswordMsg1" style="display:none;color:#F00">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<label class="text" for="pm" id="twiceCheck">
									两次输入密码不一致，请重新输入！
								</label>
							</div>
							<li>
								<label class="input-tips2">
									邮箱：
								</label>
								<div class="inputOuter2" style="display:block">
								<input type="text" id="email" name="user.email" onchange="checkEmail();"
										maxlength="40" class="inputstyle2" />
								<Button id="sendMailButton" style="display:none" onclick="">发送邮件</Button>
								</div>

								<br>
								<br>
							</li>
								<div class="RegisterName" id="emailDiv" style="display:none;color:#F00">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<label class="text" for="pm" id="emailCheck">
								</label>
							</div>
							<li>
								<label class="input-tips2">
									验证码：
								</label>
								<div class="inputOuter2" style="display:block">
								<input disabled="false" type="text" id="vaildText" onchange=""
										maxlength="16" class="inputstyle2" />
								<span></span>
								</div>
								
								<br>
								<br>
							</li>
							<div class="RegisterName" id="vaildTextDiv" style="display:none;color:#F00">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								<span class=""></span>
							</div>
							<div id="msgRegist" align="center" style="color: red;"></div>
							<li>
								<div class="inputArea">
<!-- 								<input type="submit" -->
<!-- 									style="margin-top: 10px; margin-left: 85px;" -->
<!-- 									class="button_blue" value="同意协议并注册" onclick="register()" /> -->
<!-- 								</div> -->
									<input type="button"
									style="margin-top: 10px; margin-left: 85px;"
									class="button_blue" value="同意协议并注册" onclick="register();" />
								</div>
							</li>
						</ul>

					</div>
				</form>
			</div>
			<!--注册end-->
		

		
		
		</div>
		<div class="jianyi">
			版权所有&nbsp;&nbsp;北京邮电大学&nbsp;&nbsp;地址：北京市西土城路10号&nbsp;&nbsp;邮编：100876
		</div>
		
	</body>
</html>