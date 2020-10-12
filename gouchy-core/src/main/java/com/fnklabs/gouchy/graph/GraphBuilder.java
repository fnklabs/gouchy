package com.fnklabs.gouchy.graph;

import com.google.common.graph.MutableNetwork;
import com.google.common.graph.NetworkBuilder;

public class GraphBuilder<Vertex, Edge> {

    /** Builder of a graph. */
    private final MutableNetwork<Vertex, Edge> graph;

    /**
     * Constructor of a graph builder. Set default graph edge as simple deployed on same node step with items broadcaster from source step.
     */
    public GraphBuilder() {
        this.graph = NetworkBuilder.directed()
                                   .allowsSelfLoops(false)
                                   .build();
    }

    /**
     * Add graph blueprint vertex.
     *
     * @param vertex add blueprint
     *
     * @return GraphBuilder instance
     */
    public GraphBuilder<Vertex, Edge> addVertex(Vertex vertex) {
        graph.addNode(vertex);

        return this;
    }

    /**
     * Connect to graph vertex (blueprint) with specified deploy info.
     *
     * @param left  left node
     * @param right right node
     * @param edge  edge
     *
     * @return GraphBuilder instance
     */
    public GraphBuilder<Vertex, Edge> addEdge(Vertex left, Vertex right, Edge edge) {
        graph.addEdge(left, right, edge);

        return this;
    }

    /**
     * Build graph after all etl steps were added.
     *
     * @return graph blueprint ready to deploy on cluster
     */
    public Graph<Vertex, Edge> build() {
        return new GuavaGraphAdapter<>(graph);
    }

    public static <Vertex, Edge> GraphBuilder<Vertex, Edge> builder() {
        return new GraphBuilder<Vertex, Edge>();
    }

}
