package com.mercadolivre.estudo.threads.mini_framework.core;

import com.mercadolivre.estudo.threads.mini_framework.async.AsyncManager;
import com.mercadolivre.estudo.threads.mini_framework.utils.Logger;
import com.sun.net.httpserver.HttpServer;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PooledHttpServer {

    HttpServer http;
    private AsyncManager manager;
    int port;

    public PooledHttpServer(AsyncManager manager) {
        this(8080, manager);
        this.manager = manager;
    }

    public PooledHttpServer(int port, AsyncManager manager) {
        this.manager = manager;
        this.port = port;
    }

    public void create(List<FrameworkRunner.Controller> controllers) throws IOException {
        this.http = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
        this.http.setExecutor(manager.getExecutorFor(AsyncManager.AsyncPool.REQUEST_HANDLING));

        controllers.forEach(controller -> {
            this.http.createContext(controller.uri, (httpExchange) -> {
                String[] queryParams = new String[0];

                if("GET".equals(httpExchange.getRequestMethod()) && "GET".equalsIgnoreCase(controller.httpMethod)) {
                    String[] uriBlocks = httpExchange.getRequestURI().toString().split("\\?");
                    if(uriBlocks.length > 1) {
                        queryParams = uriBlocks[1].split("&");
                    }

                    String response = "";
                    try {
                        Object classInstance = controller.javaClass.getDeclaredConstructor(AsyncManager.class).newInstance(manager);

                        HashMap<String, String> queryParamsMap = new HashMap<>();
                        Arrays.stream(queryParams).forEach(queryParam -> {
                            String[] queryParamTuple = queryParam.split("=");
                            queryParamsMap.put(queryParamTuple[0], queryParamTuple[1]);
                        });
                        response = (String) controller.method.invoke(classInstance, queryParamsMap);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }

                    OutputStream outputStream = httpExchange.getResponseBody();
                    httpExchange.getResponseHeaders().add("content-type", "application/json");
                    httpExchange.sendResponseHeaders(200, response.length());
                    outputStream.write(response.getBytes());
                    outputStream.flush();
                    outputStream.close();
                } else {
                    throw new NotImplementedException();
                }
            });
        });
    }

    public void start() throws IOException {
        Logger.log("Starting HTTP Server on 0.0.0.0:" + port, "Pooled Http Server");
        this.http.start();
        Logger.log("Http server started", "Pooled Http Server");
    }


    public void stop() {
        this.http.stop(0);
        Logger.log("Stopping http server", "Pooled Http Server");
    }
}
