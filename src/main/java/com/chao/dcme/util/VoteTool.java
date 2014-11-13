package com.chao.dcme.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-08 16:23.
 * Package: com.chao.dcme.util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class VoteTool {
    private static Map<String, Boolean> votes = new HashMap<String, Boolean>();

    public static void refresh(Set<String> set) {
        votes.clear();
        for (String str : set) {
            votes.put(str, false);
        }
    }

    /**
     * vote from a key and check whether the vote is complete
     *
     * @param key key
     * @return true if votes are complete
     */
    public static void vote(String key) {
        if (votes.containsKey(key))
            votes.put(key, true);
    }

    public static boolean isComplete() {
        Collection<Boolean> ret = votes.values();
        return ret.isEmpty() || !ret.contains(false);
    }
}
