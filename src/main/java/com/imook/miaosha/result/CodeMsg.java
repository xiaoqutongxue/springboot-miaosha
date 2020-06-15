package com.imook.miaosha.result;

public class CodeMsg {
	private int code;
	private String msg;



    //通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	// 带参数的异常
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常: %s");
	//登录模块 5002XX
    public static CodeMsg PWD_ISEmpty = new CodeMsg(500201, "密码不能为空");
    public static CodeMsg MOBILE_ISEmpty  = new CodeMsg(500202, "手机号码不能为空");
    public static CodeMsg MOBILE_ERROR  = new CodeMsg(500203, "手机号码错误");
    public static CodeMsg MOBILE_NOT_EXIST  = new CodeMsg(500204, "手机号码不存在");
    public static CodeMsg PASS_ERROR = new CodeMsg(500205, "密码错误");

	//商品模块 5003XX
	
	//订单模块 5004XX
	
	//秒杀模块 5005XX
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已经秒杀完毕");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");

	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
    private CodeMsg() {
    }

    public CodeMsg fillArgs(Object...args){
	    int code = this.code;
	    // 把原始的msg和新的加在一起
	    String message = String.format(this.msg,args);

	    // 然后返回新的
        return new CodeMsg(code,message);
    }
}
