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
    final String heights = input.replaceAll("\\R", "");
    final int start = heights.indexOf('S');
    final int end = heights.indexOf('E');

    final ValueGraph<Integer, Integer> graph = loadGraph(input.split("\\R"), (from, to) -> from >= to || from + 1 == to);
    final Dijkstra<Integer> dijkstra = new Dijkstra<>(graph);
    final Map<Integer, Integer> distances = dijkstra.shortestPath(start);

    log.info("Task 1: {} steps are required at least to get from the current position to the location with the best signal.", distances.get(end));
  }

  @Override
  public void task2()
  {
    final String heights = input.replaceAll("\\R", "");
    final int start = heights.indexOf('E');
    final List<Integer> ends = IntStream.range(0, heights.length()).filter(i -> heights.charAt(i) == 'a').boxed().toList();

    final ValueGraph<Integer, Integer> graph = loadGraph(input.split("\\R"), (from, to) -> from <= to || from - 1 == to);
    final Dijkstra<Integer> dijkstra = new Dijkstra<>(graph);
    final Map<Integer, Integer> distances = dijkstra.shortestPath(start);

    log.info("Task 2: {} steps are required at least to get from the lowest position to the location with the best signal.", ends.stream()
      .map(distances::get)
      .filter(Objects::nonNull)
      .mapToInt(i -> i)
      .min()
      .orElse(-1));
  }

  private ValueGraph<Integer, Integer> loadGraph(final String[] input, final BiPredicate<Integer, Integer> hasEdge)
  {
    final MutableValueGraph<Integer, Integer> graph = ValueGraphBuilder.directed().build();

    final int numRows = input.length;
    for(int row = 0; row < numRows; row++)
    {
      final int numCols = input[row].length();
      for(int col = 0; col < numCols; col++)
      {
        final int currentIndex = row * numCols + col;
        final int currentHeight = getHeight(input[row].charAt(col));

        graph.addNode(currentIndex);

        if(row > 0 && hasEdge.test(currentHeight, getHeight(input[row - 1].charAt(col))))
        {
          graph.putEdgeValue(currentIndex, (row - 1) * numCols + col, 1);
        }
        if(row < numRows - 1 && hasEdge.test(currentHeight, getHeight(input[row + 1].charAt(col))))
        {
          graph.putEdgeValue(currentIndex, (row + 1) * numCols + col, 1);
        }
        if(col < numCols - 1 && hasEdge.test(currentHeight, getHeight(input[row].charAt(col + 1))))
        {
          graph.putEdgeValue(currentIndex, currentIndex + 1, 1);
        }
        if(col > 0 && hasEdge.test(currentHeight, getHeight(input[row].charAt(col - 1))))
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
