package de.mpasche.aoc._2022;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.Input;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Challenge(year = 2022, day = 7)
public class Day07 implements Day
{
  private static final Pattern FILE_PATTERN = Pattern.compile("(\\d+)\\s(\\w+.*\\w*)");

  private final List<String> input;

  public Day07()
  {
    input = Input.readInputFileByLine(2022, 7);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: Total directory size {}.", decodeTerminal().dirs().stream().filter(dir -> dir.size() <= 100000).mapToInt(Node::size).sum());
  }

  @Override
  public void task2()
  {
    final Node root = decodeTerminal();
    // 70.000.000 - 30.000.000 -> max 40.000.000 may be stored on the filesystem
    final int sizeToDelete = root.size() - 70000000 - 30000000;
    log.info("Task 2: Directory size to delete {}.", root.dirs().stream().filter(dir -> dir.size() > sizeToDelete).mapToInt(Node::size).sorted().findFirst().orElse(-1));
  }

  private Node decodeTerminal()
  {
    final Node root = new Node(null, new HashMap<>(), 0);
    Node currentDir = root;
    for(final String line : input)
    {
      currentDir = currentDir.add(line);
    }
    return root;
  }

  private record Node(Node parent, Map<String, Node> children, int size)
  {
    public Node add(final String line)
    {
      if(line.startsWith("$ cd "))
      {
        return cd(line.substring(5));
      }
      final Matcher matcher = FILE_PATTERN.matcher(line);
      if(matcher.matches())
      {
        children.put(matcher.group(2), new Node(this, new HashMap<>(), Integer.parseInt(matcher.group(1))));
      }
      return this;
    }

    public Node cd(final String dir)
    {
      return switch(dir)
      {
        case "/" -> parent == null ? this : parent.cd(dir);
        case ".." -> parent;
        default -> children.computeIfAbsent(dir, d -> new Node(this, new HashMap<>(), 0));
      };
    }

    public List<Node> dirs()
    {
      return children.values().stream()
        .filter(node -> node.size == 0)
        .flatMap(node -> Stream.concat(Stream.of(node), node.dirs().stream()))
        .toList();
    }

    public int size()
    {
      return size == 0 ? children.values().stream().mapToInt(Node::size).sum() : size;
    }
  }
}
