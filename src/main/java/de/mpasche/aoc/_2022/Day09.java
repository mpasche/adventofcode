package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Direction;
import de.mpasche.aoc.utils.InputUtils;
import de.mpasche.aoc.utils.PointUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Challenge(year = 2022, day = 9)
public class Day09 implements Day
{
  private static final Pattern MOVE_PATTERN = Pattern.compile("(\\w)\\s(\\d+)");

  private final List<Move> moves;

  public Day09()
  {
    this.moves = decodeMoves(InputUtils.readInputFileByLine(2022, 9));
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The tail of the rope has visited at least {} positions.", simulateMoves(2));
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The tail of the rope has visited at least {} positions.", simulateMoves(10));
  }

  private List<Move> decodeMoves(final List<String> input)
  {
    return input.stream()
      .map(MOVE_PATTERN::matcher)
      .filter(Matcher::matches)
      .map(matcher -> new Move(getDirection(matcher.group(1).charAt(0)), Integer.parseInt(matcher.group(2))))
      .toList();
  }

  private Direction getDirection(final char input)
  {
    return switch(input)
    {
      case 'U' -> Direction.NORTH;
      case 'D' -> Direction.SOUTH;
      case 'R' -> Direction.EAST;
      case 'L' -> Direction.WEST;
      default -> throw new RuntimeException();
    };
  }

  private int simulateMoves(final int ropeSize)
  {
    // Return 0 if the rope size is 0 or less
    if(ropeSize <= 0)
    {
      return 0;
    }
    // Create a set to store the visited positions
    final Set<Point> visited = new HashSet<>();
    // Create a deque to store the positions of the sections of the rope
    final Deque<Point> rope = IntStream.range(0, ropeSize).mapToObj(i -> new Point()).collect(Collectors.toCollection(ArrayDeque::new));
    // Add the initial position of the tail of the rope to the set of visited positions
    visited.add(rope.peek());
    // Loop over the list of moves
    for(final Move move : moves)
    {
      // Loop over the number of steps in the move
      for(int i = 0; i < move.steps; i++)
      {
        // Move the head of the rope in the specified direction
        final Point head = PointUtils.move(move.direction, rope.pop());
        // Add the new position of the head to the deque
        rope.add(head);
        // Loop over the sections of the rope
        for(int j = 0; j < ropeSize - 1; j++)
        {
          // Get the previous section position
          final Point previousSectionPosition = rope.peekLast();
          // Get the current section position
          Point sectionPosition = rope.pop();
          // We only need to move if the current section is not adjacent to the previous section
          if(!PointUtils.isAdjacentInAllDirections(sectionPosition, previousSectionPosition))
          {
            // Get the direction from the current section to the previous section
            final Direction direction = PointUtils.getDirection(sectionPosition, previousSectionPosition);
            // Move the current section in the direction of the previous section
            sectionPosition = PointUtils.move(direction, sectionPosition);
            // We only need to track the position of the last section of the rope
            if(j == ropeSize - 2)
            {
              // Add the new position of the tail to the set of visited positions
              visited.add(sectionPosition);
            }
          }
          // Add the current section position back to the deque
          rope.add(sectionPosition);
        }
      }
    }
    return visited.size();
  }

  private record Move(Direction direction, int steps) {}

}
