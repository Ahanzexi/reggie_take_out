package com.zzx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzx.common.R;
import com.zzx.entity.Orders;
import com.zzx.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * 用户下单
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){
        orderService.submit(order);
        return R.success("下单成功");
    }
    /*http://localhost:8080/order/userPage?page=1&pageSize=5*/
    @GetMapping("/userPage")
    public R<Page> page(Integer page, Integer pageSize, HttpSession session){
        final Page pageInfo = new Page(page, pageSize);
        final LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId,session.getAttribute("user"));
        wrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }
}
