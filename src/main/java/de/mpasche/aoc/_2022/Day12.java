package de.mpasche.aoc._2022;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import de.mpasche.aoc.common.*;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

@SuppressWarnings("UnstableApiUsage")
@Slf4j
@Challenge(year = 2022, day = 12)
public class Day12 implements Day
{
  private final String input;

  public Day12()
  {
    this.input = InputUtils.readInputFile(2022, 12);
  }

  @Override
  public void task1()
  {
    final String nodes = input.replaceAll("\\R", "");
    final int start = nodes.indexOf('S');
    final int end = nodes.indexOf('E');

    final ValueGraph<Integer, Integer> graph = loadGraph((from, to) -> from >= to || from + 1 == to);
    final Dijkstra<Integer> dijkstra = new Dijkstra<>(graph);
    final Integer distance = dijkstra.shortestDistance(start, end);

    log.info("Task 1: {} steps are required at least to get from the current position to the location with the best signal.", distance);
  }

  @Override
  public void task2()
  {
    final String nodes = input.replaceAll("\\R", "");
    final int start = nodes.indexOf('E');
    final List<Integer> ends = IntStream.range(0, nodes.length()).filter(i -> nodes.charAt(i) == 'a').boxed().toList();

    final ValueGraph<Integer, Integer> graph = loadGraph((from, to) -> from <= to || from - 1 == to);
    final Dijkstra<Integer> dijkstra = new Dijkstra<>(graph);
    final Map<Integer, Integer> distances = dijkstra.shortestDistances(start);

    log.info("Task 2: {} steps are required at least to get from the lowest position to the location with the best signal.", ends.stream()
      .map(distances::get)
      .filter(Objects::nonNull)
      .mapToInt(i -> i)
      .min()
      .orElse(-1));
  }

  private ValueGraph<Integer, Integer> loadGraph(final BiPredicate<Integer, Integer> hasEdge)
  {
    final MutableValueGraph<Integer, Integer> graph = ValueGraphBuilder.directed().build();

    final String[] grid = input.split("\\R");
    final int rows = grid.length;
    final int cols = grid[0].length();

    final char[] nodes = input.replaceAll("\\R", "").toCharArray();
    for(int row = 0; row < rows; row++)
    {
      for(int col = 0; col < cols; col++)
      {
        final int currentIndex = row * cols + col;
        final int currentHeight = getHeight(nodes[currentIndex]);

        graph.addNode(currentIndex);

        if(row > 0 && hasEdge.test(currentHeight, getHeight(nodes[(row - 1) * cols + col])))
        {
          graph.putEdgeValue(currentIndex, (row - 1) * cols + col, 1);
        }
        if(row < rows - 1 && hasEdge.test(currentHeight, getHeight(nodes[(row + 1) * cols + col])))
        {
          graph.putEdgeValue(currentIndex, (row + 1) * cols + col, 1);
        }
        if(col < cols - 1 && hasEdge.test(currentHeight, getHeight(nodes[currentIndex + 1])))
        {
          graph.putEdgeValue(currentIndex, currentIndex + 1, 1);
        }
        if(col > 0 && hasEdge.test(currentHeight, getHeight(nodes[currentIndex - 1])))
        {
          graph.putEdgeValue(currentIndex, currentIndex - 1, 1);
        }
      }
    }
    return graph;
  }

  private int getHeight(final char input)
  {
    return switch(input)
    {
      case 'S' -> 'a';
      case 'E' -> 'z';
      default -> input;
    };
  }
}
