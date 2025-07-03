package com.gym.gym_ver2.aplicaction.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, String> uploadImage(MultipartFile file, String carpeta) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }
        Map<?, ?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", carpeta
        ));

        return Map.of(
                "url", uploadResult.get("secure_url").toString(),
                "public_id", uploadResult.get("public_id").toString()
        );
    }

    public Map deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public void deleteImage(String imagePublicId) {
        try {
            cloudinary.uploader().destroy(imagePublicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen: " + e.getMessage());
        }
    }
}

