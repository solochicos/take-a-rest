package com.mercadolivre.estudo.threads.mini_framework.entity;

import com.sun.istack.internal.NotNull;
import java.util.Random;

public class Message {

  private Integer id = new Random().nextInt(10000);

  @NotNull
  private String msg;

  public Integer getId() {
    return id;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
