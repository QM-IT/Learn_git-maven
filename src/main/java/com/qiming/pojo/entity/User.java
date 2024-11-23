package com.qiming.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author QM
 * @since 2024-11-19
 */
@Data
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    private String email;

    @Override
    public String toString() {
        return "User{" +
                "id = " + id +
                ", name = " + name +
                ", age = " + age +
                ", email = " + email +
                "}";
    }
}
