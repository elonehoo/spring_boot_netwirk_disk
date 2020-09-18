package com.inet.codebase.utlis;

public class Result {
    //相应的数据
    private Object information;
    //相应的具体信息
    private String message;
    /**
     * 100 - 请求成功
     * 101 - 请求异常
     * 103 - 未登录
     * 104 - 请求失败
     */
    private int code;

    public Result() {
    }

    public Result(Object information, String message, int code) {
        this.information = information;
        this.message = message;
        this.code = code;
    }

    public Object getInformation() {
        return information;
    }

    public void setInformation(Object information) {
        this.information = information;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Result{" +
                "data=" + information +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
