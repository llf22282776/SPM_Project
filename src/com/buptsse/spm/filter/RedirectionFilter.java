package com.buptsse.spm.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.buptsse.spm.domain.User;
import com.buptsse.spm.util.DwrUtil;
import com.opensymphony.xwork2.ActionContext;

public class RedirectionFilter implements Filter{
    public static Map<String, String> map=new HashMap<String, String>(){{
        put("loginAction","");
        put("logOut","");
        put("registerAction","");
        put("insertStudents","");
        put("indexAction","");
        put("listUser","");
        put("deleteUser","");
        put("insertUser","");
        put("courseQuery","");
        put("courseUpdate","");
        put("insertCourse","");
        put("updateOneStudent","");
        put("emailNotify","");
        put("uploadScoreFile","");
        put("findMessageList","");
        put("saveMessage","");
        put("deleteMessage","");
        put("listExam","");
        put("addQuestion","");
        put("queryQuestion","");
        put("enterExam","");
        put("checkAnswer","");
        
        put("deleteExam","");
        put("deleteQuestion","");
        put("findTradeInfoList","");
        put("findTradeInfo","");
        put("enterIndex","");
        put("listDownLoad","");
        
        put("addDownload","");
        put("deleteDownload","");
        put("enterintro","");
        put("editBasicInfo","");
        put("updateBasicInfo","");
        
        put("listTeachingPlan","");
        put("deleteTeaching","");
        put("addTeaching","");
        put("listSpChapter","");
        put("videoShow","");
        put("getSpchapterVideo","");
        put("pauseSchedule","");
        put("scheduleCheck","");
        put("listConfigInfo","");
        put("editConfigInfo","");
        
    }};
    private Pattern doRegex = Pattern.compile("/\\\\*.*?\\\\.do");
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
       //这个过滤器应该检查，session里面有没有user属性，有的话，到下边去，没有的话重定向到login
      HttpServletRequest request=(HttpServletRequest)(req);
      HttpServletResponse response=(HttpServletResponse)(res);
      HttpSession session=request.getSession();
      String rootUrlString="/SPM_Project";
      System.out.println("action过滤:"+request.getRequestURI());
      String requestURI=request.getRequestURI();
      if(session.getAttribute("user") == null){
          if(session.getAttribute("common") == null){
              //没登录
              //看看是不是登陆，登出，注册，是这三个中的一个，也不重定向了
              System.out.println("没登录");
              if( 
               (request.getRequestURI().endsWith("/loginAction.do") )  
               || 
               (request.getRequestURI().endsWith("/LogOut.do") ) 
               ||
               (request.getRequestURI().endsWith("/registerAction.do"))
               ){
                  chain.doFilter(req, res);  //可以登陆登出和注册
                  return;
              }
              else   {
                  
                  if(!response.isCommitted())response.sendRedirect(rootUrlString+"/error/authError.jsp");//}
                  return;
              }
              

          }else {
              //游客登陆
              System.out.println("游客登陆");
              System.out.println(requestURI.endsWith("/listDownLoad.do"));
              System.out.println(requestURI.endsWith("/listExam.do"));
              
              if(
                      requestURI.endsWith("/listDownload.do") ||
                      requestURI.endsWith("/listExam.do") ||
                      requestURI.endsWith("/videoShow") ||
                      requestURI.endsWith("/listSpChapter") ||
                      requestURI.endsWith("/videoShow") ||
                      requestURI.endsWith("/pauseSchedule") 
                      
                      ){
                  //游客不能访问的
                  System.out.println("不能放行:"+requestURI);
                  response.sendRedirect(rootUrlString+"/error/authError.jsp");
                  return;
                  
              }
              else {
                  chain.doFilter(req, res);
                  return;
              }  
          }
          
          
      }else{
          //已经有了用户
          System.out.println("已登录");
          session.removeAttribute("common");//移除游客}
          if(requestURI.endsWith("/loginAction.do")){
              //发起login，需要冲定位到主页面
              response.sendRedirect(rootUrlString+"/jsp/mainFrame.jsp");
              return;
          }
          
          
          User thisUser=(User) session.getAttribute("user");
        
          String oString=requestURI.substring(requestURI.lastIndexOf("/")+1);
          String dString=oString.substring(0, oString.indexOf("."));
          System.out.println("该action是:"+dString);
          if(!map.containsKey(dString)){
              //不包含该action
              System.out.println(oString+"非法！");
              response.sendRedirect(rootUrlString+"/error/authError.jsp");
              return;
              
              
          }
          if(thisUser.getPosition().equals(JspFitter.POSITION_STUDENT)){
              //学生身份
              
              chain.doFilter(req, res);  
              return;
          }else if(thisUser.getPosition().equals(JspFitter.POSITION_TEACHER)){
              //老师
              chain.doFilter(req, res);  
              return;
          }
          else if(thisUser.getPosition().equals(JspFitter.POSITION_ADMIN)){
              //管理员
              if(requestURI.endsWith("listSpChapter.do")){
                  //管理员不能访问这个action
                  System.out.println("正在过滤listSpChapter.do");
                  response.sendRedirect(rootUrlString+"/error/authError.jsp");
                  return;
                  
              }
              
              chain.doFilter(req, res);  
              return;
          }else{
              chain.doFilter(req, res);
              return;
          }
          //没有落尽来，那就走吧
       
      }
      

        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }

}
