package com.group6.controller.view;

import com.group6.entity.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weirwei 2021/1/12 14:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentPageVO {
    private List<Document> documentList;
    private long total;
}
