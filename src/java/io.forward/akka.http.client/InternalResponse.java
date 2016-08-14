package io.forward.akka.http.client;

import java.util.Map;

public final class InternalResponse {

    private final int statusCode;

    private final Map<String, String> headers;

    public InternalResponse(int statusCode, Map<String, String> headers) {
        this.statusCode = statusCode;
        this.headers = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
