package com.example.travel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Spot {
    @TableId(type= IdType.AUTO)
    public Integer id;
    public float locationX;
    public float locationY;
    public String name;
    public String description;
    public String picture;
    public String location;
    public String city;
    public String time;
    public int addPeople;
    public int visitNum;
}
