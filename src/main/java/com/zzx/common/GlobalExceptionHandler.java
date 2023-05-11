package com.zzx.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@Slf4j
@ResponseBody
@RestControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.error(ex.getMessage());
        // 判断是否是重复写入用户 Duplicate entry
        if(ex.getMessage().contains("Duplicate entry")){
            final String[] s = ex.getMessage().split(" ");
            String msg = s[2].replace("'", "")+"已存在!";
            return R.error(msg);
        }
        return R.error("未知错误!");
    }
}
