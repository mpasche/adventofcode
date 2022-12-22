package de.mpasche.aoc.common;

import com.google.common.graph.ValueGraph;

import java.util.*;

/**
 * A class that implements the Dijkstra shortest path algorithm on a weighted graph.
 *
 * @author mpasche
 */
@SuppressWarnings("UnstableApiUsage")
public class Dijkstra<E>
{
  private final ValueGraph<E, Integer> graph;

  public Dijkstra(final ValueGraph<E, Integer> graph)
  {
    this.graph = graph;
  }

  /**
   * Calculates the shortest path distances from the source node to all other nodes in the graph.
   *
   * @param source the source node.
   * @return a map of nodes to their shortest path distance from the source node.
   */
  public Map<E, Integer> shortestPath(final E source)
  {
    // Initialize distances map with the source node having a distance of 0
    final Map<E, Integer> distances = new HashMap<>();
    distances.put(source, 0);
    // Create a priority queue of nodes sorted by their distance from the source
    final Queue<E> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));
    queue.offer(source);
    // Set of visited nodes
    final Set<E> visited = new HashSet<>();
    while(!queue.isEmpty())
    {
      // Get the next node with the shortest distance
      final E from = queue.poll();
      // Mark the node as visited
      visited.add(from);
      // For each neighbor of the current node
      for(final E to : graph.successors(from))
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