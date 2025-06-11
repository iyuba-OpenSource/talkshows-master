package com.iyuba.lib_common.bean;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @title:
 * @date: 2023/6/12 18:34
 * @author: liang_mu
 * @email: liang.mu.cn@gmail.com
 * @description:
 */
@Root(name = "response",strict = false)
public class Word_collect {

    @Element(required = false)
    public int result;
    @Element(required = false)
    public String word;
}
