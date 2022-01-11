package com.websocket.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.websocket.Mapper.MessageMapper;
import com.websocket.dto.MessageDto;
import com.websocket.entity.MessageEntity;
import com.websocket.entity.UserEntity;
import com.websocket.service.MessageService;
import com.websocket.service.UserService;

@Controller
public class MainController {
	
	@Autowired
	private UserService userServiceImpl;
	
	@Autowired
	private MessageService messageServiceImpl;
	
	@RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        UserEntity userEntity =(UserEntity) request.getSession().getAttribute("username");

        if (userEntity == null ) {
            return "redirect:/login";
        }
        model.addAttribute("username", userEntity);

        return "chat";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String showLoginPage() {
        return "login";
    }
    @RequestMapping(path = "/chatAdmin", method = RequestMethod.GET)
    public String showChatAdminPage(Model model) {
//    	List<UserEntity> lstEntity= userServiceImpl.findAll();
    	List<UserEntity> lstEntity= userServiceImpl.findLstUser();
    	model.addAttribute("lstUser", lstEntity);
        return "chatadmin";
    }
    
    @RequestMapping(path = "/chatAdmin", method = RequestMethod.POST)
    public ResponseEntity<?> searchMessageUser(Model model, @RequestBody String id) {
    	Long idNewLong=Long.parseLong(id);
    	List<MessageEntity> lstEntity= messageServiceImpl.findBySenderOrReceiver(idNewLong, idNewLong);
    	UserEntity userEntity= userServiceImpl.findOneById(idNewLong);
    	Object[] mang = new Object[2];
    	mang[0]=userEntity;
    	List<MessageDto> lstDto = new ArrayList<MessageDto>();
    	for(MessageEntity entity : lstEntity) {
    		MessageDto dto = MessageMapper.convertToDto(entity);
    		lstDto.add(dto);
    	}
    	mang[1]=lstDto;
    	System.out.println("oke");
        return new ResponseEntity<Object>(mang, HttpStatus.OK);
    }
    
    
    @RequestMapping(path = "/showLstUser", method = RequestMethod.POST)
    public ResponseEntity<?> showLstUser(Model model, @RequestBody Long id) {
//    	List<UserEntity> lstEntity= userServiceImpl.findLstUser();
    	UserEntity entity = userServiceImpl.findOneById(id);
        return new ResponseEntity<UserEntity>(entity, HttpStatus.OK);
    }
    
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String doLogin(HttpServletRequest request, @RequestParam(defaultValue = "") String username) {
        username = username.trim();

        if (username.isEmpty()) {
            return "login";
        }else {       
        	
        	UserEntity entity= userServiceImpl.findOneByUserName(username);
        	if(entity==null) {
        		entity= new UserEntity();
        		entity.setUsername(username);
                entity=userServiceImpl.save(entity);
        	}else {
        		if(entity.getUsername().equals("server")) {
        			request.getSession().setAttribute("username", entity);
        			return "redirect:/chatAdmin";
        		}
        	}
        	request.getSession().setAttribute("username", entity);
        	
        }
        
        return "redirect:/";
    }

    @RequestMapping(path = "/logout")
    public String logout(HttpServletRequest request) {
        request.getSession(true).invalidate();
        
        return "redirect:/login";
    }
    
    
    @RequestMapping(path = "/callUser", method = RequestMethod.POST)
    public ResponseEntity<?> callUser(Model model, @RequestBody Long id) {
//    	List<UserEntity> lstEntity= userServiceImpl.findLstUser();
    	UserEntity entity = userServiceImpl.findOneById(id);
        return new ResponseEntity<UserEntity>(entity, HttpStatus.OK);
    }
}
