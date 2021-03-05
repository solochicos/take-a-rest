package com.mercadolivre.estudo.threads.mini_framework.entity;

import com.sun.istack.internal.NotNull;
public class Message {

  private Integer id;

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

  public void setId(Integer id) {
    this.id = id;
  }
}
