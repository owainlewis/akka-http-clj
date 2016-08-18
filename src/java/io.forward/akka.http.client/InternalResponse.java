package io.forward.akka.http.client;

import java.util.Map;

public final class InternalResponse {

    private final int statusCode;

    private final Map<String, String> headers;

    private final String body;

    public InternalResponse(int statusCode, String body, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.body = body;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
