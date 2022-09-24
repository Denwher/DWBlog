package com.dengwei.service;

import com.dengwei.domain.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Denwher
 * @version 1.0
 */
public interface UploadService {
    ResponseResult uploadImg(MultipartFile img);

}
