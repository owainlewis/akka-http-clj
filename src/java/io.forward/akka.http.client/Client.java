package io.forward.akka.http.client;

import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;
import akka.util.ByteString;

import java.util.concurrent.CompletionStage;

public final class Client {

    final ActorSystem system = ActorSystem.create();

    final ActorMaterializer materializer = ActorMaterializer.create(system);

    /**
     * Run a HTTP request and transform the response to an internal representation
     *
     * @param request A HttpRequest to execute
     * @return An internal (simplified) representation of the response
     */
    public CompletionStage<InternalResponse> run (HttpRequest request) {
        return Http.get(system)
                .singleRequest(request, materializer)
                .thenApply(ResponseTransformer::transform);
    }

    public CompletionStage<InternalResponse> run (InternalRequest request) {
        HttpRequest req = HttpRequest.create()
                .withMethod(request.getMethod())
                .withUri(request.getUri())
                .addHeaders(request.getIterableHttpHeaders());
        return request
                .getBody()
                .map((body) -> run(req.withEntity(ByteString.fromString(body))))
                .orElse(run(req));
    }
}
