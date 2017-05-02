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
       if( (session.getAttribute("user") == null) && (request.getRequestURI().endsWith("/loginAction.do") == false)  && (request.getRequestURI().endsWith("/LogOut.do")==false ) ){
           //没有登录
           System.out.println("正在重定向!!!");
           System.out.println((request.getRequestURI().endsWith("/loginAction.do")));
           System.out.println((request.getRequestURI().endsWith("/LogOut.do")) );
           response.sendRedirect(rootUrlString+"/loginAction.do");
           return;
           
       }else{
           chain.doFilter(req, res);
          
       }
        
        
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub
        
    }

}
