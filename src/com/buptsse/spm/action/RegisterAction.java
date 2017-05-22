/**
 * 
 */
package com.buptsse.spm.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.dialect.FirebirdDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.buptsse.spm.domain.Code;
import com.buptsse.spm.domain.Course;
import com.buptsse.spm.domain.User;
import com.buptsse.spm.service.ICodeService;
import com.buptsse.spm.service.IUserService;
import com.buptsse.spm.util.ConstantUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author BUPT-TC
 * @date 2015年10月17日 下午3:53:50
 * @description
 * @modify BUPT-TC
 * @modifyDate 2015年10月24日 下午11:53:50
 */
public class RegisterAction extends ActionSupport {
    private static Logger LOG = LoggerFactory.getLogger(RegisterAction.class);
    private User user;
    @Resource
    private IUserService userService;
    @Resource
    private ICodeService codeService;
    
    public static final String[] headers=new String[]{
        "学号",
        "姓名",
        "邮箱",
    }; 
    protected String userid = "";
    protected String userName = "";
    protected String position = "";
 // 上传文件存放路径
    private final static String UPLOADDIR = "/upload";
    // 上传文件集合
    private List<File> file;
    // 上传文件名集合
    private List<String> fileFileName;
    // 上传文件内容类型集合
    private List<String> fileContentType;


