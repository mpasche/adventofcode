package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Challenge(year = 2023, day = 4)
public class Day04 implements Day
{
  private static final Pattern CARD_PATTERN = Pattern.compile("Card\\s+(\\d+):\\s+(.+)\\s\\|\\s+(.+)");
  private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");

  private final List<Card> cards;

  public Day04()
  {
    final List<String> input = InputUtils.readInputFileByLine(2023, 4);

    this.cards = parseCards(input);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The cards are worth a total of {} points.", cards.stream()
      .mapToInt(this::calculateMatches)
      .map(this::calculatePoints)
      .sum());
  }

  @Override
  public void task2()
  {
    final int[] cardCountArray = new int[cards.size()];
    Arrays.fill(cardCountArray, 1);

    for(int i = 0; i < cardCountArray.length; i++)
    {
      final int matches = calculateMatches(cards.get(i));
      for(int j = 1; j <= matches; j++)
      {
        if(i + j >= cardCountArray.length)
        {
          // Cards will never make you copy a card past the end of the table.
          break;
        }
        // Add the number of cards (including copies) of the current card, used in calculating the matches.
        cardCountArray[i + j] = cardCountArray[i + j] + cardCountArray[i];
      }
    }

    log.info("Task 2: There are a total of {} scratchcards.", Arrays.stream(cardCountArray).sum());
  }

  private List<Card> parseCards(final List<String> input)
  {
    final List<Card> cards = new ArrayList<>();

    for(final String line : input)
    {
      final Matcher matcher = CARD_PATTERN.matcher(line);
      if(matcher.matches())
      {
        final String number = matcher.group(1);
        final String winningNumbers = matcher.group(2);
        final String cardNumbers = matcher.group(3);

        final Card card = new Card(Integer.parseInt(number), parseNumbers(winningNumbers), parseNumbers(cardNumbers));
        cards.add(card);
      }
    }

    return cards;
  }

  private Set<Integer> parseNumbers(final String numbers)
  {
    return NUMBER_PATTERN
      .matcher(numbers)
      .results()
      .map(MatchResult::group)
      .map(Integer::parseInt)
      .collect(Collectors.toSet());
  }

  private int calculateMatches(final Card card)
  {
    final Set<Integer> matches = new HashSet<>(card.cardNumbers);
    matches.retainAll(card.winningNumbers);
    return matches.size();
  }

  private int calculatePoints(final int matches)
  {
    // Same as Math.pow(2, matches - 1)
    return matches > 0 ? 1 << (matches - 1) : 0;
  }

  private record Card(int number, Set<Integer> winningNumbers, Set<Integer> cardNumbers) {}

}