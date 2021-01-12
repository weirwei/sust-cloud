package com.group6.service;

import com.fehead.lang.error.BusinessException;
import com.group6.controller.view.DocumentPageVO;
import com.group6.entity.Document;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author weirwei 2021/1/9 19:42
 */
public interface DocumentService {

    DocumentPageVO getDocumentList(Pageable pageable, String uid, String search, int target);

    PutObjectResult putFile(String uid, String objectKey, File file, String docDescribe) throws BusinessException;

    ObsObject getFile(String uid, String objectKey) throws BusinessException, IOException;

    void deleteFile(String uid, List<String> objectKeyList) throws BusinessException;

    void recoverFile(String uid, List<String> objectKeyList) throws BusinessException;

    void emptyBin(String uid);

    String share(String uid, String objectKey, long expires) throws BusinessException, IOException;
}
