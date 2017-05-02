package com.buptsse.spm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import com.buptsse.spm.service.IUserService;

/**
 * 
 * @author BUPT-TC 
 * @date 2015年11月6日 下午10:17:41
 * @description 
 * @modify BUPT-TC 
 * @modifyDate
 */
public class DwrUtil {

	@Resource
	private IUserService userService;
	private String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
	private Pattern emailRegex = Pattern.compile(check);  
	/**
	 * 
	 * @param userName 用户名
	 * @return boolean 判定用户名是否为数字，是返回true，否则false
	 */
	
	public boolean isNumeric(String userName){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(userName);
		if(isNum.matches()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param userName 用户名
	 * @param passwWord 密码
	 * @return String 状态消息，但是并没有什么用！
	 */
	public String loginCheck(String userName,String passwWord){
		//需要修改
		System.out.println("此处写用户名密码校验的方法，通过返回1，失败返回失败信息");
		System.out.println("userName: " + userName + ", passWord: " + passwWord);
		
		if (StringUtils.isBlank(userName) || StringUtils.isBlank(passwWord)){
			//ServletActionContext.getRequest().setAttribute("loginMsg", "账号或密码未输入！");
			return "账号或密码未输入！";
		}
		try
		{
			if(userService.findUser(userName,passwWord) == null){
				//ServletActionContext.getRequest().setAttribute("loginMsg", "对不起，该用户不存在或密码输入错误！");
				return "对不起，该用户不存在或密码输入错误！";
			}else{
				//ServletActionContext.getRequest().setAttribute("loginMsg", "登入成功！");
				return "1";
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return "1";
	}
	
	/**
	 * 
	 * @param userName 用户名
	 * @return String 检测用户名输入是否有效：10位数字，返回相应字符串
	 */
	public String extenceCheck(String userName){
		System.out.println("开始检验用户名是否存在");
		try{
			if(StringUtils.isBlank(userName) ){
				System.out.println("用户名不可为空，应为10位");
				return "用户名不可为空";
			}else if(!isNumeric(userName) || userName.length() != 10){
				System.out.println("用户名应为10位学号");
				return "用户名应为10位数字";
			}else{
				if(userService.findUser(userName) != null){
					System.out.println("用户已存在，请重新输入");
					return "extence";
				}else{
					System.out.println("用户不存在");
					return "unExtence";
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "success";
	}
	
	public String registerCheck(String registerUserName, String registerPassWord, String registerPassWord1){
		
		if (StringUtils.isBlank(registerUserName) || StringUtils.isBlank(registerPassWord) || StringUtils.isBlank(registerPassWord1)){
			System.out.println("用户名或密码为空！");
			return "error";
		}else if(!isNumeric(registerUserName) || registerUserName.length() != 10){
			return "error";
		}else{
			try {
				if(registerPassWord.equals(registerPassWord1)){
					System.out.println("两次密码输入相同");
					return "success";
				}else{
					System.out.println("两次输入的密码不一致，请重新输入！");
					return "error";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "error";
	}
	/*
	 * 检查邮箱，格式是否正确
	 * 是否已经使用
	 * 
	 * **/
	public String checkEmail(String email){
	    if(emailFormatCheck(email)== false){
	      //如果格式不合格
	        
	        return "0";
	    }else if(userService.isEmailInuse(email) == true ){
	        //如果已经有人占有
	        return "1";
	        
	        
	    }else {
	        //可以使用
	        
	        return "2";
	    }
	    
	    
	}
	public boolean emailFormatCheck(String email){
	    //检查邮箱格式
	    Matcher matcher= emailRegex.matcher(email);
	    return matcher.matches();//成功 true 失败 false
	    
	}
	
}
