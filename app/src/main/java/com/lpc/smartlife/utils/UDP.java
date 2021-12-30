package com.lpc.smartlife.utils;

import android.util.Log;

import com.lpc.smartlife.message.CommunityMessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author byu_rself
 * @date 2021/12/31 0:57
 */
public class UDP implements CommunityInterface {
    public static UDP instance;
    private BlockingQueue<String> queue;
    private DatagramSocket s;
    private String ip;
    private int port;

    public static UDP getInstance(String ip, int port) {
        if (instance == null) {
            instance = new UDP(ip, port);
            instance.queue = new LinkedBlockingQueue<>();
            instance.receiveMessage();//开启接收监听
            instance.handleMessage();
        }
        return instance;
    }

    private UDP() {
    }

    private UDP(String ip, int port) {
        try {
            if (s != null) {
                s.close();
                s = null;
            }
            if (s == null) {
                s = new DatagramSocket(null);
                s.setReuseAddress(true);
                s.bind(new InetSocketAddress(port));
            }
            this.ip = ip;
            this.port = port;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    byte[] messageBytes = new byte[100];
                    DatagramPacket packet = new DatagramPacket(messageBytes, 100);
                    try {
                        if (instance.s != null) {
                            instance.s.receive(packet);
                            Log.v("msg", new String(packet.getData()).trim());
                            queue.put(new String(packet.getData()).trim());
                        }
                        Thread.sleep(100);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void handleMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = queue.take();
                        CommunityMessageEvent msg = new CommunityMessageEvent();
                        msg.setMessage(message);
                        msg.setCode(CommunityMessageEvent.UDPDataMessage);
                        EventBus.getDefault().post(msg);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
