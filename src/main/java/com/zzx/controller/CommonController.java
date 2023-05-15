package com.zzx.controller;

import com.zzx.common.R;
import com.zzx.utils.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequestMapping("/common")
@RestController
public class CommonController {
    @Autowired
    private AliOSSUtils aliOSSUtils;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info("文件上传,{}",file);
        // 调用oss工具上传
        String url = aliOSSUtils.upload(file);
        log.info("文件上传完成,文件访问url为: {}",file);
        return R.success(url);
    }
}
