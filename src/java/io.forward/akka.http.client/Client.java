package io.forward.akka.http.client;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpMethods;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;

import java.util.concurrent.CompletionStage;

public final class Client {

    final ActorSystem system = ActorSystem.create();

    final ActorMaterializer materializer = ActorMaterializer.create(system);

    // Demo request
    public HttpRequest buildRequest () {
        return HttpRequest.create()
                .withUri("http://owainlewis.com")
                .withMethod(HttpMethods.GET);
    }

    public CompletionStage<HttpResponse> runRequest (HttpRequest request) {
        return Http.get(system)
                .singleRequest(request, materializer);
    }
}

