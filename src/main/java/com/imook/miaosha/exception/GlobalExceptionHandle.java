package com.imook.miaosha.exception;

import com.imook.miaosha.result.CodeMsg;
import com.imook.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通用异常处理@ControllerAdvice
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandle {

    // 重要！！！！@ExceptionHandler(value = Exception.class)
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandle(HttpServletRequest httpServletRequest,Exception e){
        if (e instanceof GlobalException){
            GlobalException globalException = (GlobalException) e;
            return Result.error(globalException.getCodeMsg());
        }
        else if(e instanceof BindException){
            BindException exception = (BindException) e;
            List<ObjectError> errorList = exception.getAllErrors();
            ObjectError objectError = errorList.get(0);
            String msg = objectError.getDefaultMessage();
            // 返回一个带参数的异常
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
