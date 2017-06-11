package com.buptsse.spm.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.formula.ptg.StringPtg;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;












import com.alibaba.fastjson.JSONObject;
import com.buptsse.spm.domain.Course;
import com.buptsse.spm.domain.Schedule;
import com.buptsse.spm.domain.SpChapter;
import com.buptsse.spm.domain.SpChapterVideo;
import com.buptsse.spm.domain.User;
import com.buptsse.spm.filter.JspFitter;
import com.buptsse.spm.service.IScheduleService;
import com.buptsse.spm.service.ISelectCourseService;
import com.buptsse.spm.service.ISpChapterService;
import com.buptsse.spm.service.ISpChapterVideoService;
import com.opensymphony.xwork2.ActionSupport;


/**
 * @author BUPT-TC
 * @date 2015年11月26日 下午4:17
 * @description 实现视频相关功能 
 * @modify BUPT-TC
 * @modifyDate 
 */
public class SpChapterAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(SpChapterAction.class);
	@Resource
	private ISpChapterService spChapterService;
	
	@Resource
	private ISpChapterVideoService spChapterVideoService;	
	
	@Resource
	private IScheduleService scheduleService;
	
	@Resource
	private ISelectCourseService selectCourseService;
	
	public List spChapterList = new ArrayList();
	
	public List spChapterVideoList = new ArrayList();
	
	public List scheduleList = new ArrayList();	
	
	public List videoList =new ArrayList<SpChapterVideo>();
	public List chapterList=new ArrayList<SpChapter>();
	public SpChapter spChapter;
	
	public SpChapterVideo spChapterVideo;
	
	public String spChapterName="";
	
	public int videoSize;
	
	public int totalSchedule;//总学习进度
	
	public List chapterScheduleList =  new ArrayList(); ;//章节进度

	
	   
	public String    spChapterId ;
	public String     spChapterVideoId;
	public String     percent;
	public String     studentId;

	/**
	 * 查找所有的视频
	 * @return
	 * @throws Exception
	 */
	public String findSpChapterList() throws Exception{
		
		
		User user = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
		if(user == null || user.getPosition().equals(JspFitter.POSITION_ADMIN)){
		    return "error1";
		    
		}else if(user.getPosition().equals(JspFitter.POSITION_TEACHER)  ){
		    //老师应该是一个查询页面的jsp
		     videoList=spChapterVideoService.findAllSpChapterVideo();
		     chapterList=spChapterService.findAllSpChapter1();
		   
		    return "teacher";
		}else {
		  Map parMap=  new HashMap();
		  parMap.put("studentId",user.getUserId());
		  parMap.put("status",2+"");
		  Long countNums=selectCourseService.count(parMap);
		  
		if(countNums<=0){
		    //选了课，但是没有确认
		    ServletActionContext.getResponse().getWriter().write("您还没有选课或者被确认，无法查看视频进度");
		    return null;
		}     
		    
		spChapterList = spChapterService.findSpChapterDetial();
		
		int averageTotal=0;
		//for(SpChapter spChapter:spChapterList){
		for(int i=0;i<spChapterList.size();i++){	
			//SpChapter spChapter = (SpChapter)spChapterList.get(i);
			Object[] spchapter = (Object[])spChapterList.get(i);
			int sumValueTotal=0;
			int k=0;
			//如果他没有schedule呢，怎么办，扯犊子吗？，list都没有，肯定不行，想办法搞list，就是在选课，加入学生的时候，搞事
			List<Schedule> scheduleListtmp = scheduleService.findScheduleByUserIdAndChapterId(Integer.parseInt(spchapter[0].toString()), user.getUserId());
			if(scheduleListtmp.size()<=0){
			   //这个章节的表还没有
			    insertAllSchedule(Integer.parseInt(spchapter[0].toString()), user.getUserId());
			    //scheduleListtmp = scheduleService.findScheduleByUserIdAndChapterId(Integer.parseInt(spchapter[0].toString()), user.getUserId());
			}
			for(Schedule schedule:scheduleListtmp){
				sumValueTotal+=schedule.getPercent();
				k++;
			}
			if(k==0)k=1;
			averageTotal+=sumValueTotal/k;
			//存入章节进度
			chapterScheduleList.add(sumValueTotal/k);
		}
		//总进度赋值
		totalSchedule = averageTotal/17;
		return "success";
		
		} 
	}	
	/**
	 * 
	 * 查询某个章节的名字列表
	 * 
	 * 
	 * */
	public String getSpchapterVideoList() {
	    User user = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
        if(user == null || user.getPosition().equals(JspFitter.POSITION_ADMIN)){
            return "error1";
            
        }
	    int chapter_id_1;
	    try {
            chapter_id_1=Integer.parseInt(spChapterId);
            
            
       } catch (Exception e) {
           // TODO: handle exception
           chapter_id_1=-1;
       }
	   List<SpChapterVideo> spChapter=spChapterVideoService.findSpChapterVideoByChapterId(chapter_id_1);
	   Map<String,Object> map=new HashMap<String, Object>();
	   map.put("size", spChapter.size());
	   map.put("list", spChapter);
	   String string=JSONObject.toJSONString(spChapter);
	   try {
        ServletActionContext.getResponse().getWriter().write(string);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
	   return null;
	   
    }
	
	/**
	 * 查询章节细节
	 * @return
	 * @throws Exception
	 */
	public String findSpChapter() throws Exception{
	    User user = (User)ServletActionContext.getRequest().getSession().getAttribute("user");
	    if(user == null || user.getPosition().equals(JspFitter.POSITION_ADMIN)){
            return "error1";
            
        }
	
		spChapterVideoList = spChapterVideoService.findSpChapterVideoByChapterId(spChapter.getChapter_id());
		
		spChapterName = "第"+spChapter.getChapter_id()+"章 "+spChapter.getChapter_name();
		videoSize = spChapterVideoList.size();
		//System.out.println("*****SpChapterList******:"+spChapterVideoList.size());
		//获取进度
		scheduleList = scheduleService.findScheduleByUserIdAndChapterId(spChapter.getChapter_id(), user.getUserId());
		
		
		return "success";
	}		
	/**
	 * 老师查看学习进度,
	 * 
	 * 
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public String scheduleCheck(){
	    int page=Integer.parseInt(ServletActionContext.getRequest().getParameter("page"));
        int rows=Integer.parseInt(ServletActionContext.getRequest().getParameter("rows"));
        System.out.println(page+" "+rows);
        Map paramMap = new HashMap();
        int percent_1=0;
        int chapter_id_1=0;
        int spChapterVideoId_1=0;
        try {
             percent_1=Integer.parseInt(percent);

        } catch (Exception e) {
            // TODO: handle exception
             percent_1=-1;
        } 
        try {
            chapter_id_1=Integer.parseInt(spChapterId);

       } catch (Exception e) {
           // TODO: handle exception
           chapter_id_1=-1;
       }
        try {
            spChapterVideoId_1=Integer.parseInt(spChapterVideoId);

       } catch (Exception e) {
           // TODO: handle exception
           spChapterVideoId_1=-1;
       }
        System.out.println(percent+" "+ spChapterId+" "+spChapterVideoId);
        System.out.println(percent_1+" "+ chapter_id_1+" "+spChapterVideoId_1);
        paramMap.put("chapter_id", chapter_id_1);
        paramMap.put("video_step_order", spChapterVideoId_1);
        paramMap.put("percent", percent_1);
        paramMap.put("userid", studentId);
        List list1 = scheduleService.findPage(paramMap, page, rows);
        List<Schedule> list=changeToSchedule(list1); 
        for(Schedule schedule:list){
            Course course= selectCourseService.findCourse(schedule.getUserid());
            SpChapter spChapter=spChapterService.findSpChapterById(schedule.getChapter_id()+"");
            SpChapterVideo  svideo=spChapterVideoService.findSpChapterVideoByStepOrder(schedule.getVideo_step_order()).get(0);
            if(course!=null){
                schedule.setName(course.getName());
                
            }
            if(spChapter!=null){
                System.out.println("章节名称:"+spChapter.getChapter_name());
                schedule.setChapterName(spChapter.getChapter_name());
                
            }
            if(svideo!=null){
                System.out.println("小结名称:"+svideo.getVideo_name());
                schedule.setVideoName(svideo.getVideo_name());
                
            }
        }
        Map<String, Object> map=new HashMap<String, Object>();
        map.put("rows",list);
        map.put("total", list.size());
        String str=JSONObject.toJSONString(map);
        try {
            ServletActionContext.getResponse().getWriter().write(str);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	    return null;
	}

	
	
	@SuppressWarnings("rawtypes")
    private List<Schedule> changeToSchedule(List list) {
        // TODO Auto-generated method stub
	    List<Schedule> schedules=new ArrayList<Schedule>();
	    for(Object obj:list){
	        Object[] rawStrings=(Object[])obj;
	        Schedule schedule=new Schedule();
	        schedule.setChapter_id(Integer.parseInt(rawStrings[0].toString()) );
	        schedule.setVideo_step_order(Integer.parseInt(rawStrings[1].toString()) );
	        schedule.setPercent(Integer.parseInt(rawStrings[2].toString()) );
	        schedule.setUserid(rawStrings[3].toString());
	        schedules.add(schedule);
	  }
	    
	    System.out.println("转换");
        return schedules;
    }


    public ISpChapterService getSpChapterService() {
		return spChapterService;
	}


	public void setSpChapterService(ISpChapterService spChapterService) {
		this.spChapterService = spChapterService;
	}


	public SpChapter getSpChapter() {
		return spChapter;
	}

	public void setSpChapter(SpChapter spChapter) {
		this.spChapter = spChapter;
	}
	
	
	public ISpChapterVideoService getSpChapterVideoService() {
		return spChapterVideoService;
	}

	public void setSpChapterVideoService(
			ISpChapterVideoService spChapterVideoService) {
		    spChapterVideoService = spChapterVideoService;
	}
	
	public SpChapterVideo getSpChapterVideo() {
		return spChapterVideo;
	}


	public void setSpChapterVideo(SpChapterVideo spChapterVideo) {
		this.spChapterVideo = spChapterVideo;
	}
	
	public List getSpChapterVideoList() {
		return spChapterVideoList;
	}


	public void setSpChapterVideoList(List spChapterVideoList) {
		this.spChapterVideoList = spChapterVideoList;
	}


	public String getSpChapterName() {
		return spChapterName;
	}


	public void setSpChapterName(String spChapterName) {
		this.spChapterName = spChapterName;
	}


	public int getVideoSize() {
		return videoSize;
	}


	public void setVideoSize(int videoSize) {
		this.videoSize = videoSize;
	}


	public IScheduleService getScheduleService() {
		return scheduleService;
	}


	public void setScheduleService(IScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}


	public List getScheduleList() {
		return scheduleList;
	}


	public void setScheduleList(List scheduleList) {
		scheduleList = scheduleList;
	}


	public int getTotalSchedule() {
		return totalSchedule;
	}


	public void setTotalSchedule(int totalSchedule) {
		this.totalSchedule = totalSchedule;
	}



	public List getChapterScheduleList() {
		return chapterScheduleList;
	}


	public void setChapterScheduleList(List chapterScheduleList) {
		this.chapterScheduleList = chapterScheduleList;
	}


	public List getSpChapterList() {
		return spChapterList;
	}


	public void setSpChapterList(List spChapterList) {
		this.spChapterList = spChapterList;
	}
	private void insertAllSchedule(int spId,String userId){
	    List<SpChapterVideo> spChapterVideos=spChapterVideoService.findSpChapterVideoByChapterId(spId);
	    for(SpChapterVideo sVideo:spChapterVideos){
	        Schedule schedule=new Schedule();
	        schedule.setChapter_id(spId);
	        schedule.setVideo_step_order(sVideo.getVideo_step_order());
	        schedule.setPercent(0);
	        schedule.setUserid(userId);;
	        scheduleService.insertSchedule(schedule);
	    }
	    
	}


    public String getSpChapterId() {
        return spChapterId;
    }


    public void setSpChapterId(String spChapterId) {
        this.spChapterId = spChapterId;
    }


    public String getSpChapterVideoId() {
        return spChapterVideoId;
    }


    public void setSpChapterVideoId(String spChapterVideoId) {
        this.spChapterVideoId = spChapterVideoId;
    }



    public String getPercent() {
        return percent;
    }


    public void setPercent(String percent) {
        this.percent = percent;
    }


    public String getStudentId() {
        return studentId;
    }


    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
	
	
	
}
