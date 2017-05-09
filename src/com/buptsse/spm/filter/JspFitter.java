package com.buptsse.spm.filter;

import java.io.IOException;

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

public class JspFitter implements Filter {

    public static final String POSITION_TEACHER="2";
    public static final String POSITION_STUDENT="3";
    public static final String POSITION_ADMIN="1";
    public static String[] limts_commonStrings={
        
        
        
    };//游客不能访问的jsp列表
    
    
    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        //checkSession((HttpServletRequest)req,(HttpServletResponse)res);
        // TODO Auto-generated method stub
        //过滤所有jsp页面，讲道理不应该直接访问jsp的
        System.out.println("=====>>>>>>>开始过滤jsp");
        HttpServletRequest request=(HttpServletRequest)(req);
        HttpServletResponse response=(HttpServletResponse)(res);
        HttpSession session=request.getSession();
        String rootUrlString="/SPM_Project";
        String reqUri= request.getRequestURI();
        System.out.println("jsp链接:"+reqUri);
        if(session.getAttribute("user") == null){
            if(session.getAttribute("common") == null){
                //没登录,什么页面都不能被访问,给一个错误的页面
                System.out.println("未登录处理");
                if(
                   reqUri.endsWith("/authError.jsp") ||
                   reqUri.endsWith("/register.jsp") ||
                   reqUri.endsWith("/SPM_Project/")  ||
                   reqUri.endsWith("/SPM_Project")   ||
                   reqUri.endsWith("/relogin.jsp") 
                        ){
                    //未登录能见的页面
                    System.out.println("放行:"+reqUri);
               
                    chain.doFilter(req, res);
                    
                }
                else{
                    
                    System.out.println("重定向至:"+rootUrlString+"/error/authError.jsp");
                    
                    if(!response.isCommitted())response.sendRedirect(rootUrlString+"/error/authError.jsp");
                    return;
                } 

            }else {
                //游客登陆
                //游客哪些不能访问？？
                System.out.println("游客身份处理");
                System.out.println(reqUri.endsWith("/courseSelect.jsp"));
                System.out.println(reqUri.endsWith("/scoreQueryList.jsp") );
                System.out.println(reqUri.endsWith("/mainMessage.jsp")  );
                if(
                        reqUri.endsWith("/courseSelect.jsp") ||
                        reqUri.endsWith("/scoreQueryList.jsp")  ||
                        reqUri.endsWith("/mainMessage.jsp") 
                             ){
                         //游客不能访问的
                         System.out.println("不能放行:"+reqUri);
                         if(!response.isCommitted())response.sendRedirect(rootUrlString+"/error/authError.jsp");
                         return;
                         
                }else {
                    //能访问的就在这里
                    chain.doFilter(req, res);  
                    return;
                }
               
            }
            
            
        }else{
            System.out.println("已登录身份处理");
            User thisUser=(User) session.getAttribute("user");
            System.out.println("已有用户的重定向");
            System.out.println(request.getRequestURI().endsWith("/SPM_Project") );
            System.out.println(request.getRequestURI().endsWith("/SPM_Project/") );
            System.out.println(request.getRequestURI().endsWith("/jsp/register.jsp") );
            if(
                    request.getRequestURI().endsWith("/SPM_Project")  || 
                    request.getRequestURI().endsWith("/SPM_Project/") ||
                    request.getRequestURI().endsWith(rootUrlString+"/jsp/register.jsp")
                    //想进入到登陆页面，那是不允许的
                    ){
                if(!response.isCommitted())response.sendRedirect(rootUrlString+"/jsp/mainFrame.jsp");
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
            else if(thisUser.getPosition().equals(JspFitter.POSITION_STUDENT)){
                //管理员
                chain.doFilter(req, res); 
                return;
            }else{
                chain.doFilter(req, res);
                return;
            }
           
            
        }
        
        
      
    }
    /** 校验SESSION是否有效，判断session里面是否有user */
    private void checkSession(HttpServletRequest request,
            HttpServletResponse response) {
        // 如果Session失效，跳回登录页面
        HttpSession session =  request.getSession();
        try {
            if (session == null) {
                    System.out.println("*****校验到session失效*****");
                    if(!response.isCommitted())response.sendRedirect("/SPM_Project");
                    return;
            }else{
                User user = (User)session.getAttribute("user");
                if(user==null){
                    System.out.println("*****校验到用户未登录*****");
                    if(!response.isCommitted())response.sendRedirect("/SPM_Project/jsp/relogin.jsp");
                    return;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }   
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
