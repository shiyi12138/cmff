package com.baizhi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String id;

    private String phoneNumber;

    private String password;

    private String name;

    private String dharma;

    private String head_Img;

    private String sex;

    private String address;

    private String sign;

    private String guru_Id;

    private Date last_Date;

    private Date create_Date;

    private String status;

    private String salt;

    private String other;

}