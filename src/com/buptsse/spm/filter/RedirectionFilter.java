package com.buptsse.spm.filter;

import java.io.IOException;
import java.util.Map;

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
import com.opensymphony.xwork2.ActionContext;

public class RedirectionFilter implements Filter{

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
                  
                  if(!response.isCommitted())response.sendRedirect(rootUrlString);//}
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
      
/*      
    //      
    //       if( (session.getAttribute("user") == null) && 
    //               (request.getRequestURI().endsWith("/loginAction.do") == false)  
    //               && 
    //               (request.getRequestURI().endsWith("/LogOut.do")==false ) 
    //               &&
    //               (request.getRequestURI().endsWith("/registerAction.do")==false ) 
    //
    //               ){
    //           //不是登陆，登出，注册 且 没有user
    //           //看看是不是个游客，是个游客也别重定向了
    //           //妈的，搞毛线，在action里面做个毛线的权限控制
    //           if(session.getAttribute("common")!=null ){
    //               //有这个字段,分析分析有哪些能够重定向的，暂时先不做处理,也不冲定向
    //               chain.doFilter(req, res);
    //               return ;
    //           }
    //           
    //           
    //           System.out.println("正在重定向!!!");
    //           System.out.println((request.getRequestURI().endsWith("/loginAction.do")));
    //           System.out.println((request.getRequestURI().endsWith("/logOut.do")) );
    //           response.sendRedirect(rootUrlString);
    //           return;
    //           
    //       }else{ 
    //           //可能是 登陆登出 注册中的一个或者 user 不是null
    //           if(session.getAttribute("user") != null){
    //               session.removeAttribute("common");//移除游客}
    //               //然后如果是
    //               if(request.getRequestURI().endsWith("/loginAction.do")){
    //                   //又想登陆了
    //                   response.sendRedirect(rootUrlString+"/jsp/mainFrame.jsp");
    //                   return;
    //               }else if(request.getRequestURI().endsWith("/SPM_Project") || request.getRequestURI().endsWith("/SPM_Project/")){
    //                   response.sendRedirect(rootUrlString+"/jsp/mainFrame.jsp");
    //                   return;
    //                   
    //                   
    //               }
    //           }
    //           
    //           //进行接下来处理
    //           chain.doFilter(req, res);
    //          
    //       }
        
 */  
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }

}
