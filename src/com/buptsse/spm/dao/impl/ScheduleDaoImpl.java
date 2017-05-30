package com.buptsse.spm.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.buptsse.spm.dao.IScheduleDao;
import com.buptsse.spm.domain.Schedule;


/**
 * @author BUPT-TC
 * @date 2015年11月16日 下午3:53:50
 * @description
 * @modify	BUPT-TC
 * @modifyDate 
 */

@Repository
public class ScheduleDaoImpl extends BaseDAOImpl<Schedule> implements IScheduleDao {
	private static Logger LOG = Logger.getLogger(ScheduleDaoImpl.class);


	@Override
	public boolean saveSchedule(Schedule schedule) {
		// TODO Auto-generated method stub
		try{
			super.save(schedule);
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e);
			return false;
		}
		return true;		
	}

	@Override
	public boolean updateSchedule(Schedule schedule) {
		// TODO Auto-generated method stub
		try{
			super.update(schedule);
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteSchedule(Schedule schedule) {
		// TODO Auto-generated method stub
		try{
			super.delete(schedule);
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e);
			return false;
		}
		return true;
	}



	@Override
	public boolean saveOrUpdateSchedule(Schedule schedule) {
		// TODO Auto-generated method stub
		try{
			super.saveOrUpdate(schedule);
		}catch(Exception e){
			e.printStackTrace();
			LOG.error(e);
			return false;
		}
		return true;
	}


	@Override
	public List<Schedule> findSchedule(String hql, List param) {
		// TODO Auto-generated method stub
		return super.find(hql, param);
		
	}

	@Override
	public List<Schedule> findSchedule(String hql, Object[] param) {
		// TODO Auto-generated method stub
		return super.find(hql, param);
	}




	@Override
	public Long countSchedule(String hql, List param) {
		// TODO Auto-generated method stub
		return super.count(hql, param);
	}

	
	@Override
	public Schedule findScheduleById(Integer id){
		// TODO Auto-generated method stub
		return super.get(Schedule.class, id);
		
	}
	@Override
	public List findWihtSql(String hql) {
	    SQLQuery sqlQuery = super.getSessionFactory().getCurrentSession().createSQLQuery(hql);
        List list = sqlQuery.list();
        return list;
    }
    @Override
    public List<Schedule> find(String hql, Schedule[] param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Schedule get(String hql, Schedule[] param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long count(String hql, Schedule[] param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer executeHql(String hql, Schedule[] param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List findPage(String hql,List param , Integer page,Integer rows) {
        // TODO Auto-generated method stub
        System.out.println("开始底层分页查询，查询学生视频学习进度:");
        return super.find(hql, param, page, rows);
    }

  

}