    /**
     * 
     * @discription 实现注册功能
     */
    public String register() {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        String msg = "";
        HttpServletResponse response = ServletActionContext.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        String vString=(String)session.getAttribute("vaildNum");
        System.out.println("验证字符串是:"+vString);
        if (vString == null
                || vString.equals(user.getVaildNum()) == false) {
            msg = "错误:验证码失效,请重新发送邮件";
            try {
                response.getWriter().write(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (user.getUserName() != null) {
            LOG.error("username:" + user.getUserName());
        } else
            LOG.error("username:" + "null");
        if (user == null) {
            LOG.error("USER对象为空！");
        }
        if (StringUtils.isBlank(user.getUserName())
        || StringUtils.isBlank(user.getPassword())
        || StringUtils.isBlank(user.getUserId())
        || StringUtils.isBlank(user.getEmail())
     
                ) {
            msg = "用户名或密码不能为空,请输入用户名或密码！";
        } else {
            LOG.error("开始保存数据");
            if (user.getPassword().equals(user.getPassword1())) {
            	
            	if(userService.findUserById(user.getUserId()) != null
            	
            	        ){
            		msg = "用户账号已存在";
            	}else if(userService.isEmailInuse(user.getEmail())){
            	    
            	    msg = "邮箱已经使用";
            	}
            	else if(userService.isEmailInuse(user.getEmail())){
                    
                    msg = "邮箱已经使用";
                }
            	else{
                    user.setId(user.getUserId());
                    user.setPosition("3");
                    userService.addUser(user);
                    msg = "ok";
                    LOG.error("保存数据");
            	}

                // ServletActionContext.getRequest().setAttribute("registerMsg",
                // "注册成功！");
            } else {
                msg = "对不起，两次输入的密码不一致，请重新输入！";
            }
        }
        try {
            response.getWriter().write(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 查询所有的用户信息
     * 
     * @return
     */
    public String listUser() {

        int page = Integer.parseInt(ServletActionContext.getRequest()
                .getParameter("page"));
        int rows = Integer.parseInt(ServletActionContext.getRequest()
                .getParameter("rows"));

        Map paramMap = new HashMap();
        paramMap.put("userid", userid);
        paramMap.put("userName", userName);
        paramMap.put("position", position);

        List<User> list = userService.findPage(paramMap, page, rows);

        for (User user : list) {
            Code code = codeService
                    .findCodeName("position", user.getPosition());
            String codeName = code.getCodeName();
            user.setPosition(codeName);
        }

        // 查询总条数,不带任何参数
        Long total = userService.count(paramMap);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows", list);
        map.put("total", total);

        String str = JSONObject.toJSONString(map);
        System.out.println("后台输出的json为：" + str);
        try {
            ServletActionContext.getResponse().getWriter().write(str);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }
   
    
    
    /**
     * 用户删除方法
     * 
     * @return
     */
    public String deleteUser() {

        boolean result = false;
        
        @SuppressWarnings("rawtypes")
        Map sessionMap=ActionContext.getContext().getSession();
        User user=(User) sessionMap.get("user");
        String str = "";
        String[] ids = ServletActionContext.getRequest().getParameterValues(
                "ids[]");//可以传数组的
        boolean[] booleanlist=new boolean[ids.length];
        for (int i = 0; i < ids.length; i++) {
            // 将已确认的删除
            try {
                if(user!=null && user.getId().equals(ids[i])){
                 result =false;
                }
                else result = userService.deleteUser(ids[i]);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                result = false;
            }
            booleanlist[i]=result;
        }
        String msg="";
        boolean isAllTrue=true;//假设全部都能删除成功
        List<Integer> list=new ArrayList<Integer>();
        int index=0;
        for(boolean b:booleanlist){
            if(b == false){
                 list.add(index);
                 isAllTrue=false;
             }
                 index++;   
        }
        if(isAllTrue)msg="删除成功";
        else if(list.size() < ids.length){
            msg="部分删除成功,但用户id为:";
            for(int k=0;k<list.size();k++){
                msg+=ids[list.get(k)];
                if(ids[list.get(k)].equals(user.getId()))msg+="(自己)";
                if(k<list.size()-1)msg+=",";
            }
            msg+="删除失败";
            
        }else {
            msg="删除全部失败"; 
            
        }
        System.out.println(msg);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("msg", msg);
        String message = jsonObject.toJSONString();
        try {
            ServletActionContext.getResponse().getWriter().write(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

    /**
     * 增加用户方法
     * 
     * @return
     */
    public String insertUser() {

        // 初始值
        user.setId(user.getUserId());

        boolean result = false;
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            result = userService.addUser(user);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            result = false;
        }

        if (result) {
            map.put("code", "1");
            map.put("message", "添加成功！");
        } else {
            map.put("code", "2");
            map.put("message", "添加失败，请联系管理员！");
        }

        String str = JSONObject.toJSONString(map);
        try {
            ServletActionContext.getResponse().getWriter().write(str);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
    /**
     * 批量添加学生，失败返回失败插入的学号信息或格式信息
     * 传入：xsl文件
     * 
     * */
    public String insertStudents(){
        
        Map sessionMap=ActionContext.getContext().getSession();
        User user=(User) sessionMap.get("user");
        StringBuffer str=new StringBuffer();
        if(user == null || user.getPosition().equals(ConstantUtil.POSITION_ADMIN) == false){
            str.append("您无权进行此操作");
        }
        else 
         for(int i=0;i<file.size();i++){
             File thisFile=file.get(i);
             Workbook workbook=getExeclObject(thisFile);
             if(workbook ==null)str.append("文件"+(i+1)+"出错"+"\n");
             else {
                 int nums=workbook.getNumberOfSheets();
                 if(nums == 0)str.append("文件"+(i+1)+"没有工作表\n");
                 else for(int j=0;j<nums;j++){
                     
                     Sheet sheet=workbook.getSheetAt(j);
                     if(isHeaderleagal(sheet) == false)str.append("文件"+(i+1)+"的第"+(j+1)+"个工作表不合法\n");
                     else {
                         List<Integer> failRows= dealSheet(sheet);
                         if(failRows.size() !=0){
                             str.append("文件"+(i+1)+"的第"+(j+1)+"个工作表以下行添加失败\n");
                             for(int k=0;k<failRows.size();k++){
                                 str.append(failRows.get(k)+"\n");
                             }
                         }
                     }
                 }
             }
         }
        if(str.length()<1)str.append("批量录入成功!");
         try {
            ServletActionContext.getResponse().getWriter().write(str.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        return null;
        
    }
    
    
    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user
     *            the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    public IUserService getUserService() {
        return userService;
    }

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public ICodeService getCodeService() {
        return codeService;
    }

    public void setCodeService(ICodeService codeService) {
        this.codeService = codeService;
    }
    public List<File> getFile() {
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    public List<String> getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(List<String> fileFileName) {
        this.fileFileName = fileFileName;
    }

    public List<String> getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(List<String> fileContentType) {
        this.fileContentType = fileContentType;
    }

    public static String getUploaddir() {
        return UPLOADDIR;
    }
    /**
     * 
     * 获得表格对象
     * 
     * */
    private Workbook getExeclObject(File file){
        try {
           
            
            Workbook wb = null;

            try {
                 wb = new XSSFWorkbook(new FileInputStream(file));
              } catch (Exception ex) {
                  wb = new HSSFWorkbook(new FileInputStream(file));
              }
            return wb;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        
        
        
    }
    
    /**
     * 判断表头是否合法
     * 
     * 
     * */
    private boolean isHeaderleagal(Sheet sheet){
        try {
            int firstLine=sheet.getFirstRowNum();
            int lastLine=sheet.getLastRowNum();
            System.out.println("firstLine:"+firstLine+" lastLine:"+lastLine);
            if(firstLine == lastLine )return false; //行数不对,没有
            
            Row row = sheet.getRow(firstLine); //取第一行
            int firstColNum=row.getFirstCellNum();
            int lastColNum=row.getLastCellNum();
            //问题：为什么列要多一个，行就正确
            if(firstColNum == lastColNum || (lastColNum-firstColNum)!= (headers.length) )return false; //列数不对
            System.out.println("firstColNum:"+firstColNum+" lastColNum:"+lastColNum);
           
            
            for(int i=firstColNum,j=0;i< lastColNum;i++,j++){
                Cell cell=row.getCell(i);

                String value= cell.getStringCellValue();
                System.out.println("value:"+value+" "+" headers[j]:"+headers[j]+(value.equals(headers[j])));
                if(!value.equals(headers[j]))//不等于预设的值
                    return false;
            }
           
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
     
        
        return true;
    }
    /**
     * 获得一个user对象，成功返回对象
     *失败返回null
     * 
     * 
     * */
    private User toStudentUser(Row row){
        User user=new User();
        try {
            
            user.setId(getStringFromCell(row.getCell(0))); //id
            user.setUserId(getStringFromCell(row.getCell(0))); //学号
            user.setPosition(ConstantUtil.POSITION_STUDENT);
            user.setPassword(getStringFromCell(row.getCell(0))); //密码
            user.setUserName(getStringFromCell(row.getCell(1)));
            user.setEmail(getStringFromCell(row.getCell(2)));
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
        return user;
    }
    /**
     * 
     * 获得一个工作表的插入学生，和失败
     * 
     * */
    private List<Integer>  dealSheet(Sheet sheet){
        List<Integer> failRows=new ArrayList<Integer>();
        int firstLine=sheet.getFirstRowNum();
        int lastLine=sheet.getLastRowNum();
        System.out.println("dealSheet-- firstLine:"+firstLine+" lastLine:"+lastLine);
        for(int i=firstLine+1;i<=lastLine;i++){
            Row row=sheet.getRow(i);
            User thisuser=toStudentUser(row);
            System.out.println("thisuser == null:"+(thisuser == null));
            if(thisuser == null)failRows.add(new Integer(i));
            else {
                if(userService.findUserById(thisuser.getUserId())!=null){
                    System.out.println("已经有学生了！！！");
                    failRows.add(new Integer(i));
                    }
                else userService.addUser(thisuser);

            }
        }
        return failRows;
        
    }
    
    private String  getStringFromCell(Cell cell) {
        if(cell.getCellType() ==Cell.CELL_TYPE_STRING)return cell.getStringCellValue();
        if(cell.getCellType() ==Cell.CELL_TYPE_NUMERIC)return  (int)cell.getNumericCellValue()+"";
        if(cell.getCellType() ==Cell.CELL_TYPE_BOOLEAN)return cell.getBooleanCellValue()+"";
        if(cell.getCellType() ==Cell.CELL_TYPE_BLANK)return "";
        return "";
        
    }
    
 
    
}
