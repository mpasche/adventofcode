package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Challenge(year = 2022, day = 2)
public class Day02 implements Day
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

  private final List<String> input;

  public Day02()
  {
    input = Input.readInputFileByLine(2022, 2);
  }

  private int calculateScore1(final String input)
  {
    final String[] split = input.split(" ");
    final Shape opponentShape = Shape.values()[getOrdinal(split[0])];
    final Shape myShape = Shape.values()[getOrdinal(split[1])];
    return calculateScore(opponentShape, myShape);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: Score {}", input.stream().mapToInt(this::calculateScore1).sum());
  }

  private int calculateScore2(final String input)
  {
    final String[] split = input.split(" ");
    final Shape opponentShape = Shape.values()[getOrdinal(split[0])];
    final Outcome outcome = Outcome.values()[getOrdinal(split[1])];
    final Shape myShape = chooseShape(opponentShape, outcome);
    return calculateScore(opponentShape, myShape);
  }

  @Override
  public void task2()
  {
    log.info("Task 2: Score {}", input.stream().mapToInt(this::calculateScore2).sum());
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

  private int calculateScore(final Shape opponentShape, final Shape myShape)
  {
    return calculateOutcome(opponentShape, myShape).getScore() + myShape.getScore();
  }

  private Outcome calculateOutcome(final Shape opponentShape, final Shape myShape)
  {
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
