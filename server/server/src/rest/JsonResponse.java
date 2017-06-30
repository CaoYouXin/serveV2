package rest;

public class JsonResponse {

    public static JsonResponse fail(int code, String reason) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setCode(code);
        jsonResponse.setBody(reason);
        return jsonResponse;
    }

    public static JsonResponse success(Object body) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setCode(20000);
        jsonResponse.setBody(body);
        return jsonResponse;
    }

    private int code;
    private Object body;

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
