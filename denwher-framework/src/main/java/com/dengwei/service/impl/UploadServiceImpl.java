package com.dengwei.service.impl;

import com.dengwei.domain.ResponseResult;
import com.dengwei.domain.enums.AppHttpCodeEnum;
import com.dengwei.exception.SystemException;
import com.dengwei.service.UploadService;
import com.dengwei.util.PathUtils;
import com.dengwei.util.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Denwher
 * @version 1.0
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private QiniuUtils qiniuUtils;

    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断img的类型
        String originalFilename = img.getOriginalFilename();
        if(!(originalFilename.endsWith(".jpg")||
                originalFilename.endsWith(".png")||
                originalFilename.endsWith(".jpeg"))){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        //如果类型正确，就上传文件到oss
        String filePath = PathUtils.generateFilePath(originalFilename);
        String url = qiniuUtils.uploadOss(img, filePath);
        return ResponseResult.okResult(url);
    }
}
