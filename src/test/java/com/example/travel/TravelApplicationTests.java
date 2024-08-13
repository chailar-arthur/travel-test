package com.example.travel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.travel.entity.Spot;
import com.example.travel.service.SpotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class TravelApplicationTests {

    @Autowired
    SpotService spotService;
    @Test
    void test_selectMostPopular() {
        Spot spot = spotService.selectMostPopular();
        int visitNum = spot.getVisitNum();
        assertEquals(visitNum,spotService.getById(1).getVisitNum(),"selectMostPopular测试失败");
        System.out.println("selectMostPopular测试成功");
    }

    @Test
    void test_selectById(){
        Spot spot = spotService.getById(200);
        assertNull(spot, "selectById测试失败");
        System.out.println("selectById测试成功");
    }

}
