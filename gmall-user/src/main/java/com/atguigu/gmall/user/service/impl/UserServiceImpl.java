package com.atguigu.gmall.user.service.impl;

import com.atguigu.gmall.user.bean.UmsMember;
import com.atguigu.gmall.user.bean.UmsMemberReceiveAddress;
import com.atguigu.gmall.user.mapper.UmsMemberReceiveAddressMapper;
import com.atguigu.gmall.user.mapper.UserMapper;
import com.atguigu.gmall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UmsMemberReceiveAddressMapper umsMemberReceiveAddressMapper;


    @Override
    public List < UmsMember > getAllUesr () {
        List < UmsMember >umsMemberList= userMapper.selectAll ();  //userMapper.selectAll ();     //userMapper.selectAllUser();
        return umsMemberList;
    }

    @Override  //封装的参数对象
    public List < UmsMemberReceiveAddress > getReceiveAddressByMemberId ( String memberId ) {
        UmsMemberReceiveAddress umsMemberReceiveAddress=new UmsMemberReceiveAddress();
        umsMemberReceiveAddress.setMemberId ( memberId );
        //List < UmsMemberReceiveAddress > umsMemberReceiveAddresses=umsMemberReceiveAddressMapper.selectByExample ( umsMemberReceiveAddress );
        List < UmsMemberReceiveAddress >umsMemberReceiveAddresses= umsMemberReceiveAddressMapper.select ( umsMemberReceiveAddress );
        return umsMemberReceiveAddresses;

        /*Example e=new Example (UmsMemberReceiveAddress.class  );
        e.createCriteria ().andEqualTo ( "memberId",memberId );
       umsMemberReceiveAddressMapper.selectByExample ( e );
       return null;                  Example使用复杂，后期再用               */
    }


}
