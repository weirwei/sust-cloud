package com.group6.service;

import com.group6.Application;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectResult;
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
    public void getTest() throws IOException {
        obsService.get("new/test.txt");
    }

    @Test
    public void putTest() {
        PutObjectResult put = obsService.put("ne/test.txt", new File("D://test.txt"));
        System.out.println(put);
    }

    @Test
    public void getAllFileInfoTest() throws IOException {
        List<ObsObject> allFileInfo = obsService.getAllFileInfo();
        System.out.println(allFileInfo);
    }

    @Test
    public void shareTest() throws IOException {
        String preview = obsService.share("weirwei/test/《简存取云盘》数据字典.doc", 300);
        System.out.println(preview);
    }
}
