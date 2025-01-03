package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Set<V> known = new HashSet<>(); // known vertices
        Map<V, Double> distTo = new HashMap<>();
        Map<V, E> spt = new HashMap<>();
        if (Objects.equals(start, end)) {
            return spt;
        }

        ExtrinsicMinPQ<V> minPQ = createMinPQ();

        distTo.put(start, 0.0);
        minPQ.add(start, 0.0);
        Queue<V> perimeter = new LinkedList<>();
        Set<V> visited = new HashSet<>();
        perimeter.add(start);
        visited.add(start);
        while (!perimeter.isEmpty()) {
            V from = perimeter.remove();
            for (E edge : graph.outgoingEdgesFrom(from)) {
                V to = edge.to();

                if (!visited.contains(to)) {
                    distTo.put(to, Double.POSITIVE_INFINITY);
                    minPQ.add(to, Double.POSITIVE_INFINITY);
                    perimeter.add(to);
                    visited.add(to);
                }
                if (!perimeter.contains(end) && visited.contains(end)) {
                    break;
                }

            }


        }

        while (!minPQ.isEmpty()) {
            V u = minPQ.removeMin();
            known.add(u);
            if (known.contains(end)) {
                return spt;
            }
            for (E edge : graph.outgoingEdgesFrom(u)) {
                V v = edge.to();
                //if (distTo.containsKey(v)) {
                if (!distTo.containsKey(v)) {
                    distTo.put(v, Double.POSITIVE_INFINITY);
                    minPQ.add(v, Double.POSITIVE_INFINITY);
                }
                    double oldDist = distTo.get(v);
                    double newDist = distTo.get(u) + edge.weight();
                    if (newDist < oldDist) {
                        distTo.put(v, newDist);
                        minPQ.changePriority(v, newDist);
                        spt.put(v, edge);
                    }


            }

        }
        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(end);
        }

        if (!spt.containsKey(end)) {
            return new ShortestPath.Failure<>();
        }
        List<E> edges = new ArrayList<>();
        E edge = spt.get(end);
        edges.add(edge);
        V vertex = edge.from();
        while (!Objects.equals(vertex, start)) {
            edge = spt.get(vertex);
            edges.add(edge);
            vertex = edge.from();

        }
        Collections.reverse(edges);
        return new ShortestPath.Success<>(edges);
    }

}
