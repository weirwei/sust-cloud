package com.group6.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.io.File;
import java.time.LocalDateTime;

/**
 * <p>
 * 文件表
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Data
@NoArgsConstructor
public class Document {

    // 正常状态
    public static final int NORMAL = 0;
    // 回收站
    public static final int RECYCLING = 1;

    private static final long serialVersionUID = 1L;

    /**
     * 文件id
     */
    @TableId(type = IdType.AUTO)
    private Integer docId;

    /**
     * 文件名
     */
    private String docName;

    /**
     * 文件路径
     */
    private String docPath;

    /**
     * 文件状态
     * (0, 正常)
     * (1, 回收站)
     */
    private Integer docStatus;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime uploadTime;

    /**
     * 文件描述
     */
    private String docDescribe;

    /**
     * 进回收站时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deleteTime;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 文件类型
     */
    private String docType;

    /**
     * 文件大小 单位为 KB
     */
    private Long docSize;

    public Document(String uid, String docPath, Long fileLength, String docDescribe) {
        this.uid = uid;
        this.docName = docPath.substring(docPath.lastIndexOf("/") + 1);
        this.docPath = docPath;
        this.docDescribe = docDescribe;
        this.docSize = fileLength / 1024;
        this.uploadTime = LocalDateTime.now();
        this.docStatus = NORMAL;
        this.docType = "UNKNOW";
        String suffix = this.docName.substring(this.docName.lastIndexOf(".") + 1).toLowerCase();
        if (!StringUtils.isEmpty(suffix)) {
            this.docType = suffix;
        }
    }
}
