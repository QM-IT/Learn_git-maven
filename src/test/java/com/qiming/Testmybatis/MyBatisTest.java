package com.qiming.Testmybatis;

import com.qiming.mapper.UserMapper;
import com.qiming.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Date:2021/11/26
 * Author:ybc
 * Description:
 */
public class MyBatisTest {

    private final SqlSessionFactory sqlSessionFactory;

    public MyBatisTest() throws IOException {
        //加载核心配置文件
        InputStream is = Resources.getResourceAsStream("mybatis-config.xml");
        //获取SqlSessionFactoryBuilder
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        //获取sqlSessionFactory
        sqlSessionFactory = sqlSessionFactoryBuilder.build(is);
    }

    /**
     * SqlSession默认不自动提交事务，若需要自动提交事务
     * 可以使用SqlSessionFactory.openSession(true);
     */


    @Test
    public void testMyBatis() throws IOException {
        //获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        //获取mapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //测试功能
//        User user = mapper.select(6);
//        mapper.updateUser();
//        mapper.deleteUser();
        mapper.insert();
//        List<User> userList = mapper.selectAll();
//        for (User user : userList) {
//            System.out.println(user);
//        }
        //提交事务
        //sqlSession.commit();
    }

    @Test
    public void testParamMapping() throws IOException {
        //获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        //获取mapper接口对象
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
//        Map<String, Object> userToMap = mapper.getUserToMap(6);
//        Map<String, Object> userAllToMap = mapper.getUserAllToMap();
//        userAllToMap.entrySet().forEach(new Consumer<Map.Entry<String, Object>>() {
//            @Override
//            public void accept(Map.Entry<String, Object> stringObjectEntry) {
//                System.out.println(stringObjectEntry);
//            }
//        });
//        List<User> userList = mapper.selectWithMohu("王");
//        for (User user : userList) {
//            System.out.println(user);
//        }
//        mapper.deleteBatch("6,7");
        int i = mapper.insertUser(new User(null, "龙妈", 32, 2, "1008611"));
        System.out.println(i);
    }
}
