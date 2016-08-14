package io.forward.akka.http.client;

import akka.http.javadsl.model.HttpHeader;
import akka.http.javadsl.model.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public final class ResponseTransformer {

    private static Map<String, String> extractResponseHeaders(Iterable<HttpHeader> headers) {
        Map<String, String> transformedHeaders = new HashMap<String, String>();
        for (HttpHeader header : headers) {
            transformedHeaders.put(header.name(), header.value());
        }
        return transformedHeaders;
    }

    public static InternalResponse transform(HttpResponse response) {

        int statusCode = response.status().intValue();

        Map<String, String> headers = extractResponseHeaders(response.getHeaders());

        return new InternalResponse(statusCode, headers);
    }
}

