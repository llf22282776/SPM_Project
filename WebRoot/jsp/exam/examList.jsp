<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <script type="text/javascript">
  

 
	 function delExam(examName){
	 $.messager.confirm('确认','确认要删除吗？',function(r){
	 	
	 	if(r){
			$.getJSON("${ctx}/deleteExam.do",
				{"examName":examName},
				function(data){
					$.messager.alert("提示信息",data,'info',function(){
						window.location.reload(-1);
					});
					
				});		 	
	 	}
	 
	 });  
		
		
	 }
 
	 function uploadFile(){
		 	
		 	
		 	var fileName = $('#file').filebox('getValue');
		 	var prefix = fileName.substring(fileName.lastIndexOf(".")+1);
		 	if(prefix=="xlsx"||prefix=="xls"){
		 	
			 	$('#fileUpload').form('submit',{
					 url: "${ctx}/uploadExamFile.do", 	
			         success: function(result){
			          $.messager.alert("提示",result,"info",function(){
			        	   $('#fileUpload').form('clear');
					         window.location.reload(-1); 	  
			          });
			       
			        }	
			 	}); 	
		 	
		 	}else{
		 		$.messager.alert("提示信息","您上传的文件格式为："+prefix+"，请上传文件格式为xls或xlsx的文件");
		 	
		 	}
		 

		 
		 }
	 function uploadExam(){
		 	
		 	$('#dlg').dialog('open').dialog('setTitle','试卷上传');
		 
		 }
  </script>
  
  
  <body>

	<h1 style="font-size: 28px;color: #00a1f1;border-bottom: 1px solid #b6d9e8;line-height: 50px;word-break:break-all;">
	   网上测试
   </h1>  
   <c:if test="${session.user.position=='2' }">
		<form action="${ctx}/jsp/exam/generateExam.jsp" method="post">
			<input type="submit" class="btn btn-default" value="新建测试题">
		</form>	
	</c:if>
	<c:if test="${session.user.position=='2' }">
		<input type="button" class="btn btn-default" style="margin-right:20px;" onclick="uploadExam()" value="试卷上传" />
		<div  id="dlg" class="easyui-dialog"  style="padding:10px 20px;width: 700px" closed="true" buttons="#dlg-buttons" >   
		 	<form id="fileUpload" method="post" enctype="multipart/form-data">
			  <table style="border-collapse:collapse ;"  width="600px" height="50px" cellspacing="5" cellpadding="5"> 
			  	<tr>
			  		表头:试卷名称 题目内容  选项A  选项B 选项C 选项D 正确选项
			  	</tr>
			 	<tr>
				 	<td  width="150px" align="right"  ><label for="fileName" >文件选择:</label> </td>
				 	<td   width="200px" align="left" ><input class="easyui-filebox"  id="file" name="file" buttonText="选择文件"  accept=".xlsx,.xls" style="width:300px; height: 26px"> </td>
				 	<td width="100px"   align="left" ><a href="javascript:void(0)" class="easyui-linkbutton" onclick="uploadFile()" style="width: 100px">上  传</a> </td>
			 	</tr>
			  </table>
		 </form>  
		</div>
		<div id="dlg-buttons" align="center">
		      <a href="javascript:void(0)"   class="easyui-linkbutton"  iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')" style="width:90px">关 闭</a>
		</div>  
	</c:if>
	<h2>
		在线测试题列表
	</h2> 

		<s:iterator value="examList" status="status" var="exam">			
				<table border="0" width="60%">
					<tr>
						<td width="50%"><a href="${ctx}/enterExam.do?examName=${exam}">${exam}</a></td>
						<c:if test="${session.user.position=='2' }">
							<td><a href="javaScript:delExam('${exam}')">删除</a></td>
						</c:if>
					</tr>
				</table>
		</s:iterator>	

  </body>
</html>
