package com.zzx.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMateObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpServletRequest request;

    /**
     * 插入时的操作
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充[insert]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long userId = (Long) request.getSession().getAttribute("employee");
        metaObject.setValue("createUser", userId==null?1:userId);
        metaObject.setValue("updateUser", userId==null?1:userId);
    }

    /**
     * 更新时的操作
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充[update]");
        log.info(metaObject.toString());
        Long userId = (Long) request.getSession().getAttribute("employee");
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", userId==null?1:userId);
    }
}
