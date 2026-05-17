package com.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", folder,  // Thư mục trên Cloudinary (vd: "projects", "avatars")
                    "overwrite", true,
                    "resource_type", "image"
            );

            Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), uploadParams);
            return uploadResult.get("secure_url").toString();  // URL an toàn (https)

        } catch (IOException e) {
            throw new RuntimeException("Lỗi upload ảnh: " + e.getMessage());
        }
    }

    public String uploadImage(MultipartFile file) {
        return uploadImage(file, "portfolio");
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Lỗi xóa ảnh: " + e.getMessage());
        }
    }

    public String getOptimizedUrl(String url, int width, int height) {
        // Tạo URL resize ảnh
        return url.replace("/upload/", "/upload/w_" + width + ",h_" + height + ",c_fill/");
    }
}