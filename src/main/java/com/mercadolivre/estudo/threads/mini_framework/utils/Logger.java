package com.mercadolivre.estudo.threads.mini_framework.utils;

public class Logger {

    public static void log(String message, String component) {
        System.out.println(Thread.currentThread().getName() + "["+Thread.currentThread().getId()+"] ["+component+"] - " + message);
    }

}
