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

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.*;
import akka.stream.ActorMaterializer;
import akka.util.ByteString;

import java.util.Map;
import java.util.concurrent.CompletionStage;

public final class Client {

    final ActorSystem system = ActorSystem.create();

    final ActorMaterializer materializer = ActorMaterializer.create(system);

    public RequestEntity formData(Map<String, String> formData) {
        return FormData.create(formData).toEntity();
    }

    public RequestEntity buildEntity (String body) {
        return HttpEntities.create(ContentTypes.APPLICATION_JSON, body);
    }

    /**
     * Run a HTTP request and transform the response to an internal representation
     *
     * @param request A HttpRequest to execute
     * @return An internal (simplified) representation of the response
     */
    public CompletionStage<InternalResponse> run (HttpRequest request) {
        return Http.get(system)
                .singleRequest(request, materializer)
                .thenCompose((r) -> ResponseTransformer.transform(r, materializer));
    }

    public CompletionStage<InternalResponse> run (InternalRequest request) {
        HttpRequest req = HttpRequest.create()
                .withMethod(request.getMethod())
                .withUri(request.getUrl())
                .addHeaders(request.getIterableHttpHeaders());
        return request
                .getBody()
                .map((body) -> run(req.withEntity(ByteString.fromString(body))))
                .orElse(run(req));
    }
}
