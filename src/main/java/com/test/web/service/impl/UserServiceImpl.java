package com.test.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.web.dao.UserMapper;
import com.test.web.entity.User;
import com.test.web.service.UserServiceI;

/**
 * @author gacl
 * 使用@Service注解将UserServiceImpl类标注为一个service
 * service的id是userService
 */
@Service("userService")
public class UserServiceImpl implements UserServiceI {
	
	@Autowired
	private UserMapper dao;

	@Override
	public void addUser(User user) {
		dao.insert(user);
	}

	@Override
	public User getUserById(String userId) {
		return dao.selectByPrimaryKey(userId);
	}

}