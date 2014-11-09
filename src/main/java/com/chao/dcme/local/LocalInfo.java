package com.chao.dcme.local;

import com.chao.dcme.exception.ExitCode;
import com.chao.dcme.util.Utilities;

import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-02 20:53.
 * Package: com.chao.dcme.local
 * Description: store all the information about local server and
 * local storage
 * <p/>
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class LocalInfo {
    private static PeerStatus peerStatus = PeerStatus.UNKOWN;
    private static String localIp = null;
    private static int seqNo = 0;
    private static String localIdentifier = null;
    private static Set<String> inviteeCandidate = new HashSet<String>();
    // identifier --> Peer
    private static Map<String, Peer> peers = new HashMap<String, Peer>();
    // default local port
    private static int localPort = 12345;
    private static DatagramSocket socket = null;

    public LocalInfo() {
        try {
            localIp = Utilities.getLocalIPAddr();
            localIdentifier = Utilities.getLocalHostname() + ":" + Utilities.getRandomNumber(100000000);
            for (int i = Config.LOWER_BOUND_OF_PORT; i <= Config.UPPER_BOUND_OF_PORT; i++) {
                try {
                    socket = new DatagramSocket(i);
                    localPort = i;
                    break;
                } catch (SocketException exception) {
                }
            }
            if (socket == null)
                throw new SocketException();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(ExitCode.LOCALIP_NOT_FOUND);
        } catch (SocketException e) {
            System.out.println("All the ports have been bounded");
            System.exit(ExitCode.BINDING_SOCKET_ERROR);
        }
    }

    public static void setPeerStatus(PeerStatus status) {
        peerStatus = status;
    }

    public static PeerStatus getPeerStatus() {
        return peerStatus;
    }

    public static String getLocalIp() {
        return localIp;
    }

    public static void setLocalIp(String localIp) {
        LocalInfo.localIp = localIp;
    }

    public static int getSeqNo() {
        return seqNo;
    }

    public static void setSeqNo(int seqNo) {
        LocalInfo.seqNo = seqNo;
    }

    public static String getLocalIdentifier() {
        return localIdentifier;
    }

    public static void addInviteeCandidate(String ip, int port) {
        inviteeCandidate.add(ip + ":" + port);
    }

    public static void updateSeqNo() {
        seqNo++;
    }

    public static void addPeer(String identifier, Peer peer) {
        peers.put(identifier, peer);
    }

    public static Map<String, Peer> getPeers() {
        return peers;
    }

    public static void setPeers(Map<String, Peer> peers) {
        LocalInfo.peers = peers;
    }

    public static int getLocalPort() {
        return localPort;
    }

    public static void setLocalPort(int localPort) {
        LocalInfo.localPort = localPort;
    }

    public static DatagramSocket getSocket() {
        return socket;
    }

    public static void mergePeers(Map<String, Peer> map) {
        for (String key : map.keySet()) {
            if (!peers.containsKey(key)) {
                peers.put(key, map.get(key));
            }
        }
    }

    public static Set<String> getInviteeCandidate() {
        return inviteeCandidate;
    }
}
