package com.chao.dcme.protocol;

import com.chao.dcme.local.LocalInfo;
import com.chao.dcme.local.Peer;
import com.chao.dcme.util.Utilities;

import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-04 21:11.
 * Package: com.chao.dcme.protocol
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class FloodProtocol {
    public static void floodMsg(byte[] msg) {
        Map<String, Peer> peers = LocalInfo.getPeers();
        for (Peer peer : peers.values())
            Utilities.send(msg, peer.getIp(), peer.getPort());
    }
}
