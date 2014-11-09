package com.chao.dcme.handler;

import com.chao.dcme.MainFrame;

import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-09 16:52.
 * Package: com.chao.dcme.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public interface Handler {
    public void execute(Map<String, Object> map, MainFrame frame);
}
