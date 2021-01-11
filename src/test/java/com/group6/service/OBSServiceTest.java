package com.group6.service;

import com.group6.Application;
import com.obs.services.model.DeleteObjectResult;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author weirwei 2021/1/8 23:19
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class OBSServiceTest {

    @Resource
    private OBSService obsService;


    @Test
    public void putTest() {
        PutObjectResult put = obsService.put("weirwei/test/test.jpg", new File("src/test/resources/static/test.txt"));
        assert StringUtils.equals(put.getObjectKey(), "weirwei/test/test.jpg");
    }

    @Test
    public void getTest() throws IOException {
        ObsObject obsObject = obsService.get("weirwei/test/test.jpg");
        assert StringUtils.equals(obsObject.getObjectKey(), "weirwei/test/test.jpg");
    }

    @Test
    public void getAllFileInfoTest() throws IOException {
        List<ObsObject> allFileInfo = obsService.getAllFileInfo();
        assert !allFileInfo.isEmpty();
    }

    @Test
    public void shareTest() throws IOException {
        String preview = obsService.share("weirwei/test/test.jpg", 300);
        assert !StringUtils.isEmpty(preview);
    }

    @Test
    public void deleteTest() {
        DeleteObjectResult delete = obsService.delete("weirwei/test/test.jpg");
        assert StringUtils.equals(delete.getObjectKey(), "weirwei/test/test.jpg");
    }
}
