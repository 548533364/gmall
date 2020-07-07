package com.atguigu.gmall.user.mapper;

import com.atguigu.gmall.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;


import java.util.List;

public interface UserMapper extends Mapper < UmsMember > {
    //通用mapper的整合，以后对umsmember的增删改查操作都用通用mapper操作
            List < UmsMember > selectAllUser ();
}
