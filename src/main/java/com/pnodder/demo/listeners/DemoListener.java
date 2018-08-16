package com.pnodder.demo.listeners;

import org.springframework.jms.annotation.JmsListener;

public class DemoListener {

    @JmsListener(destination = "messageQueue1")
    public void onMessage(String message) {
        System.out.println("received: " + message);
    }
}
