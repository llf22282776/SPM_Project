package com.buptsse.spm.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.directwebremoting.WebContextFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;










import com.alibaba.fastjson.JSONObject;
import com.buptsse.spm.domain.Course;
import com.buptsse.spm.domain.Exam;
import com.buptsse.spm.domain.User;
import com.buptsse.spm.service.IExamService;
import com.buptsse.spm.util.ConstantUtil;
import com.mysql.jdbc.StringUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


/**
 * @author BUPT-TC
 * @date 2015年11月20日 下午4:17
 * @description 选课的service层接口定义 
 * @modify BUPT-TC
 * @modifyDate 
 */
public class ExamAction extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(ExamAction.class);
	@Resource
	private IExamService examService;
	public List examList = new ArrayList();
	public Exam exam;
	public String examName;
	public String number;	
	String[] result=new String[10];
	int rightNumber=0 ;
	int score=0;
	public static final String[] headers=new String[]{
        "试卷标题",
        "题目内容",
        "选项A",
        "选项B",
        "选项C",
        "选项D",
        "正确选项",
    }; 
	  // 上传文件集合
    private List<File> file;
    // 上传文件名集合
    private List<String> fileFileName;
    // 上传文件内容类型集合
    private List<String> fileContentType;
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
	/**
	 * 查询所有网上测试列表
	 * @return
	 * @throws Exception
	 */
	public String findExamList() throws Exception{
		
		examList = examService.findAllExam();
		
		Map paramMap = new HashMap();
		//接下来判断
		Map<String,Object> seMap= ActionContext.getContext().getSession();
		if(seMap.get("user") == null){
		    //没有登录
		    return "error";
		}
		return "success";
	}	
	
	
	
	/**
	 * 增加试题
	 * @return
	 * @throws Exception
	 */
	public String addQuestion() throws Exception{
		String msg = "";
		
		int number = examService.findExamMaxId(exam.getExamName());
		
		System.out.println("最大编号："+number);
		exam.setNumber(number+1);
		boolean flag = examService.insertExam(exam);
		//boolean flag = false;
		if(flag){
			msg = "1";//表示保存成功
		}else{
			msg = "2";//表示保存失败
		}
		try {
			ServletActionContext.getResponse().getWriter().write(msg);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return null;
	}		
	


	/**
	 * 查询某个测试题下所有的问题
	 * @return
	 */
	public String queryQuestion(){
		
		examList = examService.findExamByName(examName);
		if(examList!=null&&examList.size()>0){
			Exam exam = (Exam) examList.get(0);
			examName =exam.getExamName();
		}
		 
		return "success";
	}

	
	
	/**
	 * 校验测试题答案
	 * @return
	 */
	public String checkAnswer(){
		String yourAnswer ="";
		examList = examService.findExamByName(examName);
		for(int i=0;i<examList.size();i++){
			Exam exam = (Exam) examList.get(i);
			if(exam.getAnswerRight().equals(result[i])){
				rightNumber++;
			}
		}
		score = rightNumber*10;
				
		return "success";
	}	
	
	
	
	/**
	 * 删除测试题
	 * @return
	 */
	public String deleteExam(){
		boolean flag=false;
		String msg="";
		examList = examService.findExamByName(examName);
		for(int i=0;i<examList.size();i++){
			Exam exam = (Exam) examList.get(i);
			flag = examService.deleteExam(exam.getExamName(), exam.getNumber());
			if(!flag){
				break;
			}
		}
		if(flag){
			msg = "删除成功！";//表示删除成功
		}else{
			msg = "删除失败，请联系管理员！";//表示保存失败
		}
		
		String str=JSONObject.toJSONString(msg);
		try {
			ServletActionContext.getResponse().getWriter().write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		return null;
	}		
	
	
	
	/**
	 * 根据测试题名称和序号删除问题
	 * @return
	 */
	public String deleteQuestion(){
		boolean flag=false;
		String msg="";
		
		flag = examService.deleteExam(examName, new Integer(number));

		if(flag){
			msg = "删除成功！";//表示删除成功
		}else{
			msg = "删除失败，请联系管理员！";//表示保存失败
		}
		
		String str=JSONObject.toJSONString(msg);
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
	 * 上传成绩文件
	 * 
	 * */
	public String uploadExamFile(){
	    Map sessionMap=ActionContext.getContext().getSession();
        User user=(User) sessionMap.get("user");
        StringBuffer str=new StringBuffer();
        if(user == null || user.getPosition().equals(ConstantUtil.POSITION_TEACHER) == false){
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
	
	public IExamService getExamService() {
		return examService;
	}


	public void setExamService(IExamService examService) {
		this.examService = examService;
	}



	public List getExamList() {
		return examList;
	}


	public void setExamList(List examList) {
		this.examList = examList;
	}


	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}
	

	public String[] getResult() {
		return result;
	}

	public void setResult(String[] result) {
		this.result = result;
	}	

	public int getRightNumber() {
		return rightNumber;
	}

	public void setRightNumber(int rightNumber) {
		this.rightNumber = rightNumber;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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
     * 获得一个exam对象，成功返回对象
     *失败返回null
     * 
     * 
     * */
    private Exam toStudentUser(Row row){
        Exam exam=new Exam();
        try {
            
            exam.setExamName(getStringFromCell(row.getCell(0)));
            exam.setNumber(examService.findExamMaxId(exam.getExamName()));
            exam.setQuestion(getStringFromCell(row.getCell(1)));
            exam.setAnswerA(getStringFromCell(row.getCell(2)));
            exam.setAnswerB(getStringFromCell(row.getCell(3)));
            exam.setAnswerC(getStringFromCell(row.getCell(4)));
            exam.setAnswerD(getStringFromCell(row.getCell(5)));
            exam.setAnswerRight(getStringFromCell(row.getCell(6)));
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
            return null;
        }
        return exam;
    }
    /**
     * 
     * 获得一个工作表的插入试卷，和失败编号
     * 
     * */
    private List<Integer>  dealSheet(Sheet sheet){
        List<Integer> failRows=new ArrayList<Integer>();
        int firstLine=sheet.getFirstRowNum();
        int lastLine=sheet.getLastRowNum();
        System.out.println("dealSheet-- firstLine:"+firstLine+" lastLine:"+lastLine);
        for(int i=firstLine+1;i<=lastLine;i++){
            Row row=sheet.getRow(i);
            Exam thisuser=toStudentUser(row);
            System.out.println("thisuser == null:"+(thisuser == null));
            if(thisuser == null)failRows.add(new Integer(i));
            else {
               if( !thisuser.getExamName().equals("") && !thisuser.getQuestion().equals("") )examService.saveOrUpdate(thisuser);
               else failRows.add(new Integer(i));
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
