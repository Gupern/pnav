package com.gupern.pnav.common.bean;


import com.gupern.pnav.common.baseInterface.BaseResponseEnum;

/**
 *响应码
 */
public enum ResponseEnum implements BaseResponseEnum {
    REQUEST_SUCCEED("200","请求处理成功"),
    REQUEST_FAILED("999","请求处理失败"),
    LOGIN_SUCCEED("201","登录成功"),
    LOGIN_FAILED("998","登录失败，用户未注册"),
    REGISTER_SUCCEED("202","用户注册成功"),
    REGISTER_FAILED("997","用户注册失败"),
    // 数据操作错误定义
    SUCCESS("200", "成功!"),
    BODY_NOT_MATCH("400","请求的数据格式不符!"),
    SIGNATURE_NOT_MATCH("401","请求的数字签名不匹配!"),
    NOT_FOUND("404", "未找到该资源!"),
    INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
    SERVER_BUSY("503","服务器正忙，请稍后再试!")
    ;

    private String code;
    private String msg;

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    //根据code返回msg信息
    public static String getMsgByCode(String code){
        for(ResponseEnum responseEnum : ResponseEnum.values()){
            if(responseEnum.getCode().equals(code)){
                return responseEnum.getMsg();
            }
        }
        return  null;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
