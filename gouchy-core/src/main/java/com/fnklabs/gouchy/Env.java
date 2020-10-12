package com.fnklabs.gouchy;

import com.fnklabs.gouchy.graph.Graph;
import com.google.common.util.concurrent.Futures;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Execution environment for graph evaluation
 */
public class Env {

    private final ExecutorService executorService;

    public Env(ExecutorService executorService) {this.executorService = executorService;}

    public <V extends F<?, Map<V, ?>>, E> CompletableFuture<Void> evaluate(Graph<V, E> graph) {

        Map<V, CompletableFuture<?>> vertexFuturesMap = new ConcurrentHashMap<>();

        Iterator<V> iterator = graph.iterator();

        while (iterator.hasNext()) {
            V next = iterator.next();

            Set<E> subscribedEdgeFutures = graph.incomingEdgesOf(next);

            Map<V, ? extends CompletableFuture<?>> subscribedFutures = subscribedEdgeFutures.stream()
                                                                                            .collect(Collectors.toMap(
                                                                                                    e -> graph.getEdgeSource(e),
                                                                                                    e -> {
                                                                                                        V source = graph.getEdgeSource(e);

                                                                                                        return vertexFuturesMap.get(source);
                                                                                                    },
                                                                                                    (a, b) -> a
                                                                                            ));


            CompletableFuture<Void> mergeFuture = CompletableFuture.allOf(subscribedFutures.values().toArray(new CompletableFuture[0]));

            CompletableFuture<?> vertexFuture = mergeFuture.thenCompose((r) -> {
                Map<V, ?> collect = subscribedFutures.entrySet()
                                                     .stream()
                                                     .collect(Collectors.toMap(
                                                             e -> e.getKey(),
                                                             e -> {
                                                                 return Futures.getUnchecked(e.getValue());
                                                             },
                                                             (a, b) -> a
                                                     ));

                return next.call(collect);
            });

            vertexFuturesMap.put(next, vertexFuture);
        }

        return null;
    }
}
