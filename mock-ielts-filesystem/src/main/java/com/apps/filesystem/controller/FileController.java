package com.apps.filesystem.controller;

import com.apps.common.ResponseResult;
import com.apps.filesystem.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${file.upload.base-dir}")
    private String baseDir;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public ResponseResult<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseResult.error("请选择要上传的文件");
        }
        try {
            String fileUrl = fileService.uploadFile(file);
            return ResponseResult.success("文件上传成功", fileUrl);
        } catch (IllegalArgumentException e) {
            return ResponseResult.error(e.getMessage());
        } catch (IOException e) {
            return ResponseResult.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 删除文件
     * @param fileName
     * @return
     */
    @DeleteMapping("/{fileName}")
    public ResponseResult<Void> deleteFile(@PathVariable String fileName) {
        try {
            fileService.deleteFile(fileName);
            return ResponseResult.success("文件删除成功",null);
        } catch (Exception e) {
            return ResponseResult.error("文件删除失败: " + e.getMessage());
        }
    }

    /**
     * 下载文件
     * @param fileName
     * @return
     */
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(baseDir).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 在线查看文件
     * @param fileName
     * @return
     */
    @GetMapping("/stream/{fileName}")
    public ResponseEntity<Resource> streamFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(baseDir).resolve(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}

