package com.group6.service.impl;

import com.group6.service.OBSService;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author weirwei 2021/1/8 22:54
 */
@Service
public class OBSServiceImpl implements OBSService {

    @Value("${obs.sust-cloud.endpoint}")
    private String endPoint;
    @Value("${obs.sust-cloud.ak}")
    private String ak;
    @Value("${obs.sust-cloud.sk}")
    private String sk;
    @Value("${obs.sust-cloud.bucketname}")
    private String bucketName;


    /**
     * 创建 ObsClient 实例
     */
    private ObsClient obsClient = null;

    private ObsClient getInstance() {
        if (obsClient == null) {
            return new ObsClient(ak, sk, endPoint);
        }
        return obsClient;
    }


    @Override
    public PutObjectResult put(String objectKey, File file) {
        ObsClient obsClient = getInstance();
        return obsClient.putObject(bucketName, objectKey, file);
    }

    @Override
    public ObsObject get(String objectKey) {
        ObsClient obsClient = getInstance();
        boolean flag = obsClient.doesObjectExist(bucketName, objectKey);
        if (flag) {
            return obsClient.getObject(bucketName, objectKey);
        }
        return null;
    }

    @Override
    public List<ObsObject> getAllFileInfo() throws IOException {
        ObsClient obsClient = getInstance();
        ObjectListing objectList = obsClient.listObjects(bucketName);
        List<ObsObject> list = objectList.getObjects();
        obsClient.close();
        return list;
    }

    @Override
    public String preview(String objectKey) throws IOException {
        ObsClient obsClient = getInstance();
        // 300 有效时间
        TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, 300);
        request.setBucketName(bucketName);
        request.setObjectKey(objectKey);
        TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
        obsClient.close();
        return response.getSignedUrl();
    }
}
