package com.fh.common.exception;

import com.fh.common.exception.CountException;
import com.fh.common.json.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//springmvc的全局异常处理
@ControllerAdvice
public class ControllerException {


    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public JsonData test(){
        return JsonData.getJsonError("参数为0");
    }


    //购物车
    @ExceptionHandler(NoLoginException.class) //
    @ResponseBody
    public JsonData handleNoLoginException(NoLoginException e){
        return JsonData.getJsonError(1000,e.getMessage());
    }

    @ExceptionHandler(CountException.class)
    @ResponseBody
    public JsonData handleCountException(CountException e){
        return  JsonData.getJsonError(2000,e.getMessage());
    }


    /**
     * 处理所有不可知异常
     *
     * @param e 异常
     * @return json结果
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JsonData handleException(Exception e) {

        return JsonData.getJsonError(e.getMessage());
    }



}
