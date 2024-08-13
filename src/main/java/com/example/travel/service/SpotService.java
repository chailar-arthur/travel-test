package com.example.travel.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.travel.entity.Spot;
import org.springframework.stereotype.Service;
import java.util.List;

public interface SpotService extends IService<Spot> {
    abstract Spot selectMostPopular();
    abstract List<String> selectCity();
}
