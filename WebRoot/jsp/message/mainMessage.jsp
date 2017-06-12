<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<html>
	<head>
		<title>"教育部-IBM精品课程建设项目"软件项目管理课程</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/sweetalert2.min.css" />
		<link>



		<script type="text/javascript" src="${ctx}/dwr/util.js"></script>
		<script type="text/javascript" src="${ctx}/dwr/engine.js"></script>
		<script type="text/javascript" src="${ctx}/dwr/interface/dwrUtil.js"></script>
		<script type="text/javascript" src="${ctx}/js/sweetalert2.min.js"></script>
		
	</head>
	

<script type="text/javascript" language="javascript">   

function iFrameHeight() {   
	var ifm= document.getElementById("iframepage");   
	var subWeb = document.frames ? document.frames["iframepage"].document : ifm.contentDocument;   
	if(ifm != null && subWeb != null) {
	   ifm.height = subWeb.body.scrollHeight;
	   ifm.width = subWeb.body.scrollWidth;
	}   
}


function submitMessage(){
	var s1= document.getElementById("title").value; 
	var s2= document.getElementById("name").value; 
	var s3= document.getElementById("content").value; 
	dwrUtil.checkMessageBlank(s1,s2,s3,callBackFunc);
	
	
	function callBackFunc(result){
		if(result == "true" || result == true){
			$('#fm').form('submit',{
			    url:'${ctx}/saveMessage.do',
			    onSubmit: function(){
			        return $(this).form('validate');
			    },
			    success: function(result){
			    
			    
			    
			    	if(result==1){
			    		swal("提示","发送成功","success");
				    	$('#iframepage')[0].src="${ctx}/findMessageList.do";	    	
				    	$('#fm').form('clear');	    	
			    	}else{
			    		swal("提示","发送失败","warning");
			    	}

			    }
			});
		}else {
			swal("提示","内容不可全为空","warning");
			
		}
		
		
	}
	
	

}



</script>
	<body>
  	<iframe id="iframepage" width="100%" scrolling="no" marginheight="0" marginwidth="0" onLoad="iFrameHeight()" frameborder="0" src="${ctx}/findMessageList.do"    height="" scrolling="no"  > 

	</iframe> 
	

		<c:if test="${session.user.position != '1'}">
			<div class="article">
			<h1>
				我要留言
			</h1>
			<form id="fm">
				<table>
					<tr>
						<td>
							&nbsp;&nbsp;姓名
						</td>
						<td>
								<input id="name" class="easyui-textbox" type="text" name="message.name"   required="true"  style="height: 30px; width: 200px; margin-left: 20px;" />								
						</td>
						<td>
							&nbsp;&nbsp;标题
						</td>
						<td>
							<input id="title" class="easyui-textbox" type="text" name="message.title" required="true"  style="height: 30px; width: 200px; margin-left: 20px"/>	
						
						</td>
					</tr>
				</table>
				<textarea id="content" class="textarea easyui-validatebox" required="true"   name="message.detail" style="width: 100%; height: 100px; margin: 10px; margin-top: 20px;font-size: 15px;"></textarea>
				<div align="center"> 
				<input type="button" onclick="submitMessage()" class="btn btn-default" value="提交留言"></input>
				</div>
			</form>
		</div>	
		
		</c:if>

	</body>
</html>