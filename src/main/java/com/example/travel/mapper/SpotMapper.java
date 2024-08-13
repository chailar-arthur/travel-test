package com.example.travel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.travel.entity.Spot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;
@Mapper
public interface SpotMapper extends BaseMapper<Spot> {
    @Select("SELECT * from spot WHERE visit_num=(SELECT MAX(visit_num) from spot)")
    abstract Spot selectMostPopular();
    @Select("SELECT DISTINCT  city from spot")
    abstract List<String> selectCity();
}
