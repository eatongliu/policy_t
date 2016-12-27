package com.gpdata.wanyou.base.core;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * 
 * @author chengchaos
 *
 */
public class JsonResult implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 返回的状态
     */
    private StateType state;
    
    /**
     * 错误原因
     */
    private String cause;
    
    /**
     * 给出的建议
     */
    private String suggest;
    
    /**
     * 携带的值
     */
    private Object value;
    
    private JsonResult(StateType state) {
        this.state = state;
    }
    
    
    /**
     * 携带的状态 OK / ERROR / FAILURE
     * @return
     */
    public StateType getState() {
        return state;
    }

    /**
     * 携带的状态
     * @param state
     * @return
     */
    public JsonResult setState(StateType state) {
        this.state = state;
        return this;
    }

    /**
     * 错误原因
     * @return
     */
    public String getCause() {
        return cause;
    }

    /**
     * 错误原因
     * @param cause
     * @return
     */
    public JsonResult setCause(String cause) {
        this.cause = cause;
        return this;
    }

    /**
     * 给出的建议
     * @return
     */
    public String getSuggest() {
        return suggest;
    }

    /**
     * 给出的建议
     * @param suggest
     * @return
     */
    public JsonResult setSuggest(String suggest) {
        this.suggest = suggest;
        return this;
    }

    /**
     * 携带的值
     * @return
     */
    public Object getValue() {
        return value;
    }

    /**
     * 携带的值
     * @param value
     * @return
     */
    public JsonResult setValue(Object value) {
        this.value = value;
        return this;
    }

    public enum StateType {
        
        OK("OK"), 
        
        ERROR("ERROR"), 
        
        FAILURE("FAILURE"),
        
        OTHER("OTHER");
        
        private String value;
        
        public String value() {
            return value;
        }
        
        private StateType(String value) {
            this.value = value;
        }


        @Override
        public String toString() {
            
            return super.toString();
        }
        
    }
    
    public static JsonResult ok(Object value) {
        return new JsonResult(JsonResult.StateType.OK).setValue(value);
    }


    public static JsonResult error(String cause) {
        return new JsonResult(JsonResult.StateType.ERROR).setCause(cause);
    }

    public static JsonResult error(String cause, String suggest) {
        return new JsonResult(JsonResult.StateType.ERROR).setCause(cause).setSuggest(suggest);
    }

    public static JsonResult failure(String cause) {
        return new JsonResult(JsonResult.StateType.FAILURE).setCause(cause);
    }

    public static JsonResult failure(String cause, String suggest) {
        return new JsonResult(JsonResult.StateType.FAILURE).setCause(cause).setSuggest(suggest);
    }

    public static JsonResult other(String suggest, String value) {
        return new JsonResult(JsonResult.StateType.OTHER).setSuggest(suggest).setValue(value);

    }

    public static void main(String[] args) {

        JsonResult js = new JsonResult(JsonResult.StateType.FAILURE);
        js.setCause("错误");
        
        String rs = JSON.toJSONString(js);
        System.out.println(rs);
        
    }

}
