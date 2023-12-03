package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Challenge(year = 2023, day = 2)
public class Day02 implements Day
{
  private static final Pattern GAME_PATTERN = Pattern.compile("Game\\s(\\d+):\\s(.*)");
  private static final Pattern CUBE_PATTERN = Pattern.compile("(\\d+)\\s(\\w+)");

  private final List<String> input;

  public Day02()
  {
    this.input = InputUtils.readInputFileByLine(2023, 2);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The sum of the IDs is {}.", input.stream()
      .map(this::decodeGame)
      .filter(Objects::nonNull)
      .filter(this::isValidGame)
      .mapToInt(game -> game.number)
      .sum());
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The sum of the power is {}.", input.stream()
      .map(this::decodeGame)
      .filter(Objects::nonNull)
      .mapToInt(this::calculatePower)
      .sum());
  }

  private Game decodeGame(final String line)
  {
    final Matcher matcher = GAME_PATTERN.matcher(line);
    if(matcher.matches())
    {
      final String number = matcher.group(1);
      final String cubes = matcher.group(2);

      return new Game(Integer.parseInt(number), decodeCubes(cubes));
    }

    return null;
  }

  private List<Cube> decodeCubes(final String line)
  {
    final List<Cube> cubes = new ArrayList<>();

    final Matcher matcher = CUBE_PATTERN.matcher(line);
    while(matcher.find())
    {
      final String count = matcher.group(1);
      final String color = matcher.group(2);

      final Cube cube = new Cube(color, Integer.parseInt(count));
      cubes.add(cube);
    }

    return cubes;
  }

  private boolean isValidGame(final Game game)
  {
    return game.cubes.stream().allMatch(this::isValidCube);
  }

  private boolean isValidCube(final Cube cube)
  {
    return switch(cube.color)
    {
      case "red" -> cube.count <= 12;
      case "green" -> cube.count <= 13;
      case "blue" -> cube.count <= 14;
      default -> false;
    };
  }

  private int calculatePower(final Game game)
  {
    int maxRed = 0;
    int maxGreen = 0;
    int maxBlue = 0;

    for(final Cube cube : game.cubes)
    {
      switch(cube.color)
      {
        case "red" -> maxRed = Math.max(maxRed, cube.count);
        case "green" -> maxGreen = Math.max(maxGreen, cube.count);
        case "blue" -> maxBlue = Math.max(maxBlue, cube.count);
      }
    }

    return maxRed * maxGreen * maxBlue;
  }

  private record Game(int number, List<Cube> cubes) {}

  private record Cube(String color, int count) {}

}
