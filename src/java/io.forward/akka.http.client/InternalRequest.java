package io.forward.akka.http.client;

import akka.http.javadsl.model.HttpHeader;
import akka.http.javadsl.model.HttpMethod;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.headers.RawHeader;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InternalRequest {

    private final HttpMethod method;

    private final String uri;

    private final Map<String, String> headers;

    private final String body; // TODO replace string with something better?

    public static HttpMethod methodFromString(String method) {
        Optional<HttpMethod> m = HttpMethods.lookup(method.toUpperCase());
        return m.orElseThrow(() -> new RuntimeException("Invalid HTTP method " + method));
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

    /**
     * Returns an iterable that can be passed to a Akka request constructor
     *
     * @return An iterable of HttpHeaders
     */
    public Iterable<HttpHeader> getIterableHttpHeaders () {
        return headers.entrySet().stream().map((e) -> {
            return RawHeader.create(e.getKey(), e.getValue());
        }).collect(Collectors.toSet());
    }

    public String getUri () {
        return uri;
    }

    public String getBody () {
        return body;
    }
}
