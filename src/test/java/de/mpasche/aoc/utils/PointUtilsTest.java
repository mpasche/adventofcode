package de.mpasche.aoc.utils;

import de.mpasche.aoc.common.Direction;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PointUtilsTest
{
  @Test
  public void testIsAdjacentInAllDirections()
  {
    final Point firstPoint = new Point(0, 0);

    // Test adjacent points in the center direction
    Point secondPoint = new Point(0, 0);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the north direction
    secondPoint = new Point(0, 1);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(0, 2);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the northeast direction
    secondPoint = new Point(1, 1);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(2, 2);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the east direction
    secondPoint = new Point(1, 0);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(2, 0);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the southeast direction
    secondPoint = new Point(1, -1);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(2, -2);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the south direction
    secondPoint = new Point(0, -1);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(0, -2);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the southwest direction
    secondPoint = new Point(-1, -1);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(-2, -2);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the west direction
    secondPoint = new Point(-1, 0);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(-2, 0);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));

    // Test adjacent points in the northwest direction
    secondPoint = new Point(-1, 1);
    assertTrue(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
    secondPoint = new Point(-2, 2);
    assertFalse(PointUtils.isAdjacentInAllDirections(firstPoint, secondPoint));
  }

  @Test
  public void testGetDirection()
  {
    final Point from = new Point(0, 0);

    Point to = new Point(0, 0);
    assertEquals(Direction.CENTER, PointUtils.getDirection(from, to));

    to = new Point(0, 1);
    assertEquals(Direction.NORTH, PointUtils.getDirection(from, to));

    to = new Point(1, 1);
    assertEquals(Direction.NORTHEAST, PointUtils.getDirection(from, to));

    to = new Point(1, 0);
    assertEquals(Direction.EAST, PointUtils.getDirection(from, to));

    to = new Point(1, -1);
    assertEquals(Direction.SOUTHEAST, PointUtils.getDirection(from, to));

    to = new Point(0, -1);
    assertEquals(Direction.SOUTH, PointUtils.getDirection(from, to));

    to = new Point(-1, -1);
    assertEquals(Direction.SOUTHWEST, PointUtils.getDirection(from, to));

    to = new Point(-1, 0);
    assertEquals(Direction.WEST, PointUtils.getDirection(from, to));

    to = new Point(-1, 1);
    assertEquals(Direction.NORTHWEST, PointUtils.getDirection(from, to));
  }

  @Test
  public void testMove()
  {
    final Point from = new Point(0, 0);

    Point to = new Point(0, 0);
    assertEquals(to, PointUtils.move(Direction.CENTER, from));

    to = new Point(0, 1);
    assertEquals(to, PointUtils.move(Direction.NORTH, from));

    to = new Point(1, 1);
    assertEquals(to, PointUtils.move(Direction.NORTHEAST, from));

    to = new Point(1, 0);
    assertEquals(to, PointUtils.move(Direction.EAST, from));

    to = new Point(1, -1);
    assertEquals(to, PointUtils.move(Direction.SOUTHEAST, from));

    to = new Point(0, -1);
    assertEquals(to, PointUtils.move(Direction.SOUTH, from));

    to = new Point(-1, -1);
    assertEquals(to, PointUtils.move(Direction.SOUTHWEST, from));

    to = new Point(-1, 0);
    assertEquals(to, PointUtils.move(Direction.WEST, from));

    to = new Point(-1, 1);
    assertEquals(to, PointUtils.move(Direction.NORTHWEST, from));
  }
}