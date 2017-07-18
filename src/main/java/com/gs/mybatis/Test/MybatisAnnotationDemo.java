package com.gs.mybatis.Test;

import com.gs.mybatis.Impl.UserMapperImpl;
import com.gs.mybatis.Util.MybatisUtil;
import com.gs.mybatis.bean.User;
import org.apache.ibatis.session.SqlSession;

import java.io.IOException;
import java.util.List;

public class MybatisAnnotationDemo {

    public static void main(String args[]) throws IOException {

        User user = new User();
        user.setId(4);
        user.setName("Jo Zhou");
        user.setAge(18);

        SqlSession sqlSession = MybatisUtil.getSqlSession(true);
        UserMapperImpl annotationImplTool = sqlSession.getMapper(UserMapperImpl.class);

        int addResult = annotationImplTool.AddUser(user);

        User retrieveUser = annotationImplTool.retrieveUser(1);
        List<User> userList = annotationImplTool.retrieveAllUser();

        user.setName("Jo Yang");
        int updateResult = annotationImplTool.updateUser(user);

        int deleteResult = annotationImplTool.deleteUser("Jo Yang");


    }
}
