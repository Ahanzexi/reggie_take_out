package com.zzx.controller;

import com.zzx.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequestMapping("/common")
@RestController
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        // 获取原始文件名的后缀
        final String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 创建目录
        File dir  = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        // 生产新的文件名
        String newFilename = UUID.randomUUID().toString()+suffix;
        System.out.println(newFilename);
        file.transferTo(new File(basePath+newFilename));
        return R.success(newFilename);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        // 输入流,通过输入流读取文件内容
        final FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        System.out.println(basePath + name);
        // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片了
        response.setContentType("image/jpeg");
        final ServletOutputStream outputStream = response.getOutputStream();
        final byte[] bytes = new byte[1024];
        int len = 0;
        while((len = fileInputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }
}
