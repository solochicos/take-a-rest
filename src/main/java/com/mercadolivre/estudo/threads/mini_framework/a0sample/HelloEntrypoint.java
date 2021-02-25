package com.mercadolivre.estudo.threads.mini_framework.a0sample;

import com.mercadolivre.estudo.threads.mini_framework.async.AsyncManager;
import com.mercadolivre.estudo.threads.mini_framework.core.RequestBody;
import com.mercadolivre.estudo.threads.mini_framework.core.RestController;
import com.mercadolivre.estudo.threads.mini_framework.utils.Logger;

import com.mercadolivre.estudo.threads.mini_framework.utils.RequestMethod;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HelloEntrypoint {

    AsyncManager asyncManager;

    public HelloEntrypoint(AsyncManager asyncManager) {
        this.asyncManager = asyncManager;
    }

    @RestController(path = "/hello", method = RequestMethod.GET)
    public String sayHello(HashMap<String, String> queryParams) {
        Logger.log("Request received on /hello with params ["+queryParams.keySet()+"]", "Strings Entrypoint");
        return "{ \"message\": \"Hello!\" }";
    }

    @RestController(path = "/multithread", method = RequestMethod.GET)
    public String sayMultiThread(HashMap<String, String> queryParams) throws ExecutionException, InterruptedException {
        Logger.log("Request received on /multithread with params ["+queryParams.keySet()+"]", "Strings Entrypoint");

        Future<?> multiFuture = asyncManager.execute(() -> {
            Thread.sleep(1000);
            return "Multi";
        });

        Future<?> threadFuture = asyncManager.execute(() -> {
            Thread.sleep(4000);
            return "Thread";
        });

        return "{ \"message\": \""+ multiFuture.get() + " " + threadFuture.get() +"\" }";
    }

    @RestController(path = "/params", method = RequestMethod.GET)
    public String sayMyParams(HashMap<String, String> queryParams) {
        Logger.log("Request received on /params with params ["+queryParams.keySet()+"]", "Strings Entrypoint");
        StringBuilder response = new StringBuilder();

        response.append("[\n");

        queryParams.keySet().forEach(key -> {
            response.append("\""+key+"\": \""+ queryParams.get(key) +"\",\n");
        });

        response.append("]");
        return response.toString();
    }

    @RestController(path = "/saveAny", method = RequestMethod.POST)
    public String saveAny(@RequestBody String data) {
        Logger.log("Request received on /saveAny with requestBody: " + data, "Strings Entrypoint");
        return "{ \"data\": \"" + data + "\" }";
    }

}
