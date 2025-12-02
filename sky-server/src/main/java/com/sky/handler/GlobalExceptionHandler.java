package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * 处理SQL异常
     * @param ex SQL异常对象
     * @return 封装后的结果对象
     */
    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex){
        // 示例异常信息：
        // Duplicate entry 'zhangsan' for key 'employee.idx_username'
        String message = ex.getMessage();

        if(message.contains("Duplicate entry")){
            // 将异常信息按空格拆分
            String[] split = message.split(" ");
            // 获取重复的用户名
            String username = split[2];
            // 拼接返回消息
            String msg = username + MessageConstant.ALREADT_EXISTS;
            // 返回错误结果
            return Result.error(msg);
        } else {
            // 返回通用未知错误
            return Result.error(MessageConstant.UNKNOWN_ERROR);
        }
    }
}
