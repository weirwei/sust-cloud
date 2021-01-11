package com.group6.service;

import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author weirwei 2021/1/8 22:54
 */
public interface OBSService {


    /**
     * 上传文件
     *
     * @param objectKey 相对于存储桶的对象路径，如 test/test.txt
     * @param file      为待上传的文件
     * @return
     */
    PutObjectResult put(String objectKey, File file);

    /**
     * 下载文件
     *
     * @param objectKey 相对于存储桶的对象路径，如 test/test.txt
     * @return
     * @throws IOException IOException
     */
    ObsObject get(String objectKey) throws IOException;

    List<ObsObject> getAllFileInfo() throws IOException;

    String share(String objectKey, long expires) throws IOException;

    DeleteObjectResult delete(String objectName);
}
