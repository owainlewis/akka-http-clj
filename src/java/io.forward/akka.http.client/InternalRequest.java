/**
 * Copyright (c) 2016 Owain Lewis <owain@owainlewis.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
