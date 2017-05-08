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
      System.out.println(">>>>>>>>>>URI>>>>>>>>>:"+request.getRequestURI());
       if( (session.getAttribute("user") == null) && 
               (request.getRequestURI().endsWith("/loginAction.do") == false)  
               && 
               (request.getRequestURI().endsWith("/LogOut.do")==false ) 
               &&
               (request.getRequestURI().endsWith("/registerAction.do")==false ) 

               ){
           //不是登陆，登出，注册 且 没有user
           //看看是不是个游客，是个游客也别重定向了
           //妈的，搞毛线，在action里面做个毛线的权限控制
           if(session.getAttribute("common")!=null ){
               //有这个字段,分析分析有哪些能够重定向的，暂时先不做处理,也不冲定向
               chain.doFilter(req, res);
               return ;
           }
           
           
           System.out.println("正在重定向!!!");
           System.out.println((request.getRequestURI().endsWith("/loginAction.do")));
           System.out.println((request.getRequestURI().endsWith("/logOut.do")) );
           response.sendRedirect(rootUrlString);
           return;
           
       }else{ 
           //可能是 登陆登出 注册中的一个或者 user 不是null
           if(session.getAttribute("user") != null)session.removeAttribute("common");//移除游客
           
           //进行接下来处理
           chain.doFilter(req, res);
          
       }
        
        
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }

}
