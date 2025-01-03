package graphs.minspantrees;

import disjointsets.DisjointSets;
//import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
//import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));
        List<V> vertices = new ArrayList<>(graph.allVertices());
        DisjointSets<V> disjointSets = createDisjointSets();
        Set<E> finalMST = new HashSet<>();
        if (edges.isEmpty() && vertices.size() > 1) {
            return new MinimumSpanningTree.Failure<>();
        }

        for (V vertex : vertices) {
            disjointSets.makeSet(vertex);
        }

        for (E edge : edges) { // each edge (u,v)
            V u = edge.from();
            V v = edge.to();
            int uMST = disjointSets.findSet(u);
            int vMST = disjointSets.findSet(v);
            if (uMST != vMST) {
                finalMST.add(edge);
                disjointSets.union(u, v);
            }
        }

        if (!edges.isEmpty() && finalMST.size() != vertices.size() - 1) {
            return new MinimumSpanningTree.Failure<>();
        }


        return new MinimumSpanningTree.Success<>(finalMST);
    }
}
