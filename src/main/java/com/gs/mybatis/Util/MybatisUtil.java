package com.gs.mybatis.Util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MybatisUtil {

    public static SqlSession getSqlSession(boolean AutoCommit){

        SqlSession sqlSession = null;

        String resource = "mybatis-config.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionBuild = new SqlSessionFactoryBuilder().build(inputStream);
            sqlSession = sqlSessionBuild.openSession(AutoCommit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSession;
    }



}
