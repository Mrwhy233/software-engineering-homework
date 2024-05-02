package com.example.vhr.http;

public class AjaxResult {
    private Integer status;
    private String msg;
    private Object obj;

    public static AjaxResult build() {
        return new AjaxResult();
    }

    public static AjaxResult ok(String msg) {
        return new AjaxResult(200, msg, null);
    }

    public static AjaxResult ok(String msg, Object obj) {
        return new AjaxResult(200, msg, obj);
    }

    public static AjaxResult error(String msg) {
        return new AjaxResult(500, msg, null);
    }

    public static AjaxResult error(String msg, Object obj) {
        return new AjaxResult(500, msg, obj);
    }

    private AjaxResult() {
    }

    private AjaxResult(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }

    public Integer getStatus() {
        return status;
    }

    public AjaxResult setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public AjaxResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public AjaxResult setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}
