package de.mpasche.aoc.common;

import java.util.List;

public enum Direction
{
  CENTER, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

  public static List<Direction> getCardinalDirections()
  {
    return List.of(NORTH, EAST, SOUTH, WEST);
  }

  public Direction getOppositeDirection()
  {
    return switch(this)
    {
      case CENTER -> CENTER;
      case NORTH -> SOUTH;
      case NORTHEAST -> SOUTHWEST;
      case EAST -> WEST;
      case SOUTHEAST -> NORTHWEST;
      case SOUTH -> NORTH;
      case SOUTHWEST -> NORTHEAST;
      case WEST -> EAST;
      case NORTHWEST -> SOUTHEAST;
    };
  }
}
