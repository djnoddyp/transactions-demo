package com.pnodder.demo.listeners;

import javax.jms.Message;

public class DemoListener implements javax.jms.MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("got message");
    }
}
