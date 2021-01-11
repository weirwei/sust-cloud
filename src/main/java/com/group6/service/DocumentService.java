package com.group6.service;

import com.fehead.lang.error.BusinessException;
import com.group6.entity.Document;
import com.obs.services.model.DeleteObjectResult;
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

    List<Document> getDocumentList(Pageable pageable, String uid, String search);

    PutObjectResult putFile(String uid, String objectKey, File file, String docDescribe) throws BusinessException;

    ObsObject getFile(String uid, String objectKey) throws BusinessException, IOException;

    void deleteFile(String uid, String objectKey) throws BusinessException;

    void recoverFile(String uid, String objectKey) throws BusinessException;

    void emptyBin(String uid);

    String share(String uid, String objectKey, long expires) throws BusinessException, IOException;
}
