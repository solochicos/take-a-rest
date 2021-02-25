package com.mercadolivre.estudo.threads.mini_framework.core;


import com.mercadolivre.estudo.threads.mini_framework.async.AsyncManager;
import com.mercadolivre.estudo.threads.mini_framework.utils.AnnotationUtils;
import com.mercadolivre.estudo.threads.mini_framework.utils.ClassUtils;
import com.mercadolivre.estudo.threads.mini_framework.utils.Logger;

import com.mercadolivre.estudo.threads.mini_framework.utils.RequestMethod;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.*;

public class FrameworkRunner {

    private AsyncManager manager = new AsyncManager(1, 2);

    public void execute() throws IOException, URISyntaxException, ClassNotFoundException {
        showBanner();

        List<Controller> controllers = configureEndpoints();
        PooledHttpServer httpServer = startHttpServer(controllers);

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Type something and press enter to stop server");
        keyboard.hasNext();

        stopAllResources(httpServer);
    }

    private List<Controller> configureEndpoints() throws URISyntaxException, IOException, ClassNotFoundException {
        Logger.log("Loading endpoints from package: com.mercadolivre.estudo.threads.mini_framework", "Framework Runner");
        Iterable<Class> controllerClasses = ClassUtils.getClasses("com.mercadolivre.estudo.threads.mini_framework");


        List<Controller> controllers = new ArrayList<>();

        controllerClasses.forEach(controllerClass -> {
            List<Method> annotatedMethods = AnnotationUtils.findAnnotatedMethods(controllerClass, RestController.class);

            annotatedMethods.forEach(annotatedMethod -> {
                String httpMethod = annotatedMethod.getAnnotation(RestController.class).method().toString();
                String httpUri = annotatedMethod.getAnnotation(RestController.class).path();

                controllers.add(new Controller(httpMethod, httpUri, controllerClass, annotatedMethod));
            });
        });

        controllers.forEach(controller -> {
            Logger.log("Loaded endpoint ["+controller+"]", "Framework Runner");
        });

        return controllers;
    }

    private PooledHttpServer startHttpServer(List<Controller> controllers) throws IOException {
        PooledHttpServer httpServer = new PooledHttpServer(8080, manager);

        httpServer.create(controllers);
        httpServer.start();

        return httpServer;
    }

    private void stopAllResources(PooledHttpServer httpServer) {
        httpServer.stop();
        manager.shutdown();
    }

    public class Controller {

        public Controller(String httpMethod, String uri, Class javaClass, Method method) {
            this.httpMethod = httpMethod;
            this.uri = uri;
            this.javaClass = javaClass;
            this.method = method;
        }

        String httpMethod;
        String uri;
        Class javaClass;
        Method method;

        @Override
        public String toString() {
            return "Controller{" +
                    "httpMethod='" + httpMethod + '\'' +
                    ", uri='" + uri + '\'' +
                    ", javaClass=" + javaClass +
                    ", method=" + method +
                    '}';
        }
    }

    private void showBanner() {
        System.out.println("\n");
        System.out.println("d888888P          dP                          888888ba                      dP");
        System.out.println("   88             88                          88    `8b                     88");
        System.out.println("   88    .d8888b. 88  .dP  .d8888b. .d8888b. a88aaaa8P' .d8888b. .d8888b. d8888P ");
        System.out.println("   88    88'  `88 88888\"   88ooood8 88'  `88  88   `8b. 88ooood8 Y8ooooo.   88");
        System.out.println("   88    88.  .88 88  `8b. 88.  ... 88.  .88  88     88 88.  ...       88   88");
        System.out.println("   dP    `88888P8 dP   `YP `88888P' `88888P8  dP     dP `88888P' `88888P'   dP   ");
        System.out.println("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        System.out.println("\n");
    }

}
