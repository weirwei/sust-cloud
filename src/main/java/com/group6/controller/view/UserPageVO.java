package com.group6.controller.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author weirwei 2021/1/12 14:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPageVO {
    private List<UserVO> userVOList;
    private long total;

}
