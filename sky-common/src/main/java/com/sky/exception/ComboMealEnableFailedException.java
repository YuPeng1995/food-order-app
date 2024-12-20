package com.sky.exception;

/**
 * 套餐启用失败异常
 */
public class ComboMealEnableFailedException extends BaseException {

    public ComboMealEnableFailedException(){}

    public ComboMealEnableFailedException(String msg){
        super(msg);
    }
}
