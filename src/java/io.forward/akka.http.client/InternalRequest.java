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

    private final String url;

    private final Map<String, String> headers;

    private final Optional<String> body; // TODO replace string with something better?

    public static HttpMethod methodFromString(String method) {
        Optional<HttpMethod> m = HttpMethods.lookup(method.toUpperCase());
        return m.orElseThrow(() -> new RuntimeException("Invalid HTTP method " + method));
    }

    public InternalRequest(String method, String url, Map<String, String> headers, String body) {
        this.method = methodFromString(method);
        this.url = url;
        this.headers = headers;
        this.body = Optional.of(body);
    }

    public InternalRequest(String method, String url, Map<String, String> headers) {
        this.method = methodFromString(method);
        this.url = url;
        this.headers = headers;
        this.body = Optional.empty();
    }

    public InternalRequest(String method, String url) {
        this.method = methodFromString(method);
        this.url = url;
        this.headers = new HashMap<>();
        this.body = Optional.empty();
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
        return headers.entrySet()
                .stream()
                .map((e) -> RawHeader.create(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

    public String getUrl () {
        return url;
    }

    public Optional<String> getBody () {
        return body;
    }
}
