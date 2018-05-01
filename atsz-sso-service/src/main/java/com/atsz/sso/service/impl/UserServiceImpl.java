package com.atsz.sso.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atsz.manager.mapper.UserMapper;
import com.atsz.manager.pojo.User;
import com.atsz.sso.service.UserService;
import com.atsz.sso.service.redis.impl.RedisServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserServiceImpl implements UserService {
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisServiceImpl reidsService;

	@Override
	public Boolean check(String param, String type) throws Exception {
		User user = new User();
		switch (type) {
		case "1":
			user.setUsername(param);
			break;
		case "2":
			user.setPhone(param);
			break;
		case "3":
			user.setEmail(param);
			break;

		default:
			break;
		}
		
		User selectOne = userMapper.selectOne(user);
		if (selectOne == null) {
			//可用：true
			return true;
		}
		return false;
	}

	@Override
	public Boolean register(User user){
		String md2Hex = DigestUtils.md2Hex(user.getPassword());
		user.setPassword(md2Hex);
		int selective = 0;
		try {
			selective = userMapper.insertSelective(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (selective == 0) {
			return false;
		}
		return true;
	}

	@Override
	public String login(User user) throws Exception{
		User userEg = new User();
		userEg.setUsername(user.getUsername());
		
		User selectOne = userMapper.selectOne(userEg);
		if (selectOne == null) {
			//用户名不存在
			return null;
		}
		
		
		if (!selectOne.getPassword().equals(DigestUtils.md2Hex(user.getPassword()))) {
			//密码不正确
			return null;
		}
		
		//生成ticket
		String ticket = DigestUtils.md2Hex(System.currentTimeMillis() + user.getUsername());
		//保存到redis中
		reidsService.expire(ticket, MAPPER.writeValueAsString(selectOne), 1800);
		return ticket;
	}

	@Override
	public User ticketLog(String ticket) throws Exception {
		String redisTicket = reidsService.get(ticket);
		
		if (redisTicket == null || "".equals(redisTicket)) {
			//缓存中不存在
			return null;
		}
		User user = MAPPER.readValue(redisTicket, User.class);
				
		return user;
	}

}
