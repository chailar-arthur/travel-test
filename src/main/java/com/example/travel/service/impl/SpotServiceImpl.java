package com.example.travel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.travel.entity.Spot;
import com.example.travel.mapper.SpotMapper;
import com.example.travel.service.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class SpotServiceImpl extends ServiceImpl<SpotMapper, Spot> implements SpotService {
    @Autowired
    public SpotMapper spotMapper;

    @Override
    public Spot selectMostPopular() {
        return spotMapper.selectMostPopular();
    }

    @Override
    public List<String> selectCity() {
        return spotMapper.selectCity();
    }
}
