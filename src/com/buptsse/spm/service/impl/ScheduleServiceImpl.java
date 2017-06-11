package com.buptsse.spm.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.buptsse.spm.dao.IScheduleDao;
import com.buptsse.spm.domain.Schedule;
import com.buptsse.spm.service.IScheduleService;


/**
 * @author BUPT-TC  
 * @date 2015年11月16日 下午3:53
 * @description 视频进度的service层实现类定义 
 * @modify BUPT-TC 
 * @modifyDate 
 */

@Transactional
@Service
public class ScheduleServiceImpl implements IScheduleService{

	@Resource
	private IScheduleDao iScheduleDao;



	@Override
	public Schedule findScheduleById(String id) {
		// TODO Auto-generated method stub
		return iScheduleDao.findScheduleById(new Integer(id));
	}

	@Override
	public boolean insertSchedule(Schedule schedule) {
		// TODO Auto-generated method stub
		return iScheduleDao.saveSchedule(schedule);
	}

	@Override
	public List<Schedule> findAllSchedule() {
		// TODO Auto-generated method stub
		String hql = "from Schedule";
		List list = new ArrayList();
		return iScheduleDao.findSchedule(hql, list);
	}

	@Override
	public List<Schedule> findScheduleByUserIdAndStepOrder(Integer stepOrder,String userId) {
		// TODO Auto-generated method stub
		
		String hql = "from Schedule where userId=? and video_step_order=?";
		List listParam = new ArrayList();
		listParam.add(userId);
		listParam.add(stepOrder);
		return iScheduleDao.findSchedule(hql, listParam);		
	}	
	
	@Override
	public boolean deleteSchedule(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveOrUpdate(Schedule schedule) {
		// TODO Auto-generated method stub
		//List<Schedule> list = this.findScheduleByUserIdAndStepOrder(schedule.getVideo_step_order(), schedule.getUserid());
		
		//if(list!=null && list.size()>0){
		//	Schedule scheduleTmp = list.get(0);
		//	scheduleTmp.setPercent(schedule.getPercent());
		//}
		//return false;
		
		return iScheduleDao.saveOrUpdateSchedule(schedule);
	}
	

	@Override
	public List<Schedule> findScheduleByUserIdAndChapterId(Integer chapterId,
			String userId) {
		// TODO Auto-generated method stub
		String hql = "from Schedule where userId=? and chapter_id=?";
		List listParam = new ArrayList();
		listParam.add(userId);
		listParam.add(chapterId);
		return iScheduleDao.findSchedule(hql, listParam);			
		
	}
	
	
	
	
	
	public IScheduleDao getiScheduleDao() {
		return iScheduleDao;
	}

	public void setiScheduleDao(IScheduleDao iScheduleDao) {
		this.iScheduleDao = iScheduleDao;
	}

    @SuppressWarnings("rawtypes")
    @Override
    public List findPage(Map param, Integer page, Integer rows) {
        // TODO Auto-generated method stub
     // TODO Auto-generated method stub
        System.out.println("$$$$$$$$进入service**查询");
        String hql = "SELECT * from Schedule where";
        List paramList = new ArrayList();
        Iterator iter = param.keySet().iterator();
        int index=0;
        while (iter.hasNext()){
            String key = (String) iter.next();
            String value = param.get(key)+"";
            System.out.println("&&&&&value&&&&:"+value);
            if(!"".equals(value) && !"-1".equals(value)){
              if(index==0)  {hql+="  "+key+" = "+value;index++;}
              else hql+=" and "+key+" = "+value;
                paramList.add(value);               
            } 
        }       
        
        System.out.println("进入查询的Service:"+hql);
        List list=iScheduleDao.findWihtSql(hql);
        return list;
        //return iSelectCourseDao.findAllCourse();
    }






}
