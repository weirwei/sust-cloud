package com.group6.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文件分享
 * </p>
 *
 * @author xigua
 * @since 2021-01-08
 */
@Data
public class Share{

    private static final long serialVersionUID=1L;

    /**
     * 分享id
     */
      @TableId(value = "share_id", type = IdType.AUTO)
    private Integer shareId;

    /**
     * 文件id
     */
    private Integer docId;

    /**
     * 分享链接
     */
    private String shareLink;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 失效时间
     */
    private LocalDateTime invalidTime;


}
