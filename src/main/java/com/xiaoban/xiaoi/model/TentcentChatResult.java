package com.xiaoban.xiaoi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiaoban
 * @version 1.0.0
 * @create 2021/04/13 - 11:10
 */
@NoArgsConstructor
@Data
public class TentcentChatResult <T> {

    @JsonProperty("ret")
    private Integer ret;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private T data;

}
