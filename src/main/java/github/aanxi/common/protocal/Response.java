package github.aanxi.common.protocal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = -4317845782629589997L;

    private Status status;

    private Map<String, String> headers = new HashMap<>();

    private Object returnValue;

    private Exception exception;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getHeader(String name) {
        return this.headers == null ? null : this.headers.get(name);
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);

    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public Response(Status status) {
        this.status = status;
    }
}
