package com.buptsse.spm.util;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import uk.ltd.getahead.dwr.WebContext;

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
    private static final String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    private static final String checkName="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
    private Pattern emailRegex = Pattern.compile(DwrUtil.check);
    private Pattern unamePattern = Pattern.compile(DwrUtil.checkName);
    private static final String MAILHOST = "smtp.163.com";
    private static final String PASSPORT = "llf22282776";
    private static final String PASSWD = "seeAISINI211";

    /**
     * 
     * @param userName
     *            用户名
     * @return boolean 判定用户名是否为数字，是返回true，否则false
     */

    public boolean isNumeric(String userName) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(userName);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 发送邮件
     * 
     * 
     * 
     * */

    public String sendEmail(String email) {
        int timeout = 10000;
        try {
            JavaMailSenderImpl jemailImpl = new JavaMailSenderImpl();
            jemailImpl.setHost(DwrUtil.MAILHOST);
            jemailImpl.setUsername(PASSPORT);
            jemailImpl.setPassword(PASSWD);
            MimeMessage mailMessage = jemailImpl.createMimeMessage();
            MimeMessageHelper mHelper = new MimeMessageHelper(mailMessage);
            mHelper.setFrom("llf22282776@163.com");
            mHelper.setTo(email);
            mHelper.setSubject("SPM教务系统注册验证码");
            int randomNum = ((int) ((Math.random() * 9 + 1) * 10000));

            HttpSession session = WebContextFactory.get().getSession();
            mHelper.setText("您的验证码是:" + randomNum);
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.timeout", "" + timeout);// 超时时间设置
            jemailImpl.setJavaMailProperties(properties);
            jemailImpl.send(mailMessage);
            session.setAttribute("vaildNum", randomNum + "");// 把数字字符串放进来

        } catch (Exception e) {
            // TODO: handle exception
            return "error";
        }

        return "ok";// 发送成功

    }

    /**
     * 判断是不是有user
     * 
     * 
     * 
     * */
    public String isUserHas() {
        HttpSession session = WebContextFactory.get().getSession();
        if (session.getAttribute("user") != null) {
            return "has";

        } else {
            return "doHas";
        }

    }

    /**
     * 游客登陆
     * 
     * */
    public String commonLogin() {
        HttpSession session = WebContextFactory.get().getSession();
        session.setAttribute("common", "somthing");// 放个游客标志
        return "ok";

    }

    /**
     * 验证码是否正确
     * 
     * */

    public String checkVaildText(String validText) {
        HttpSession session = WebContextFactory.get().getSession();
        String textString = session.getAttribute("vaildNum") + "";// 取出验证码
        return textString.equals(validText) ? "ok" : "error";// 验证成功返回ok

    }

    /**
     * 
     * @param userName
     *            用户名
     * @param passwWord
     *            密码
     * @return String 状态消息，但是并没有什么用！
     */
    public String loginCheck(String userName, String passwWord) {
        // 需要修改
        System.out.println("此处写用户名密码校验的方法，通过返回1，失败返回失败信息");
        System.out
                .println("userName: " + userName + ", passWord: " + passwWord);

        if (StringUtils.isBlank(userName) || StringUtils.isBlank(passwWord)) {
            // ServletActionContext.getRequest().setAttribute("loginMsg",
            // "账号或密码未输入！");
            return "账号或密码未输入！";
        }
        try {
            if (userService.findUserById(userName, passwWord) == null) {
                // ServletActionContext.getRequest().setAttribute("loginMsg",
                // "对不起，该用户不存在或密码输入错误！");
                return "对不起，该用户不存在或密码输入错误！";
            } else {
                // ServletActionContext.getRequest().setAttribute("loginMsg",
                // "登入成功！");
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "1";
    }

    /**
     * by llf 用户注册密码检查
     * 
     * */
    public String registerPwdCheck(String pwd) {
        System.out.println("正在检查密码是否合格" + " " + pwd);
        Pattern pattern = Pattern
                .compile("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{6,16}$");
        Matcher isNumOrLets = pattern.matcher(pwd);
        if (pwd.length() >= 6 && pwd.length() <= 16) {
            if (isNumOrLets.matches()) {
                // 位数符合要求的前提下，满足字母和数字的混合搭配
                return "ok";// 返回ok
            } else {
                return "密码必须是数字和字母的组合";
            }
        } else {
            return "密码必须是6位以上，16位以下"; // 密码不满足要求
        }

    }

    /**
     * 
     * @param userName
     *            用户名
     * @return String 检测用户名输入是否有效：10位数字，返回相应字符串
     */
    public String extenceCheck(String userId) {
        System.out.println("开始检验账号是否存在");
        try {
            if (StringUtils.isBlank(userId)) {
                System.out.println("账号不可为空，应为10位");
                return "账号不可为空";
            } else if (!isNumeric(userId) || userId.length() != 10) {
                System.out.println("账号应为10位学号");
                return "账号应为10位数字";
            } else {
                if (userService.findUserById(userId) != null) {
                    System.out.println("账号已存在，请重新输入");
                    return "extence";
                } else {
                    System.out.println("账号不存在");
                    return "unExtence";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    public String registerCheck(String registerUserName,
            String registerPassWord, String registerPassWord1) {

        if (StringUtils.isBlank(registerUserName)
                || StringUtils.isBlank(registerPassWord)
                || StringUtils.isBlank(registerPassWord1)) {
            System.out.println("用户名或密码为空！");
            return "error";
        } else if (!isNumeric(registerUserName)
                || registerUserName.length() != 10) {
            return "error";
        } else {
            try {
                if (registerPassWord.equals(registerPassWord1)) {
                    System.out.println("两次密码输入相同");
                    return "success";
                } else {
                    System.out.println("两次输入的密码不一致，请重新输入！");
                    return "error";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "error";
    }
    /**
     * 
     * 检查用户名格式
     * 
     * 
     * */
    public String checkUserNameFormat(String userName) {
        //检查用户命名格式，不能有特殊字符
        Matcher matcher = unamePattern.matcher(userName);
        if( matcher.matches() == true)return "错误:用户名含有特殊字符 ";// 成功 true 失败 false
        else {
            if(userName.length()<1 || userName.length()> 10){
                return "用户名必须在十个字符以内";
            }else{
                return "ok";
            }
            
        }
    }

    /**
     * 
     * 检查邮箱，格式是否正确 是否已经使用
     * 
     * 
     * *
     **/

    public String checkEmail(String email) {
        System.out.println("检查email中：" + " " + email);
        if (emailFormatCheck(email) == false) {
            // 如果格式不合格

            return "0";
        } else if (userService.isEmailInuse(email) == true) {
            // 如果已经有人占有
            return "1";

        } else {
            // 可以使用
            return "2";
        }

    }
    /**
     * 发言判断
     * 
     * */
    public boolean checkMessageBlank(String s1,String s2,String s3){
        return !(StringUtils.isBlank(s1) || StringUtils.isBlank(s2)||StringUtils.isBlank(s3));
        
        
    }
    
    
    public boolean emailFormatCheck(String email) {
        // 检查邮箱格式
        Matcher matcher = emailRegex.matcher(email);
        return matcher.matches();// 成功 true 失败 false

    }

}
