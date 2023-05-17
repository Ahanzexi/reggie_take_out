package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.OrderDetail;
import com.zzx.entity.User;
import com.zzx.mapper.OrderDetailMapper;
import com.zzx.mapper.UserMapper;
import com.zzx.service.OrderDetailService;
import com.zzx.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}
