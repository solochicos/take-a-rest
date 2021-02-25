package com.mercadolivre.estudo.threads.mini_framework.core;

import com.mercadolivre.estudo.threads.mini_framework.utils.RequestMethod;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RestController {

    String path();
    RequestMethod method() default RequestMethod.GET;
}
