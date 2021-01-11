package com.group6.service;

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
     *  @param objectKey 相对于存储桶的对象路径，如 test/test.txt
     * @param file 为待上传的文件
     * @return
     */
    public PutObjectResult put(String objectKey, File file);

    /**
     * 下载文件
     *
     * @param objectKey 相对于存储桶的对象路径，如 test/test.txt
     * @throws IOException IOException
     * @return
     */
    public ObsObject get(String objectKey) throws IOException;

    public List<ObsObject> getAllFileInfo() throws IOException;

    public String preview(String objectKey) throws IOException;
}
