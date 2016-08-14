package io.forward.akka.http.client;

import akka.http.javadsl.model.HttpMethod;
import java.util.Map;

public class InternalRequest {

    private final HttpMethod method;

    private final Map<String, String> headers;

    public InternalRequest(HttpMethod method, Map<String, String> headers) {
        this.method = method;
        this.headers = headers;
    }

    public HttpMethod getMethod () {
        return method;
    }

    public Map<String, String> getHeaders () {
        return headers;
    }
}
