package com.wre.game.api.controller.api;

import com.wre.game.api.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@RestController
public class AsyncCallableController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TaskService taskService;

    @Autowired
    public AsyncCallableController(TaskService taskService) {
        this.taskService = taskService;
    }

    @RequestMapping(value = "/callable1", method = RequestMethod.GET, produces = "text/html")
    public Callable<String> executeSlowTask() {
        logger.info("Request received");
        Callable<String> callable = taskService::execute;
        logger.info("Servlet thread released");

        return callable;
    }

    @RequestMapping(value = "/callable2", method = RequestMethod.GET, produces = "text/html")
    public Callable<String> executeSlowTask2() {
        logger.info("Request received");
        final String x="testss";
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return taskService.testAsync(x);
            }
        };

        logger.info("Servlet thread released");

        return callable;
    }
    @RequestMapping("/callable")
    public Callable<String> forCallable(Model model) throws Exception {
        return () -> {
            TimeUnit.SECONDS.sleep(1);//睡眠1秒，模仿某些业务操作
            model.addAttribute("a", "aaaaaaa");
            return "async_request_callable";
        };
    }
    @RequestMapping("/callable/timeout")
    public WebAsyncTask<String> forCallableWithTimeout(Model model) throws Exception {
        long timeout = 5 * 1000L;
        WebAsyncTask<String> asyncTask = new WebAsyncTask<>(timeout, () -> {
            TimeUnit.MILLISECONDS.sleep(timeout + 1000);
            model.addAttribute("a", "aaaaaaa");
            return "async_request_callable";
        });
        asyncTask.onTimeout(() -> {
            System.out.println("响应超时回调");
            return "async_request_callable_timeout";
        });
        asyncTask.onCompletion(() -> {
            System.out.println("响应callable调用完成的回调");
        });
        return asyncTask;
    }

    @RequestMapping("/deferredresult")
    public DeferredResult<String> forDeferredResult() throws Exception {
        DeferredResult<String> result = new DeferredResult<>();
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.setResult("async_request_deferredresult");
        }).start();
        return result;
    }

    @RequestMapping("/deferredresult/timeout")
    public DeferredResult<String> forDeferredResultWithTimeout() throws Exception {
        DeferredResult<String> result = new DeferredResult<>(5 * 1000l);
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(31);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            result.setResult("async_request_deferredresult");
        }).start();

        result.onTimeout(() -> {
            System.out.println("响应超时回调函数");
        });

        result.onCompletion(() -> {
            System.out.println("响应完成的回调函数");
        });

        return result;
    }

}