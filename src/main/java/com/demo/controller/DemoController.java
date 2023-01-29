package com.demo.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class DemoController {

    private volatile boolean stopThread = false;
    private volatile int permanentThreadCount = 0;
    private volatile int sleepThreadCount = 0;
    List<Object> objectList = new ArrayList<>();

    @RequestMapping("/demo/request")
    public Object demoRequest(@RequestParam("operation") String operation){

        String methodName = "demoRequest";
        log.info("[{}] receive request", methodName);

        switch (operation){
            case "createObj":{
                ArrayList<Object> list = new ArrayList<>();
                for (int i = 0; i < 100000; i++) {
                    list.add(new Object());
                }
                objectList.add(list);
                break;
            }
            case "clearList":{
                objectList.clear();
                break;
            }
            case "sleep":{
                new Thread(()->{
                    log.info("[{}] sleep for 10 sec...", Thread.currentThread().getName());
                    try {
                        Thread.sleep(10*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                },"SleepThread"+sleepThreadCount++).start();
                break;
            }
            case "runPermanentThread":{
                stopThread = false;
                new Thread(()->{
                    while (!stopThread) {
                        log.info("[{}] running...", Thread.currentThread().getName());
                    }
                }, "PermanentThread"+permanentThreadCount++).start();
                break;
            }
            case "stopPermanentThread":{
                stopThread = true;
                permanentThreadCount = 0;
                break;
            }

        }
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("result",operation + " success.");
        return resultMap;
    }


}
