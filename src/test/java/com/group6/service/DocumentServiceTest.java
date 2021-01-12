package com.group6.service;

import com.fehead.lang.error.BusinessException;
import com.group6.Application;
import com.group6.controller.view.DocumentPageVO;
import com.group6.entity.Document;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weirwei 2021/1/11 20:24
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class DocumentServiceTest {

    @Resource
    private DocumentService documentService;

    private final Pageable pageable = new PageRequest(1, 6);
    private final String uid = "201706020228";
    private final String objectKey = "weirwei/test/test.txt";

    @Test
    public void putFileTest() throws BusinessException {
        String docDescribe = "";
        File file = new File("src/test/resources/static/test.txt");
        PutObjectResult putObjectResult = documentService.putFile(uid, objectKey, file, docDescribe);
        assert StringUtils.equals(putObjectResult.getObjectKey(), objectKey);
    }

    @Test
    public void getDocumentListTest() {
        String search = "";
        int target = 2;
        DocumentPageVO documentPageVO = documentService.getDocumentList(pageable, uid, search, target);
        assert documentPageVO != null;
    }


    @Test
    public void getFileTest() throws BusinessException, IOException {
        ObsObject obsObject = documentService.getFile(uid, objectKey);
        assert StringUtils.equals(obsObject.getObjectKey(), objectKey);
    }

    @Test
    public void shareTest() throws BusinessException, IOException {
        String share = documentService.share(uid, objectKey, 300);
        assert !StringUtils.isEmpty(share);
    }

    @Test
    public void deleteFileTest() throws BusinessException {
        List<String> objectKeyList = new ArrayList<>();
        objectKeyList.add(objectKey);
        documentService.deleteFile(uid, objectKeyList);
    }

    @Test
    public void recoverFileTest() throws BusinessException {
        List<String> objectKeyList = new ArrayList<>();
        objectKeyList.add(objectKey);
        documentService.recoverFile(uid, objectKeyList);
    }

    @Test
    public void emptyBinTest() {
        documentService.emptyBin(uid);
    }


}
