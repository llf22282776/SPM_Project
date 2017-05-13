/**
 * 
 */
package com.buptsse.spm.service.impl;

import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.buptsse.spm.dao.IUserDao;
import com.buptsse.spm.domain.User;
import com.buptsse.spm.service.IUserService;

/**
 * @author BUPT-TC 
 * @date 2015年11月24日 下午3:53:50
 * @description 
 */


@Transactional
@Service
public class UserServiceImpl implements IUserService {
	
	@Resource
	private IUserDao iUserDao;
	/* (non-Javadoc)
	 * @see com.buptsse.spm.service.IUserService#findUser(java.lang.String, java.lang.String)
	 */
	
	@Override
	public User findUserById(String id) {
		// TODO Auto-generated method stub
		User user = null;
	
		user = iUserDao.findUserById(id);
		return user;
	}
	
	@Override
	public User findUserById(String id, String password){
		User user= new User();
		user.setUserId(id);
		user.setPassword(password);
		user=iUserDao.findUser_Id(user);
		if(user==null || !user.getPassword().equals(password)){
			return null;
		}else{
			return user;
		}
	}
	
	@Override
	public User findUser(String userName, String password) {
		// TODO Auto-generated method stub
		User user= new User();
		user.setUserName(userName);
		user.setPassword(password);
		user=iUserDao.findUser(user);
		if(user==null || !user.getPassword().equals(password)){
			return null;
		}else{
			return user;
		}
	}
	
	public User findUser(String userName){
		User user = new User();
		user.setUserName(userName);
		user.setId(userName);
		user = iUserDao.findUser(user);
		if(user == null){
			return null;
		}else{
			return user;
		}
	}
	

	/* (non-Javadoc)
	 * @see com.buptsse.spm.service.IUserService#insertUser(com.buptsse.spm.domain.User)
	 */
	@Override
	public boolean insertUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.buptsse.spm.service.IUserService#searchUser(java.lang.String)
	 */
	@Override
	public List<User> searchUser(String choose) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.buptsse.spm.service.IUserService#deleteUser(java.lang.String)
	 */
	@Override
	public boolean deleteUser(String id) {
		// TODO Auto-generated method stub
		User user = new User();
		user= iUserDao.findUserById(id);
		
		return iUserDao.deleteUser(user);
	}

	/* (non-Javadoc)
	 * @see com.buptsse.spm.service.IUserService#addUser(com.buptsse.spm.domain.User)
	 */
	@Override
	public boolean addUser(User user) {
		return iUserDao.addUser(user);
	}

	@Override
	public boolean updateUser(User user) {
		
		
		return iUserDao.updateUser(user);
	}

	@Override
	public List findPage(@SuppressWarnings("rawtypes") Map param, Integer page, Integer rows) {
		// TODO Auto-generated method stub
		String hql = "from User where 1=1 ";
		List paramList = new ArrayList();
		Set<Entry<String, String>> set=param.entrySet();
		for(Entry<String, String> entry:set){
		    String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            System.out.println("key:"+key+" "+"value:"+value);
            if(!StringUtils.isBlank(value)){
                //只要不是空
                hql+="and "+key+"=? ";
                paramList.add(value);               
            }
		}

		System.out.println("hql:"+hql);
		return iUserDao.findPage(hql,paramList, page, rows);		
		
	}

	@Override
	public Long count(Map param) {
		// TODO Auto-generated method stub
		String hql = "select count(*) from User where 1=1 ";
		List paramList = new ArrayList();
		Iterator iter = param.keySet().iterator();
		
		while (iter.hasNext()){
			String key = (String) iter.next();
			String value = (String) param.get(key);
			System.out.println("&&&&&value&&&&:"+value);
			if(!"".equals(value)){
				hql+="and "+key+"=? ";
				paramList.add(value);				
			}
		}		
		return iUserDao.countUser(hql, paramList);
	}

    @Override
    public boolean isEmailInuse(String email) {
        // TODO Auto-generated method stub
        
        
        
        return iUserDao.isEmailInUse(email);
    }

	
	
	
	
	
	
}
