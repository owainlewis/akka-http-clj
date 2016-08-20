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

