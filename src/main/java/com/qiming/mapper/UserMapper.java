package com.qiming.mapper;

import com.qiming.pojo.User;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    /**
     * CRUD
     */
    User select(int id);

    List<User> selectAll();

    void updateUser();

    void deleteUser();

    void insert();
    int insertUser(User user);

    /**
     * ParamMapping
     */
    Map<String, Object> getUserToMap(@Param("id") int ID);
    @MapKey("id")
    Map<String, Object> getUserAllToMap();

    /**
     * 测试模糊查询
     * @param mohu
     * @return
     */
    List<User> selectWithMohu(@Param("for_name") String mohu);

    /**
     * 批量删除
     * @param ids
     * @return
     */
    int deleteBatch(@Param("ids") String ids);
}
