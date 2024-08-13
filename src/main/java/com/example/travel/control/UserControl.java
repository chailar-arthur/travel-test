package com.example.travel.control;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.travel.entity.Users;
import com.example.travel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserControl {
    @Autowired
    public UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @ResponseBody
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Users user, HttpSession session) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone()).eq("password", user.getPassword());
        Users user_select = userService.getOne(queryWrapper);
        if (user_select != null) {
            map.put("status", "1");
            session.setMaxInactiveInterval(5000);
            session.setAttribute("user", user_select.id);
        } else {
            QueryWrapper<Users> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("phone", user.getPhone());
            Users user_select2 = userService.getOne(queryWrapper2);
            if(user_select2!=null){
                map.put("status", "0"); //password error
            }else{
                map.put("status", "-1");//this user has not register
            }
        }
        return map;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @ResponseBody
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Users user) {
        Map<String, String> map = new HashMap<>();
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", user.getPhone());
        Users user_select = userService.getOne(queryWrapper);
        if (user_select == null) {
            boolean save = userService.save(user);
            if (save)
                map.put("status", "1");
            else
                map.put("status", "-1");
        } else {
            map.put("status", "0");
        }
        return map;
    }

    @GetMapping("/exit")
    public String exit(HttpSession session){
        session.removeAttribute("user");
        return "index";
    }
}
