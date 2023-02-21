package com.gupern.pnav.common.bean;

import java.io.Serializable;

public class ResultMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    private String code;//接口状态码
    private String msg;//接口返回信息
    private Object data;//返回参数

    public ResultMsg(String code, String msg, Object data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    public ResultMsg(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }
    public void setResponseEnum(ResponseEnum rEnum){
        this.code = rEnum.getCode();
        this.msg = rEnum.getMsg();
    }
    public ResultMsg(){ super();}
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResultMsg success(ResponseEnum responseEnum,Object data){
        return new ResultMsg(responseEnum.getCode(),responseEnum.getMsg(),data);
    }
    public static ResultMsg success(String code,String msg){
        return new ResultMsg(code, msg);
    }
    public static ResultMsg success(String code,String msg, String data){
        return new ResultMsg(Constant.RESPONSE_SUCCEED_CODE,msg,data);
    }
    public static ResultMsg fail(ResponseEnum responseEnum){
        return new ResultMsg(responseEnum.getCode(),responseEnum.getMsg(),null);
    }
    public static ResultMsg fail(String code,String msg){
        return new ResultMsg(code,msg);
    }
    public static ResultMsg fail(String code,String msg,Object data){
        return new ResultMsg(code,msg,data);
    }

    //判断响应码是否为200
    public boolean hasSuccessful(){ return Constant.RESPONSE_SUCCEED_CODE.equals(this.getCode()); }

}
