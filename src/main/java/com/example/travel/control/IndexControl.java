package com.example.travel.control;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.travel.TravelApplication;
import com.example.travel.entity.Route;
import com.example.travel.entity.Spot;
import com.example.travel.service.PageService;
import com.example.travel.service.RouteService;
import com.example.travel.service.SpotService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexControl {
    @Autowired
    SpotService spotService;
    @Autowired
    PageService pageService;
    @Autowired
    RouteService routeService;
    @RequestMapping("/index")
    public ModelAndView index(HttpSession session) {
        ModelAndView mv=new ModelAndView();
        mv.setViewName("index");
        mv.addObject("spot",spotService.selectMostPopular());
        return mv;
    }
    @GetMapping("/pdf/{routeId}")
    public void generatePdf(@PathVariable int routeId, HttpServletResponse response) throws IOException {
        Route route=routeService.getById(routeId);
        Gson gson=new Gson();
        Type mapType = new com.google.gson.reflect.TypeToken<Map<String, List<Integer>>>() {}.getType();
        Map<String, List<Integer>> map = gson.fromJson(route.getRoute(), mapType);
        QueryWrapper<Spot> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("id",map.get("spotId"));
        List<Spot> list = spotService.list(queryWrapper);
        Map<String,Object> map2=new HashMap<String, Object>();
        map2.put("spots",list);
        pageService.generatePdf(response.getOutputStream(),map2);
    }
}
