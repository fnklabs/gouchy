package com.fnklabs.gouchy.graph;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterators;
import com.google.common.graph.MutableNetwork;
import com.google.common.graph.Traverser;
import sun.security.provider.certpath.Vertex;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

class GuavaGraphAdapter<V, E> implements Graph<V, E> {
    private final MutableNetwork<V, E> graph;

    public GuavaGraphAdapter(MutableNetwork<V, E> graph) {
        this.graph = graph;
    }

    @Override
    public Set<V> vertexSet() {
        return graph.nodes();
    }

    @Override
    public Set<E> incomingEdgesOf(V vertex) {
        return graph.inEdges(vertex);
    }

    @Override
    public Set<E> outgoingEdgesOf(V vertex) {
        return graph.outEdges(vertex);
    }

    @Override
    public V getEdgeSource(E edge) {
        return graph.incidentNodes(edge).source();
    }

    @Override
    public V getEdgeTarget(E edge) {
        return graph.incidentNodes(edge).target();
    }

    @Override
    public Iterator<V> iterator() {
        HashSet<V> topVertexes = new HashSet<>();

        for (V node : graph.nodes()) {
            if (graph.inEdges(node).isEmpty()) {
                topVertexes.add(node);
            }
        }

        Iterator iterator = Iterators.concat(
                topVertexes.stream()
                           .map(v -> Traverser.forGraph(graph).breadthFirst(v).iterator())
                           .toArray(r -> new Iterator[r])
        );

        return iterator;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GuavaGraphAdapter that = (GuavaGraphAdapter) o;
        return Objects.equals(graph, that.graph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("graph", graph)
                          .toString();
    }

}
