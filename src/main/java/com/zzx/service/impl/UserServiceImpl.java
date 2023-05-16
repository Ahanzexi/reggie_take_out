package com.zzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.common.CustomException;
import com.zzx.dto.SetmealDto;
import com.zzx.entity.Setmeal;
import com.zzx.entity.SetmealDish;
import com.zzx.entity.User;
import com.zzx.mapper.SetmealMapper;
import com.zzx.mapper.UserMapper;
import com.zzx.service.SetmealDishService;
import com.zzx.service.SetmealService;
import com.zzx.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
