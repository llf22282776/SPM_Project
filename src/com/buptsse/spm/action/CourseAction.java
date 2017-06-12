package com.buptsse.spm.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.buptsse.spm.domain.Code;
import com.buptsse.spm.domain.Course;
import com.buptsse.spm.domain.User;
import com.buptsse.spm.filter.JspFitter;
import com.buptsse.spm.service.ICodeService;
import com.buptsse.spm.service.ISelectCourseService;
import com.mysql.jdbc.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.utility.StringUtil;


/**
 * @author BUPT-TC
 * @date 2015年11月17日 下午4:17
 * @description 有关课程处理的action
 * @modify BUPT-TC
 * @modifyDate 
 */
public class CourseAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(CourseAction.class);
	private User user;
	private Course course;
	protected String stdId="";
	protected String classId="";
	protected String name="";
	protected String status="";
	protected String syear="";
	protected String position="";
	private String operateType;	

	@Resource
	private ISelectCourseService selectCourseService;
	
	@Resource
	private ICodeService codeService;
	
	 /** 
	  * 分页查询所有课程列表
	 * @return
	 */
	public String listCourse(){
		
		
		int page=Integer.parseInt(ServletActionContext.getRequest().getParameter("page"));
		int rows=Integer.parseInt(ServletActionContext.getRequest().getParameter("rows"));
			
		Map paramMap = new HashMap();
		paramMap.put("studentId", stdId);
		paramMap.put("classId", classId);
		paramMap.put("name", name);
		paramMap.put("status", status);
		paramMap.put("syear", syear);
		System.out.println(stdId);
		List<Course> list = selectCourseService.findPage(paramMap,page, rows);
		
		for(Course course:list){
		    
			Code code =  codeService.findCodeName("status", course.getStatus());
			String codeName =code.getCodeName();
			course.setStatus(codeName);//2变成“已选课程”，类似这样
		}
		
		//查询总条数
		Long total = selectCourseService.count(paramMap);
		Long total1=-1L,total2=-1L;
		 
		if(position!=null && position.equals(JspFitter.POSITION_STUDENT)){
		    
		    paramMap.put("status", 1+"");//正在申请中,马丹，字符串搞成了数字，感觉爆炸
		    total1 = selectCourseService.count(paramMap);
		    paramMap.put("status", 3+"");//已被取消
		    total2 = selectCourseService.count(paramMap);
		  
		}
		System.out.println("total1:"+total1+" total2:"+total2);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("rows", list);
		map.put("total", total);
		if(total1!=-1L)map.put("total1", total1);
		if(total2!=-1L)map.put("total2", total2);//
		String str=JSONObject.toJSONString(map);
		try {
			ServletActionContext.getResponse().getWriter().write(str);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
    /**
     * 
     * 更新某个学生的成绩
     * 
     * 
     * */
	public String updateOneStudent(){
	    User user = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
	    if(user ==null || user.getPosition().equals(JspFitter.POSITION_TEACHER) == false){
	        try {
                ServletActionContext.getResponse().getWriter().write("您无权进行此操作");
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	        return null;
	    }
	        BigDecimal total=course.getDailyGrade().multiply(new BigDecimal(0.1))
                .add(course.getMidGrade().multiply(new BigDecimal(0.1)))
                .add(course.getPracticeGrade().multiply(new BigDecimal(0.2)))
                .add(course.getFinalGrade().multiply(new BigDecimal(0.6)));
            
            course.setTotalGrade(total.setScale(2,BigDecimal.ROUND_HALF_UP));
	    if(selectCourseService.updateCourse1(course)==true){
	        try {
                ServletActionContext.getResponse().getWriter().write("更新成功");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	    }else {
	        
	        try {
                ServletActionContext.getResponse().getWriter().write("更新失败");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
	        
	        
	    }
	    return null;
	}
	
	/**
	 * 更新课程信息，可批量更新
	 * @return
	 */
	public String updateCourse(){
		boolean result = false;
		String str = "";
		String[] studentIds  = ServletActionContext.getRequest().getParameterValues("studentIds[]");  
		for (int i = 0; i < studentIds.length; i++) { 
			if("U".equals(operateType)){
				//将未确认的确认
				result = selectCourseService.changeStatus(studentIds[i], 2);
				if(result){
					str = "确认成功！";
				}else{
					str = "确认失败，请联系管理员！";
				}
			}
			if("C".equals(operateType)){
				//将未确认或者已确认的取消
				result = selectCourseService.changeStatus(studentIds[i], 3);
				if(result){
					str = "取消成功！";
				}else{
					str = "取消失败，请联系管理员！";
				}
			}			
			
			if("D".equals(operateType)){
				//将已确认的删除
				result = selectCourseService.changeStatus(studentIds[i], 4);
				if(result){
					str = "删除成功！";
				}else{
					str = "删除失败，请联系管理员！";
				}
			}
			
		}

		String message = JSONObject.toJSONString(str);
		try {
			ServletActionContext.getResponse().getWriter().write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	
		return null;
	}	
	
	

	
	/**
	 * 增加选课信息
	 * @return
	 */
	public String insertCourse(){
		Map<String, Object> map=new HashMap<String, Object>();
		Course courseExit = selectCourseService.findCourse(course.getStudentId());
		if(courseExit!=null){
		    if(courseExit.getStatus().equals("2") ==false){
		        //不是选中的状态
		        course.setStatus("1");
	            boolean flag=false;
	            try{
	                flag = selectCourseService.saveOrUpdate(course);
	            }catch(Exception e){
	                e.printStackTrace();
	                flag=false;
	            }

	            if(flag){
	                
	                map.put("code", "1");
	                map.put("message", "已成功申请！");
	            }else{
	                map.put("code", "2");
	                map.put("message", "选课申请失败，请联系管理员！");
	            }           
		        
		    }else {
	            map.put("code", "2");
	            map.put("message", "学号为"+course.getStudentId()+"的学生已选课成功，请勿重复选课！");   
		    }
		}else{
			//初始化状态
			course.setStatus("1");
			boolean flag=false;
			try{
				flag = selectCourseService.insertCourse(course);
			}catch(Exception e){
				e.printStackTrace();
				flag=false;
			}

			if(flag){
			    
				map.put("code", "1");
				map.put("message", "选课成功！");
			}else{
				map.put("code", "2");
				map.put("message", "选课失败，请联系管理员！");
			}			
		}

		String str=JSONObject.toJSONString(map);
		System.out.println("str"+str);
		
		try {
			ServletActionContext.getResponse().getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return null;
	}	
	
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getStdId() {
		return stdId;
	}

	public void setStdId(String stdId) {
		this.stdId = stdId;
	}

	
	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getSyear() {
		return syear;
	}


	public void setSyear(String syear) {
		this.syear = syear;
	}

	public ICodeService getCodeService() {
		return codeService;
	}

	public void setCodeService(ICodeService codeService) {
		this.codeService = codeService;
	}


    public String getPosition() {
        return position;
    }


    public void setPosition(String position) {
        this.position = position;
    }
		
}
