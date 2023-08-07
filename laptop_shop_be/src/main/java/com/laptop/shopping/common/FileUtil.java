package com.laptop.shopping.common;

import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@Service
public class FileUtil {
    private final HttpServletRequest request;
    public FileUtil(HttpServletRequest request) {
        this.request = request;
    }
    public static void saveFile(String filePath, MultipartFile multipartFile) throws IOException {
        File file = new File(filePath);
        File folder = file.getParentFile();
        if (!folder.exists()) folder.mkdirs();
        FileCopyUtils.copy(multipartFile.getBytes(), new FileOutputStream(new File(filePath)));
    }
    public static String formatPathImg(String path) {
        return path.substring(path.indexOf("/assets"));
    }
}
