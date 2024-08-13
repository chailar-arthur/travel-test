package com.example.travel.control;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.entity.Spot;
import com.example.travel.entity.Users;
import com.example.travel.service.SpotService;
import com.example.travel.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/spot")
@Controller
public class SpotControl {
    @Autowired
    public SpotService spotService;

    @GetMapping("/detail/{id}")
    public ModelAndView detail(@PathVariable int id) {
        ModelAndView mv = new ModelAndView();
        Spot spot = spotService.getById(id);
        spot.setVisitNum(spot.getVisitNum() + 1);
        spotService.updateById(spot);
        mv.addObject("spot", spot);
        mv.setViewName("spotDetail");
        return mv;
    }

    @GetMapping("/overview/{currentPage}")
    public ModelAndView spotOverview(@PathVariable int currentPage, String x1, String x2, String y1, String y2, String city, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        QueryWrapper<Spot> queryWrapper = new QueryWrapper<>();
        List<Integer> list=new ArrayList<>();
        list.add(0);
        if (session.getAttribute("user") != null)
            list.add((Integer) session.getAttribute("user"));
        queryWrapper.in("add_people",list);
        if (city != null)
            queryWrapper.eq("city", city);
        if (x1 != null && x2 != null && y1 != null && y2 != null) {
            queryWrapper.lt("location_x", Float.parseFloat(x2));
            queryWrapper.lt("location_y", Float.parseFloat(y2));
            queryWrapper.gt("location_x", Float.parseFloat(x1));
            queryWrapper.gt("location_y", Float.parseFloat(y1));
        }
        Page<Spot> page = new Page<>(currentPage, 2);
        spotService.page(page, queryWrapper);
        mv.addObject("page", page);
        mv.addObject("areas", spotService.selectCity());
        mv.setViewName("spotOverview");
        return mv;
    }

    @ResponseBody
    @PostMapping("/add")
    public Map<String, String> add(MultipartFile picture, String name, String location, String description, String location_x, String location_y, String city,HttpSession session) throws IOException {
        Map<String, String> map = new HashMap<>();
        String path = FileUtils.upload(picture, "/image/");
        Spot spot=new Spot();
        spot.setPicture(path);
        spot.setName(name);
        spot.setLocation(location);
        spot.setLocationX(Float.parseFloat(location_x));
        spot.setLocationY(Float.parseFloat(location_y));
        spot.setCity(city);
        spot.setDescription(description);
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        spot.setTime(formattedDateTime);
        spot.setAddPeople((int)session.getAttribute("user"));
        boolean save = spotService.save(spot);
        if (save)
            map.put("status", "1");
        else
            map.put("status", "-1");
        return  map;
    }
}
