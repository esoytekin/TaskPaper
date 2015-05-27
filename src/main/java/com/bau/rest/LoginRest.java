package com.bau.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bau.rest.entity.User;
import com.bau.rest.entity.UserRole;
import com.bau.rest.pojo.Role;
import com.bau.rest.pojo.SignElement;
import com.bau.rest.service.TaskpaperServices;

@Component
@RequestMapping("/login")
public class LoginRest {

	@Autowired
	private TaskpaperServices taskpaperServices;
	
	@RequestMapping(method=RequestMethod.POST)
	@ResponseBody
	public boolean signUp(@ModelAttribute SignElement signElement){

		User user = new User();
		user.setName(signElement.getLgFirstName());
		user.setSurname(signElement.getLgLastName());
		user.setUsername(signElement.getLgUsername());
		user.setPassword(signElement.getLgPassword());
		user.setEmail(signElement.getLgEmail());
		user.setDate(new Date());
		user.setEnabled(true);
		
		UserRole role = new UserRole();
		role.setUsername(user.getUsername());
		role.setRole(Role.ROLE_USER);
		
		User oldUser = taskpaperServices.getUserByUsername(user.getUsername());
		if(oldUser!=null)
			return false;
		
		user.setUserRole(role);

		taskpaperServices.saveUser(user);
		
		return true;
		
		
	}
}
