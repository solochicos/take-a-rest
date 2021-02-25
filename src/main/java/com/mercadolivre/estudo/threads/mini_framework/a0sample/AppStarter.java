package com.mercadolivre.estudo.threads.mini_framework.a0sample;

import com.mercadolivre.estudo.threads.mini_framework.core.FrameworkRunner;
import com.mercadolivre.estudo.threads.mini_framework.utils.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

public class AppStarter {

    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException {
        Logger.log("Starting", "Framework Runner");
        FrameworkRunner starter = new FrameworkRunner();
        starter.execute();
        Logger.log("Shutting down", "Framework Runner");
    }

}
