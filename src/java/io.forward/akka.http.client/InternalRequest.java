package io.forward.akka.http.client;

import akka.http.javadsl.model.HttpMethod;
import akka.http.javadsl.model.HttpMethods;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InternalRequest {

    private final HttpMethod method;

    private final String uri;

    private final Map<String, String> headers;

    private final String body; // TODO replace string with something better?

    public static HttpMethod methodFromString(String method) {
        Optional<HttpMethod> m = HttpMethods.lookup(method.toUpperCase());
        if (m.isPresent()) {
            return m.get();
        } else {
            throw new RuntimeException("Invalid HTTP method " + method);
        }
    }

    public InternalRequest(HttpMethod method, String uri, String body, Map<String, String> headers) {
        this.method = method;
        this.uri = uri;
        this.body = body;
        this.headers = headers;
    }

    public InternalRequest(HttpMethod method, String uri) {
        this.method = method;
        this.uri = uri;
        this.body = null;
        this.headers = new HashMap<>();
    }

    public HttpMethod getMethod () {
        return method;
    }

    public Map<String, String> getHeaders () {
        return headers;
    }

    public String getUri () {
        return uri;
    }

    public String getBody () {
        return body;
    }
}
