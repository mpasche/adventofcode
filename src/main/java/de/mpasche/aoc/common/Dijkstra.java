package de.mpasche.aoc.common;

import com.google.common.graph.ValueGraph;

import java.util.*;

/**
 * A class that implements the Dijkstra shortest path algorithm on a weighted graph.
 *
 * @author mpasche
 */
@SuppressWarnings("UnstableApiUsage")
public class Dijkstra<N>
{
  private final ValueGraph<N, Integer> graph;

  public Dijkstra(final ValueGraph<N, Integer> graph)
  {
    this.graph = graph;
  }

  /**
   * Calculates the shortest distances from the source node to all other nodes in the graph.
   *
   * @param source the source node.
   * @return a map of nodes to their shortest distance from the source node.
   */
  public Map<N, Integer> shortestDistances(final N source)
  {
    return buildPath(source, null);
  }

  /**
   * Calculates the shortest distance from the source node to the target node in the graph.
   *
   * @param source the source node.
   * @param target the target node.
   * @return the shortest distance from the source node to the target node.
   */
  public Integer shortestDistance(final N source, final N target)
  {
    return buildPath(source, target).get(target);
  }

  private Map<N, Integer> buildPath(final N source, final N target)
  {
    // Initialize distances map with the source node having a distance of 0
    final Map<N, Integer> distances = new HashMap<>();
    distances.put(source, 0);
    // Create a priority queue of nodes sorted by their distance from the source
    final Queue<N> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
    queue.offer(source);
    // Set of visited nodes
    final Set<N> visited = new HashSet<>();
    while(!queue.isEmpty())
    {
      // Get the next node with the shortest distance
      final N from = queue.poll();
      // If the current node is the target node then we have found the shortest distance
      if(from.equals(target))
      {
        return distances;
      }
      // Mark the current node as visited
      visited.add(from);
      // For each neighbor of the current node
      for(final N to : graph.successors(from))
      {
        // If the neighbor has not been visited
        if(!visited.contains(to))
        {
          final int newDistance = distances.computeIfAbsent(from, i -> 0) + graph.edgeValue(from, to).orElseThrow();
          final int oldDistance = distances.computeIfAbsent(to, i -> Integer.MAX_VALUE);
          // If the new distance is shorter than the old distance, update the distance and add the neighbor to the queue
          if(newDistance < oldDistance)
          {
            distances.put(to, newDistance);
            queue.offer(to);
          }
        }
      }
    }
    // Return the map of distances
    return distances;
  }
}