package com.apps.filesystem.service.impl;

import com.apps.filesystem.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.base-dir}")
    private String baseDir;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    @Value("${file.upload.allowed-types}")
    private String allowedTypes;

    private final Tika tika = new Tika();

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // 验证文件类型
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String extension = FilenameUtils.getExtension(originalFilename).toLowerCase();
        if (!isAllowedFileType(extension)) {
            throw new IllegalArgumentException("不支持的文件类型: " + extension + "，支持的类型: " + allowedTypes);
        }

        // 验证文件大小（默认最大100MB）
        long maxSize = 100 * 1024 * 1024; // 100MB
        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("文件大小超过限制，最大支持100MB");
        }

        // 生成唯一文件名
        String fileName = UUID.randomUUID().toString() + "." + extension;
        
        // 确保上传目录存在
        Path uploadDir = Paths.get(baseDir);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 保存文件
        Path filePath = uploadDir.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        // 返回文件访问URL
        return getFileUrl(fileName);
    }

    @Override
    public void deleteFile(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path filePath = Paths.get(baseDir, fileName);
        try {
            if (!Files.exists(filePath)) {
                throw new IllegalArgumentException("文件不存在: " + fileName);
            }
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("删除文件失败: " + fileName, e);
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        return urlPrefix + "/" + filePath;
    }

    private boolean isAllowedFileType(String extension) {
        return Arrays.asList(allowedTypes.split(","))
                .contains(extension.toLowerCase());
    }
} 