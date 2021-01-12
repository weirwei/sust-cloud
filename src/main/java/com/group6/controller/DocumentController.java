package com.group6.controller;

import com.fehead.lang.controller.BaseController;
import com.fehead.lang.error.BusinessException;
import com.fehead.lang.error.EmBusinessError;
import com.fehead.lang.response.CommonReturnType;
import com.fehead.lang.response.FeheadResponse;
import com.group6.cookie.CookieUtil;
import com.group6.service.DocumentService;
import com.group6.util.FileUtils;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author weirwei 2021/1/8 16:44
 */
@RestController
@Controller
@RequestMapping("/doc")
@Api("文件系统相关接口")
@Slf4j
public class DocumentController extends BaseController {
    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource
    private DocumentService documentService;


    @GetMapping("/documentList")
    @ResponseBody
    @ApiOperation("获取所有文件信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNumber", value = "默认1"),
            @ApiImplicitParam(name = "pageSize", value = "默认6"),
            @ApiImplicitParam(name = "uid", value = "用户工号，默认为空，即查找所有"),
            @ApiImplicitParam(name = "search", value = "搜索，默认为空，即查找所有"),
            @ApiImplicitParam(name = "target", value = "搜索目标，0正常，1回收站，2全部", required = true)
    })
    public FeheadResponse getDocumentList(@PageableDefault(size = 6, page = 1) Pageable pageable,
                                          @RequestParam(value = "uid", defaultValue = "") String uid,
                                          @RequestParam(value = "search", defaultValue = "") String search,
                                          @RequestParam(value = "target", defaultValue = "0") int target) {
        log.info(PARAM + "uid: " + uid);
        log.info(PARAM + "search: " + search);
        log.info(PARAM + "target: " + target);
        return CommonReturnType.create(documentService.getDocumentList(pageable, uid, search, target));
    }


    @PostMapping("/file")
    @ResponseBody
    @ApiOperation("上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectKey", value = "文件上传的路径，如 new/test.txt", required = true),
            @ApiImplicitParam(name = "docDescribe", value = "选填，默认为空")
    })
    public FeheadResponse putFile(@RequestParam(value = "objectKey", defaultValue = "") String objectKey,
                                  @RequestParam(value = "uid") String uid,
                                  @RequestParam(value = "file") MultipartFile file,
                                  @RequestParam(value = "docDescribe", defaultValue = "") String docDescribe) throws Exception {
        log.info(PARAM + "uid: " + uid);
        log.info(PARAM + "file: " + file);
        log.info(PARAM + "docDescribe: " + docDescribe);
        objectKey += file.getOriginalFilename();
        if (StringUtils.isEmpty(objectKey)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传路径获取异常");
        }
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户id获取异常");
        }
        if (file == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "文件获取异常");
        }
        File toFile = FileUtils.multipartFileToFile(file);
        PutObjectResult putObjectResult = documentService.putFile(uid, objectKey, toFile, docDescribe);
        // multipartFileToFile 会在根目录生产一个副本，删除
        FileUtils.delteTempFile(toFile);
        return CommonReturnType.create(putObjectResult);
    }


    @GetMapping("/file")
    @ApiOperation("下载文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectKey", value = "文件相对cos中的路径，如 new/test.txt", required = true),
    })
    public void getFile(@RequestParam(value = "objectKey") String objectKey,
                        @RequestParam(value = "uid") String uid) throws IOException, BusinessException {
        log.info(PARAM + "objectKey: " + objectKey);
        log.info(PARAM + "uid: " + uid);
        if (StringUtils.isEmpty(objectKey)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传路径获取异常");
        }
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户id获取异常");
        }
        ObsObject obsObject = documentService.getFile(uid, objectKey);
        String fileName =
                new String(objectKey.substring(objectKey.lastIndexOf("/") + 1).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        InputStream inputStream = obsObject.getObjectContent();
        // 缓冲文件输出流
        BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/x-download");
        // 设置让浏览器弹出下载提示框，而不是直接在浏览器中打开
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        IOUtils.copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }


    @DeleteMapping("/file")
    @ResponseBody
    @ApiOperation("删除文件，放入回收站")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "备注", value = "输入文件在obs中的路径，文件相对cos中的路径，如 new/test.txt", readOnly = true),
    })
    public FeheadResponse deleteFile(@RequestParam(value = "objectKeyList[]") List<String> objectKeyList,
                                     @RequestParam(value = "uid") String uid) throws BusinessException {
        log.info(PARAM + "objectKeyList: " + objectKeyList);
        log.info(PARAM + "uid: " + uid);
        if (objectKeyList.size() == 0) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传路径获取异常");
        }
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户id获取异常");
        }
        documentService.deleteFile(uid, objectKeyList);
        return CommonReturnType.create(null);
    }


    @PutMapping("/file")
    @ResponseBody
    @ApiOperation("从回收站恢复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "备注", value = "输入文件在obs中的路径，文件相对cos中的路径，如 new/test.txt", readOnly = true),
    })
    public FeheadResponse recoverFile(@RequestParam(value = "objectKeyList[]") List<String> objectKeyList,
                                      @RequestParam(value = "uid") String uid) throws BusinessException {
        log.info(PARAM + "objectKeyList: " + objectKeyList);
        log.info(PARAM + "uid: " + uid);
        if (objectKeyList.size() == 0) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传路径获取异常");
        }
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户id获取异常");
        }
        documentService.recoverFile(uid, objectKeyList);
        return CommonReturnType.create(null);
    }


    @GetMapping("/share")
    @ResponseBody
    @ApiOperation("获得分享链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectKey", value = "文件相对cos中的路径，如 new/test.txt", required = true),
            @ApiImplicitParam(name = "expires", value = "链接超时时间，单位为秒，默认7个小时"),
    })
    public FeheadResponse getShareLink(@RequestParam(value = "objectKey") String objectKey,
                                       @RequestParam(value = "uid") String uid,
                                       @RequestParam(value = "expires", defaultValue = "7") long expires) throws BusinessException, IOException {
        log.info(PARAM + "objectKey: " + objectKey);
        log.info(PARAM + "uid: " + uid);
        log.info(PARAM + "expires: " + expires);
        if (StringUtils.isEmpty(objectKey)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "上传路径获取异常");
        }
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户id获取异常");
        }
        String shareLink = documentService.share(uid, objectKey, expires);
        return CommonReturnType.create(shareLink);
    }


    @DeleteMapping("/bin")
    @ResponseBody
    @ApiOperation("清空回收站")
    public FeheadResponse emptyBin(@RequestParam(value = "uid") String uid) throws BusinessException {
        log.info(PARAM + "uid: " + uid);
        if (StringUtils.isEmpty(uid)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR, "用户id获取异常");
        }
        documentService.emptyBin(uid);
        return CommonReturnType.create(null);
    }

}
