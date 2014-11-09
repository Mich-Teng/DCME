package com.chao.dcme.util;

import com.chao.dcme.exception.FileNotExistException;
import com.chao.dcme.local.LocalInfo;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2014-11-02 20:30.
 * Package: com.chao.dcme.util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Utilities {
    public static String readFile(String filePath) throws FileNotExistException{
        File file = new File(filePath);
        StringBuilder builder = new StringBuilder();
        if(!file.exists()) {
            throw new FileNotExistException(filePath);
        }
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String tmp = buffer.readLine();
            while(tmp != null) {
                builder.append(tmp);
                tmp = buffer.readLine();
                if(tmp != null)
                    builder.append('\n');
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static void writeFile(String path, String content) {
        File file = new File(path);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLocalIPAddr() throws UnknownHostException{
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println(addr);
        return addr.getHostAddress();
    }

    public static String getLocalHostname() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName();
    }

    public static void send(byte[] content, String ip, int port) {
        try {
            DatagramSocket socket = LocalInfo.getSocket();
            DatagramPacket sendPacket = new DatagramPacket(content, content.length,
                    InetAddress.getByName(ip), port);
            socket.send(sendPacket);
        }catch (UnknownHostException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }

    public static int getRandomNumber(int range) {
        return  (int)(Math.random()*range);
    }

}
