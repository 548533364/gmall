package com.atguigu.gmall.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    @Reference
    UserService userService;
//根据用户Id查询用户商品收货地址
    @RequestMapping ("getReceiveAddressByMemberId")
    @ResponseBody
    public  List< UmsMemberReceiveAddress > getReceiveAddressByMemberId( String memberId){  //@RequestBody这个可以传前端的json参数
        List< UmsMemberReceiveAddress >umsMemberReceiveAddresses= userService.getReceiveAddressByMemberId(memberId);
        return umsMemberReceiveAddresses;
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public  List< UmsMember > getAllUser(){    //获取数据库Ums_member的集合
       List<UmsMember>umsMembers= userService.getAllUesr();  //service里面要有一个getAllUser返回List<UmsMember>
        return umsMembers;
    }

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello";
    }


}
