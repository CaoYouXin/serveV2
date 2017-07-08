package rest;

import beans.EntityBeanI;

public class JsonResponse {

    private int code;
    private Object body;

    public static JsonResponse fail(int code, String reason) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setCode(code);
        jsonResponse.setBody(reason);
        return jsonResponse;
    }

    public static JsonResponse success(Object body) {
        if (body instanceof EntityBeanI) {
            body = ((EntityBeanI) body).toMap();
        }

        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setCode(20000);
        jsonResponse.setBody(body);
        return jsonResponse;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "code=" + code +
                ", body=" + body +
                '}';
    }
}
