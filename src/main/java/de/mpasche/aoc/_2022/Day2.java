package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Date;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Date(year = 2022, day = 2)
public class Day2 implements Challenge
{
  private enum Shape
  {
    ROCK, PAPER, SCISSOR;

    public int getScore()
    {
      return this.ordinal() + 1;
    }
  }

  private enum Outcome
  {
    LOSS, DRAW, WIN;

    public int getScore()
    {
      return this.ordinal() * 3;
    }
  }

  final int year = getClass().getAnnotation(Date.class).year();
  final int day = getClass().getAnnotation(Date.class).day();
  final List<String> input;

  public Day2()
  {
    Input.loadData(year, day);
    input = Input.readInputFileByLine(year, day);
  }

  @Override
  public void task1()
  {
    final Function<String, Map.Entry<Shape, Shape>> mapToShape = input -> {
      final String[] split = input.split(" ");
      final Shape opponentShape = Shape.values()[getOrdinal(split[0])];
      final Shape myShape = Shape.values()[getOrdinal(split[1])];
      return Map.entry(opponentShape, myShape);
    };
    
    log.info("Task 1: Score {}", input.stream().map(mapToShape).map(this::calculateScore).mapToInt(i -> i).sum());
  }

  @Override
  public void task2()
  {
    final Function<String, Map.Entry<Shape, Shape>> mapToShape = input -> {
      final String[] split = input.split(" ");
      final Shape opponentShape = Shape.values()[getOrdinal(split[0])];
      final Outcome outcome = Outcome.values()[getOrdinal(split[1])];
      final Shape myShape = chooseShape(opponentShape, outcome);
      return Map.entry(opponentShape, myShape);
    };

    log.info("Task 2: Score {}", input.stream().map(mapToShape).map(this::calculateScore).mapToInt(i -> i).sum());
  }

  private int getOrdinal(final String input)
  {
    return switch(input)
    {
      case "A", "X" -> 0;
      case "B", "Y" -> 1;
      case "C", "Z" -> 2;
      default -> throw new RuntimeException();
    };
  }

  private Shape chooseShape(final Shape opponentsShape, final Outcome outcome)
  {
    return switch(outcome)
    {
      case LOSS -> Shape.values()[Math.floorMod(opponentsShape.ordinal() - 1, Shape.values().length)];
      case DRAW -> opponentsShape;
      case WIN -> Shape.values()[Math.floorMod(opponentsShape.ordinal() + 1, Shape.values().length)];
    };
  }

  private int calculateScore(final Map.Entry<Shape, Shape> pair)
  {
    return calculateOutcome(pair).getScore() + pair.getValue().getScore();
  }

  private Outcome calculateOutcome(final Map.Entry<Shape, Shape> pair)
  {
    final Shape opponentShape = pair.getKey();
    final Shape myShape = pair.getValue();

    if(opponentShape == myShape)
    {
      return Outcome.DRAW;
    }
    else if(opponentShape.ordinal() == Math.floorMod(myShape.ordinal() + 1, Shape.values().length))
    {
      return Outcome.LOSS;
    }
    return Outcome.WIN;
  }
}