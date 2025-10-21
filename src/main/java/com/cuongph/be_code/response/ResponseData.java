package com.cuongph.be_code.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.ThreadContext;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {
    private static final long serialVersionUID = -1953354741201959789L;

    private int status;
    private String errorCode;
    private T errorDesc;
    private String clientMessageId;
    private String path;
    private T data;

    public ResponseData<T> success(T data) {
        this.data = data;
        this.status = 200;
        this.clientMessageId = ThreadContext.get("clientMessageId");
        return this;
    }

    public ResponseData<T> error(int status, String errorCode, T errorDesc) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.clientMessageId = ThreadContext.get("clientMessageId");
        return this;
    }

    public ResponseData<T> error(int status, String errorCode, T errorDesc, T data) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorDesc = errorDesc;
        this.data = data;
        this.clientMessageId = ThreadContext.get("clientMessageId");
        return this;
    }

    public ResponseData<T> path(String path) {
        this.path = path;
        return this;
    }

}
