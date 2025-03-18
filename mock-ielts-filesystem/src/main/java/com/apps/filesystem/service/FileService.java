package com.apps.filesystem.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {
    /**
     * 上传文件
     * @param file 文件
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file) throws IOException;

    /**
     * 删除文件
     * @param fileUrl 文件URL
     */
    void deleteFile(String fileUrl);

    /**
     * 获取文件访问URL
     * @param filePath 文件路径
     * @return 文件访问URL
     */
    String getFileUrl(String filePath);
} 