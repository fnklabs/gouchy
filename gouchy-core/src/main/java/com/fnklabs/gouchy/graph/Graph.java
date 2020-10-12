package com.fnklabs.gouchy.graph;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Graph interface
 *
 * @param <Vertex> Vertex class type
 * @param <Edge>   Edge class type
 */
public interface Graph<Vertex, Edge> {
    /**
     * Get all vertexes
     *
     * @return Set of vertexes
     */
    Set<Vertex> vertexSet();

    /**
     * Get income/source edges for vertex
     *
     * @param vertex Vertex instance
     *
     * @return Set of edges
     */
    Set<Edge> incomingEdgesOf(Vertex vertex);

    /**
     * Get outgoing/target edges for vertex
     *
     * @param vertex Vertex instance
     *
     * @return Set of edges
     */
    Set<Edge> outgoingEdgesOf(Vertex vertex);

    /**
     * Get vertex source for edge
     *
     * @param edge Edge instance
     *
     * @return Source vertex
     */
    Vertex getEdgeSource(Edge edge);

    /**
     * Get vertex target for edge
     *
     * @param edge Edge instance
     *
     * @return Target vertex
     */
    Vertex getEdgeTarget(Edge edge);

    /**
     * Get graph iterator
     *
     * @return Iterator
     */
    Iterator<Vertex> iterator();

}
