package com.zzx.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzx.common.R;
import com.zzx.entity.ShoppingCart;
import com.zzx.service.ShoppingCartService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 增加菜品或套餐进购物车
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        log.info("加入购物车 {}",shoppingCart);
        // 设置用户ID
        Long userID = (Long)session.getAttribute("user");
        shoppingCart.setUserId(userID);
        // 查询当前菜品或者套餐是否在购物车中
        final Long dishId = shoppingCart.getDishId();
        final LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userID);
        if(dishId!=null){
            // 添加到购物车的是菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            // 添加到购物车的是套餐
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        // 如果已经存在,数量加一
        ShoppingCart  one  = shoppingCartService.getOne(wrapper);
        if (one!=null){
            final Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
            // 添加到购物车,数量默认为 1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;

        }
        return R.success(one);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart,HttpSession session){
        log.info("减少数量 {}",shoppingCart);
        // 设置用户ID
        Long userID = (Long)session.getAttribute("user");
        shoppingCart.setUserId(userID);
        // 根据用户ID和菜品ID或者套餐ID确定数据
        final Long dishId = shoppingCart.getDishId();
        final LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userID);
        if(dishId!=null){
            // 添加到购物车的是菜品
            wrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            // 添加到购物车的是套餐
            wrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        final ShoppingCart one = shoppingCartService.getOne(wrapper);
        if(one.getNumber()-1==0){
            // 如果已经是最后一份.减少直接删除
            shoppingCartService.remove(wrapper);
            shoppingCart.setNumber(one.getNumber()-1);
            return R.success(one);
        }else{
            shoppingCart.setNumber(one.getNumber()-1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.update();
        }

        return R.success(one);
    }


    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public  R<List<ShoppingCart>> list(HttpSession session){
        log.info("查看购物车" );
        final LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,session.getAttribute("user"));
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        final List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clear(HttpSession session){
        final LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,session.getAttribute("user"));
        shoppingCartService.remove(wrapper);
        return R.success("清空购物车成功");
    }
}
