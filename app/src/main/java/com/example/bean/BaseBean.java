package com.example.bean;


//from https://github.com/wildcreek/MultiChoice
public class BaseBean {


    public int result;//
    public String message;
    public int flag;


    public final static int MESSAGE_SUCCESS = 1;

    public final static int MESSAGE_FAILED = 0;


    public final static int MESSAGE_DECODE_WRONG = -1;

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}