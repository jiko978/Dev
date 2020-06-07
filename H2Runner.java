package com.example.demo;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class H2Runner implements ApplicationRunner
{
    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        try(Connection connection = dataSource.getConnection()){
            System.out.println(connection);
            String URL = connection.getMetaData().getURL();
            System.out.println(URL);
            String User = connection.getMetaData().getUserName();
            System.out.println(User);

            System.out.println("# USER 테이블 생성");
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE USER(ID INTEGER NOT NULL, NAME VARCHAR(255), PRIMARY KEY (ID))";
            statement.executeUpdate(sql);
            
            System.out.println("# TB_COUPON_MST 테이블 생성");
            Statement statement2 = connection.createStatement();
            String sql2 = "CREATE TABLE TB_COUPON_MST(COUPON_ID varchar(30) NOT NULL, USER_ID VARCHAR(20), USE_YN varchar(1), EXPIRE_DT varchar(8), PRIMARY KEY ( COUPON_ID ))";
            statement2.executeUpdate(sql2);
            
        }

        System.out.println("# USER 등록");
        jdbcTemplate.execute("INSERT INTO USER VALUES(1, 'saelobi')");
        
        //System.out.println("# TB_COUPON_MST 등록");
        //jdbcTemplate.execute("INSERT INTO TB_COUPON_MST VALUES('COUPON-20200601-0000001','jiko','20200607')");

    }
}
