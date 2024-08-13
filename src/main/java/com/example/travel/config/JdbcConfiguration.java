package com.example.travel.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration//声明该类是一个java配置类，相当于一个西xml配置文件
@EnableConfigurationProperties(JdbcProperties.class) //加载JDBC的配置类
public class JdbcConfiguration {


    @Autowired
    private JdbcProperties jdbcProperties;

    //方式一
    @Bean //将返回值注入到IOC容器中
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        //设置连接数据库的四大参数
        dataSource.setDriverClassName(this.jdbcProperties.getDriverClassName());
        dataSource.setUrl("jdbc:sqlite:"+this.jdbcProperties.getUrl()+"db.db");
        dataSource.setValidationQuery("SELECT 1");
        return dataSource;
    }
}