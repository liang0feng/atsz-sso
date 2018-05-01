package com.atsz.sso.service;

import java.sql.SQLException;

import com.atsz.manager.pojo.User;

public interface UserService {

	public Boolean check(String param, String type) throws Exception;

	public Boolean register(User user);

	public String login(User user) throws Exception;

	public User ticketLog(String ticket) throws Exception;


	
}
