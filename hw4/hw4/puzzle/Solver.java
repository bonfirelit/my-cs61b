package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private final ArrayList<WorldState> solution;
    private final SearchNode first;
    private int moves;

    private class SearchNode implements Comparable<SearchNode> {
        private int move;
        private WorldState cur;
        private SearchNode prev;
        private int priority;

        public SearchNode(WorldState c, SearchNode p, int m, int priority) {
            cur = c;
            prev = p;
            move = m;
            this.priority = priority;
        }

        public int compareTo(SearchNode o) {
            return Integer.compare(this.priority, o.priority);
        }
    }

    public Solver(WorldState initial) {
        solution = new ArrayList<>();
        first = new SearchNode(initial, null, 0, initial.estimatedDistanceToGoal());
        moves = 0;
        BestFirstSearch();
    }

    private void BestFirstSearch() {
        MinPQ<SearchNode> pq = new MinPQ<>();
        pq.insert(first);
        SearchNode min = pq.delMin();
        while (!min.cur.isGoal()) {
            for (WorldState n : min.cur.neighbors()) {
                if (min.prev == null || !n.equals(min.prev.cur)) {
                    int num = min.move + 1;
                    pq.insert(new SearchNode(n, min, num, num + n.estimatedDistanceToGoal()));
                }
            }
            min = pq.delMin();
        }
        moves = min.move;
        while (min != null) {
            solution.add(min.cur);
            min = min.prev;
        }
    }

    public int moves() {
        return moves;
    }

    public Iterable<WorldState> solution() {
        Collections.reverse(solution);
        return solution;
    }

    public static void main(String[] args) {
    }
}
