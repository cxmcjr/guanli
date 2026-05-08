package com.example.service;

import com.example.dao.UploadFileDao;
import com.example.entity.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    private final UploadFileDao uploadFileDao;

    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    public FileService(UploadFileDao uploadFileDao) {
        this.uploadFileDao = uploadFileDao;
    }

    public UploadFile upload(MultipartFile file, String category, String relatedType, Integer relatedId) throws IOException {
        Path uploadPath = Paths.get(uploadDir, category);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }
        String savedName = UUID.randomUUID().toString() + ext;
        Path filePath = uploadPath.resolve(savedName);
        file.transferTo(filePath.toFile());

        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileName(savedName);
        uploadFile.setOriginalName(originalName);
        uploadFile.setFilePath(filePath.toString());
        uploadFile.setFileSize(file.getSize());
        uploadFile.setFileType(ext.replace(".", ""));
        uploadFile.setCategory(category);
        uploadFile.setRelatedType(relatedType);
        uploadFile.setRelatedId(relatedId);
        uploadFileDao.add(uploadFile);

        return uploadFile;
    }

    public List<UploadFile> findAll() {
        return uploadFileDao.findAll();
    }

    public UploadFile findById(Integer id) {
        return uploadFileDao.findById(id);
    }

    public List<UploadFile> findByRelated(String relatedType, Integer relatedId) {
        return uploadFileDao.findByRelated(relatedType, relatedId);
    }

    public List<UploadFile> findByCategory(String category) {
        return uploadFileDao.findByCategory(category);
    }

    public boolean delete(Integer id) {
        UploadFile file = uploadFileDao.findById(id);
        if (file != null) {
            try {
                Files.deleteIfExists(Paths.get(file.getFilePath()));
            } catch (IOException ignored) {
            }
            uploadFileDao.delete(id);
            return true;
        }
        return false;
    }

    public File getFile(Integer id) {
        UploadFile uploadFile = uploadFileDao.findById(id);
        if (uploadFile != null) {
            File file = new File(uploadFile.getFilePath());
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }
}
