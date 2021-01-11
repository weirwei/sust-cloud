package com.group6.dao;

import com.group6.Application;
import com.group6.entity.Document;
import com.group6.mapper.DocumentMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author weirwei 2021/1/8 17:53
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@ContextConfiguration
public class DocumentMapperTest {

    @Resource
    private DocumentMapper documentMapper;

    @Test
    public void  test(){
        List<Document> list = documentMapper.selectList(null);
        System.out.println(list);
    }

}
