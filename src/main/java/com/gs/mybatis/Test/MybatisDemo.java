package com.gs.mybatis.Test;

import com.gs.mybatis.Util.MybatisUtil;
import com.gs.mybatis.bean.User;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class MybatisDemo {

    static SqlSession sqlSession = MybatisUtil.getSqlSession(false);

    public static void addUser(User user){

        if(user ==null){
            return;
        }

        String statement = "userMapper.addUser";
        int insertResult = sqlSession.insert(statement, user);

//        sqlSession.commit();
        sqlSession.close();
        System.out.print("成功添加了 "+insertResult + " 个用户");
    }

    public static void retrieveUser(int userId){

        if(userId <=0){
            return;
        }

        String SQLMapping = "userMapper.retrieveUser";

        User user = sqlSession.selectOne(SQLMapping, userId);
        System.out.print(user);
    }

    public static void retrieveAllUser(){

        String statement = "userMapper.retrieveAllUser";

        List<User> userList = sqlSession.selectList(statement);

        System.out.print(userList);

    }

    public static void updateUser(User user){

        if(user ==null){
            return;
        }

        String statement = "userMapper.updateUser";

       int ResultInt = sqlSession.update(statement,user);

        System.out.print("更新了 "+ResultInt+" 个用户");

    }

    public static void deleteUser(User user){

        if(user ==null){
            return;
        }

        String statement = "userMapper.deleteUser";

        int ResultInt = sqlSession.update(statement,user);

        System.out.print("删除了 "+ResultInt+" 个用户");

    }

    public static void main(String args[]) throws IOException {

        User user = new User();
        user.setId(1);
        user.setName("Jo Zhou");
        user.setAge(18);

//        addUser(user);
//        retrieveUser(1);
//        retrieveAllUser();
//        updateUser(user);
//        deleteUser(user);

    }
}
