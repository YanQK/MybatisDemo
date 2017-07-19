package com.gs.mybatis.Util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MybatisUtil {

    /**
     * 获取 SqlSession 工厂
     * @return
     */
    public static SqlSessionFactory getSqlSessionFactory() {

        SqlSessionFactory factory = null;
        String resource = "mybatis-config.xml";
        InputStream is = null;

        try {
            is = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        factory = new SqlSessionFactoryBuilder().build(is);
        return factory;
    }

    /**
     * 获取 SqlSession 对象    [判断是否自动提交]
     * @param isAutoCommit   是否自动提交
     * @return
     */
    public static SqlSession getSqlSession(boolean isAutoCommit) {
        return getSqlSessionFactory().openSession(isAutoCommit);
    }

}
