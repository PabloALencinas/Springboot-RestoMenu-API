package com.pabloagustin.myfood.project.backend.services.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public interface FileService {

	String uploadImage(String path, MultipartFile file) throws IOException;

}
