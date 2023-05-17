package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.Orders;
import com.zzx.entity.User;
import com.zzx.mapper.OrderMapper;
import com.zzx.mapper.UserMapper;
import com.zzx.service.OrderService;
import com.zzx.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

}
