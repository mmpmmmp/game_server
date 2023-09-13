package com.wre.game.api.service.impl;

import com.wre.game.api.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String execute() {
        try {
            Thread.sleep(5000);
            logger.info("Slow task executed, thread name-> ", Thread.currentThread().getName());
            return "Task finished";
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @Async
    public String testAsync(String task) {
        try {
            Thread.sleep(5000);
            logger.info("Slow task executed, testAsync-> ", task);
            return "Task finished";
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}