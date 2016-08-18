package io.forward.akka.http.client;

import akka.http.javadsl.model.HttpHeader;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class ResponseTransformer {

    private static Map<String, String> extractResponseHeaders(Iterable<HttpHeader> headers) {
        Map<String, String> transformedHeaders = new HashMap<>();
        for (HttpHeader header : headers) {
            transformedHeaders.put(header.name(), header.value());
        }
        return transformedHeaders;
    }

    public static CompletableFuture<InternalResponse> transform(HttpResponse response, Materializer mat) {
        CompletableFuture<ByteString> body =
                response.entity()
                        .getDataBytes()
                        .runWith(Sink.<ByteString>head(), mat)
                        .toCompletableFuture();

        return body.thenApply((responseBody) -> {
            int statusCode = response.status().intValue();
            Map<String, String> headers = extractResponseHeaders(response.getHeaders());
            return new InternalResponse(statusCode, responseBody.utf8String(), headers);
        });
    }
}

