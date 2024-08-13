package com.example.travel.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileUtils {
    public static String generateFileName(String originalFilename) {
        // 获取当前时间，并格式化为年月日时分秒的字符串
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = now.format(formatter);
        // 获取文件后缀名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 组合新的文件名
        return timestamp + extension;
    }
    public static String upload(MultipartFile file,String uploadPath) throws IOException {
        String path1 = ResourceUtils.getURL("classpath:").getPath();
        String originalFilename = file.getOriginalFilename();
        String newFileName = generateFileName(originalFilename);
        File dest = new File(path1+"static/"+uploadPath + newFileName);
        file.transferTo(dest);
        return uploadPath + newFileName;
    }

}
