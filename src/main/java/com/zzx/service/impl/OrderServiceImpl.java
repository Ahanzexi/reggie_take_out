package com.zzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzx.common.CustomException;
import com.zzx.entity.*;
import com.zzx.mapper.OrderMapper;
import com.zzx.mapper.UserMapper;
import com.zzx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private  HttpSession session;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    /**
     * 用户下单
     * @param orders
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        // 获取当前用户Id
        final Long userId = (Long) session.getAttribute("user");
        // 查询当前用户的购物车数据
        final LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        final List<ShoppingCart> shoppingCartList = shoppingCartService.list(wrapper);
        // 如果购物车为空
        if(shoppingCartList==null||shoppingCartList.size()==0){
            throw new CustomException("购物车为空,不能下单!");
        }
        // 获取用户数据
        final User user = userService.getById(userId);

        // 获取地址数据
        final Long addressBookId = orders.getAddressBookId();
        final AddressBook addressBook = addressBookService.getById(addressBookId);
        if(addressBook == null){
            throw new CustomException("地址信息有误,不能下单");
        }
        // 计算总金额和构造订单明细
        final long orderId = IdWorker.getId();//订单号

        // 原子操作,保证线程安全
        AtomicInteger amount = new AtomicInteger(0);
         List<OrderDetail> orderDetails = shoppingCartList.stream().map((item)->{
            final OrderDetail orderDetail = new OrderDetail();
             orderDetail.setOrderId(orderId);
             orderDetail.setNumber(item.getNumber());
             orderDetail.setDishFlavor(item.getDishFlavor());
             orderDetail.setDishId(item.getDishId());
             orderDetail.setSetmealId(item.getSetmealId());
             orderDetail.setName(item.getName());
             orderDetail.setImage(item.getImage());
             orderDetail.setAmount (item.getAmount());
             amount.addAndGet (item.getAmount().multiply (new BigDecimal (item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        // 构造orders对象

        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName()==null?"":addressBook.getProvinceName())
        +(addressBook.getCityName()==null?"":addressBook.getCityName())
        +(addressBook.getDistrictName()==null?"":addressBook.getDistrictName())
        +(addressBook.getDetail()==null?"":addressBook.getDetail()));
        // 向订单表插入数据,一条
        this.save(orders);


        // 向订单明细表插入数据,多条
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车数据
        shoppingCartService.remove(wrapper);
    }
}
