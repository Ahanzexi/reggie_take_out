package com.zzx.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.entity.ShoppingCart;
import com.zzx.entity.User;
import com.zzx.mapper.ShoppingCartMapper;
import com.zzx.mapper.UserMapper;
import com.zzx.service.ShoppingCartService;
import com.zzx.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

}
