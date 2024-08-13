package com.example.travel.control;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.travel.entity.Route;
import com.example.travel.entity.Spot;
import com.example.travel.service.RouteService;
import com.example.travel.service.SpotService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/route")
public class RouteControl {
    @Autowired
    RouteService routeService;
    @Autowired
    SpotService spotService;
    @GetMapping("/overview/{currentPage}")
    public ModelAndView overview(@PathVariable int currentPage,HttpSession session) {
        ModelAndView mv = new ModelAndView();
        QueryWrapper<Route> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p_id", 0);
        if (session.getAttribute("user") != null)
            queryWrapper.or().eq("p_id", session.getAttribute("user"));
        Page<Route> page = new Page<>(currentPage, 10);
        routeService.page(page, queryWrapper);
        mv.addObject("page", page);
        List<Spot> list = spotService.list();
        mv.addObject("addItems",list);
        mv.setViewName("routeOverview");
        return mv;
    }
    @GetMapping("/detail/{routeId}")
    public ModelAndView detail(@PathVariable int routeId,HttpSession session,String currentPage) {
        ModelAndView mv = new ModelAndView();
        Route route=routeService.getById(routeId);
        Gson gson=new Gson();
        Type mapType = new com.google.gson.reflect.TypeToken<Map<String, List<Integer>>>() {}.getType();
        Map<String, List<Integer>> map = gson.fromJson(route.getRoute(), mapType);
        QueryWrapper<Spot> queryWrapper=new QueryWrapper<>();
        queryWrapper.in("id",map.get("spotId"));
        if (currentPage==null)
            currentPage="1";
        Page<Spot> page = new Page<>(Integer.parseInt(currentPage), 2);
        spotService.page(page, queryWrapper);
        mv.addObject("page", page);
        mv.addObject("routeId",routeId);
        mv.setViewName("routeDetail");

        QueryWrapper<Spot> queryWrapper2=new QueryWrapper<>();
        queryWrapper2.notIn("id",map.get("spotId"));
        queryWrapper2.select("distinct id,name");
        List<Spot> list = spotService.list(queryWrapper2);
        mv.addObject("addItems",list);
        if(session.getAttribute("user") != null){
            if(route.getPId()==(int)session.getAttribute("user"))
                mv.addObject("updateAbility",true);
        }
        return mv;
    }
    @GetMapping("/delete/{routeId}")
    public String deleteRoute(@PathVariable int routeId,HttpSession session) {
        Route route=routeService.getById(routeId);
        if(session.getAttribute("user")!=null){
            if(route.getPId()==(int)session.getAttribute("user")) {
                QueryWrapper<Route> queryWrapper=new QueryWrapper<>();
                queryWrapper.eq("id",routeId);
                routeService.remove(queryWrapper);
            }
        }
        return "redirect:/route/overview/1";
    }

    @GetMapping("/detail/delete/{routeId}")
    public String deleteSpotOfRoute(@PathVariable int routeId,HttpSession session,int spotId) {
        Route route=routeService.getById(routeId);
        if(session.getAttribute("user")!=null){
            if(route.getPId()==(int)session.getAttribute("user")) {
                Gson gson=new Gson();
                Type mapType = new com.google.gson.reflect.TypeToken<Map<String, List<Integer>>>() {}.getType();
                Map<String, List<Integer>> map = gson.fromJson(route.getRoute(), mapType);
                List<Integer> spotIds = map.get("spotId");
                spotIds.remove(Integer.valueOf(spotId));
                map.put("spotId",spotIds);
                route.setRoute(gson.toJson(map));
                routeService.updateById(route);
            }
        }
        return "redirect:/route/detail/" +routeId +"?currentPage=1";
    }

    @PostMapping("/detail/add/{routeId}")
    @ResponseBody
    public Map<String,String> addSpotOfRoute(@PathVariable int routeId,HttpSession session,int spotId){
        Map<String,String> returnMap=new HashMap<>();
        Route route=routeService.getById(routeId);
        if(session.getAttribute("user")!=null){
            if(route.getPId()==(int)session.getAttribute("user")) {
                Gson gson=new Gson();
                Type mapType = new com.google.gson.reflect.TypeToken<Map<String, List<Integer>>>() {}.getType();
                Map<String, List<Integer>> map = gson.fromJson(route.getRoute(), mapType);
                List<Integer> spotIds = map.get("spotId");
                spotIds.add(spotId);
                map.put("spotId",spotIds);
                route.setRoute(gson.toJson(map));
                boolean b = routeService.updateById(route);
                if (b)
                    returnMap.put("status", "1");
                else
                    returnMap.put("status", "-1");
            }
        }
        return returnMap;
    }

    @ResponseBody
    @PostMapping("/add")
    public Map<String,String> addRoute(String name,Integer firstSpotId,HttpSession session){
        Map<String,String> returnMap=new HashMap<>();
        if(session.getAttribute("user")!=null){
            Route route=new Route();
            route.setName(name);
            route.setPId((int)session.getAttribute("user"));
            Map<String, List<Integer>> map=new HashMap<>();
            List<Integer> list=new ArrayList<>();
            list.add(firstSpotId);
            map.put("spotId",list);
            Gson gson=new Gson();
            route.setRoute(gson.toJson(map));
            boolean save = routeService.save(route);
            if (save)
                returnMap.put("status", "1");
            else
                returnMap.put("status", "-1");
        }
        return returnMap;
    }
}
