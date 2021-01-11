package com.group6.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.group6.entity.Document;
import com.group6.entity.User;
import com.group6.mapper.DocumentMapper;
import com.group6.mapper.UserMapper;
import com.group6.service.DocumentService;
import com.group6.service.OBSService;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author weirwei 2021/1/9 19:43
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Resource
    private OBSService obsService;
    @Resource
    private DocumentMapper documentMapper;
    @Resource
    private UserMapper userMapper;


    @Override
    public List<Document> getDocumentList(Pageable pageable, String uid, String search) {
        Page<Document> documentPage = new Page<>(pageable.getPageNumber(), pageable.getPageSize());
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        if (!uid.isEmpty()) {
            documentQueryWrapper.eq("uid", uid);
        }
        documentQueryWrapper.eq("doc_status", 0)
                .like("doc_name", search);
        IPage<Document> documentIPage = documentMapper.selectPage(documentPage, documentQueryWrapper);
        return documentIPage.getRecords();
    }

    @Override
    public PutObjectResult putFile(String uid, String objectKey, File file, String docDescribe) throws BusinessException {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("uid", uid));
        if (user == null || user.getStatus() != User.NORMAL) {
            throw new BusinessException(EmBusinessError.USER_LACK_OF_PERMISSION, "用户不存在或无权限");
        }
        Document document = new Document(uid, objectKey, file, docDescribe);
        documentMapper.insert(document);
        return obsService.put(objectKey, file);
    }

    @Override
    public ObsObject getFile(String uid, String objectKey) throws BusinessException, IOException {
        Document document = documentMapper.selectOne(new QueryWrapper<Document>().eq("uid", uid)
                .eq("doc_path", objectKey));
        if (document == null) {
            throw new BusinessException(EmBusinessError.USER_LACK_OF_PERMISSION, "用户无权限或资源不存在");
        }
        return obsService.get(objectKey);
    }
}
