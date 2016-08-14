package io.forward.akka.http.client;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;

import java.util.concurrent.CompletionStage;

public final class Client {

    final ActorSystem system = ActorSystem.create();

    final ActorMaterializer materializer = ActorMaterializer.create(system);

    public HttpRequest buildRequest () {
        return HttpRequest.create()
                .withUri("http://owainlewis.com")
                .withMethod(HttpMethods.GET);
    }

    /**
     * Run a HTTP request and transform the response to an internal representation
     *
     * @param request A HttpRequest to execute
     * @return An internal (simplified) representation of the response
     */
    public CompletionStage<InternalResponse> runRequest (HttpRequest request) {
        return Http.get(system)
                .singleRequest(request, materializer)
                .thenApply(ResponseTransformer::transform);
    }
}
