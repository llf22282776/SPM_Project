<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>"教育部-IBM精品课程建设项目"软件项目管理课程</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${ctx}/css/spm.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${ctx}/js/jquery-1.3.2.min1.js"></script>
<script type="text/javascript" src="${ctx}/js/script.js"></script>
<script type="text/javascript">
//条件查询 
function query(){
   $('#dataList')[0].style.display="";
   $('#dg').datagrid({
   url:"${ctx}/scheduleCheck.do",
   queryParams:{
	   			spChapterId:$('#spChapterId').combobox('getValue') ,
	   			spChapterVideoId:$('#spChapterVideoId').combobox('getValue') ,
	   		    percent:$('#percent').val(),
	   		    studentId:$('#studentId').val()
	   		} //传参
	   		}); 	
} 
</script>




</head>
<body>
 <form id="ff" method="post">  

 <c:if test="${session.user.position=='2' }">
	 <table style="background:#efefef; border-collapse:collapse ;"   width="100%" height="80" cellspacing="5" cellpadding="5"> 
	 	<tr>
		 	<td width="15%">
		 	<select class="easyui-combobox" type="text" id="spChapterId"  panelHeight="100" style="width:150px;" >
	    			<option value="">所有章节</option>
	    			<s:iterator value="chapterList" status="status"  var="spChapter">             
						<option value="${spChapter[0]}">${spChapter[2]}</option>  
					</s:iterator>
	    	</select>
	    	</td> 
	    	<td width="15%">
		 	<select class="easyui-combobox" type="text" id="spChapterVideoId"  panelHeight="100" style="width:150px;" >
	    			<option value="">所有小节</option>
	    			<s:iterator value="videoList" status="status"  var="spChapterVideo">             
						<option value="${spChapterVideo.id}">${spChapterVideo.video_name}</option>
					</s:iterator>
	    	</select>
	    	</td>
		 	<td width="15%" align="right"> <label for="name">进度:</label> </td>
		 	<td width="15%"><input class="easyui-textbox" type="text" id="percent" /></td>  	
		 	<td width="15%" align="right"> <label for="name">学号:</label> </td>
		 	<td width="15%"><input class="easyui-textbox" type="text" id="studentId" /></td>  	
	 	</tr>
	 </table>

 	    <div style="text-align:center;padding:5px">
 	    
 	        <input type="button" class="btn btn-default" style="margin-right:20px;" onclick="query()" value="查  询" />
 	     
	    </div>
 </c:if>	    
</form>
 <div id="dataList" style="display:none;" > 
    <table id="dg"  class="easyui-datagrid"  width="100%" 
            pagination="true"
            rownumbers="true" fitColumns="true" singleSelect="false"
            data-options="fit:false,border:false,pageSize:10,pageList:[10,15,20]" >
        <thead>
            <tr >
            	<th data-options="field:'userId'"  width="15%">学号</th>
            	<th data-options="field:'name'"  width="15%">姓名</th>
                <th data-options="field:'chapterName'" width="15%">章节</th>
                <th data-options="field:'videoName'" width="5%">小节</th>                
                <th data-options="field:'percent'" width="20%">进度</th>
                
            </tr>
        </thead>
    </table>  

  </div>
</body>
</html>