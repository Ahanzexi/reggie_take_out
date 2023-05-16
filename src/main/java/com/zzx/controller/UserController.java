package com.zzx.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzx.common.R;
import com.zzx.entity.User;
import com.zzx.service.UserService;
import com.zzx.utils.SMSUtils;
import com.zzx.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        // 获取手机号
        String phone = user.getPhone();
        //生产随机的四位验证码
        final String code = ValidateCodeUtils.generateValidateCode(4).toString();
        //调用阿里云的SMS的API发送短信
        SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
        //保存验证码到Session
        session.setAttribute(phone,code);
        return R.success("手机验证码短信发送成功!");
    }

    /**
     * 移动端登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        // 获取手机号
        final String phone = map.get("phone").toString();
        // 获取验证码
        final String code = map.get("code").toString();
        // 从Session获取验证码
        final Object codeInSession = session.getAttribute(phone);
        // 进行验证码的比对(页面提交的验证码和Session中保存的验证码对比)
        if(codeInSession!=null&&codeInSession.equals(code)){
            // 如果对比成功,登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if (user==null){
                // 如果是新用户,自动注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            return R.success(user);
        }
        return R.error("登录失败!");
    }

}
