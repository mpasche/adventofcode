package de.mpasche.aoc._2023;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.InputUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@Challenge(year = 2023, day = 7)
public class Day07 implements Day
{
  private enum TYPE
  {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND
  }

  private final List<Hand> hands;

  public Day07()
  {
    final List<String> input = InputUtils.readInputFileByLine(2023, 7);

    this.hands = parseHands(input);
  }

  @Override
  public void task1()
  {
    log.info("Task 1: The total winning is {}.", calculateTotalWinning(getComparator(false)));
  }

  @Override
  public void task2()
  {
    log.info("Task 2: The new total winning is {}.", calculateTotalWinning(getComparator(true)));
  }

  private List<Hand> parseHands(final List<String> input)
  {
    final List<Hand> hands = new ArrayList<>();

    for(final String line : input)
    {
      final String[] split = line.split("\\s");
      final Hand hand = new Hand(split[0], Integer.parseInt(split[1]));
      hands.add(hand);
    }

    return hands;
  }

  private int calculateTotalWinning(final Comparator<Hand> comparator)
  {
    final List<Hand> sortedHands = hands.stream().sorted(comparator).toList();

    int sum = 0;
    for(int i = 0; i < sortedHands.size(); i++)
    {
      final Hand hand = sortedHands.get(i);
      sum += (i + 1) * hand.bid;
    }
    return sum;
  }

  private Map<Character, Integer> getCardCountMap(final Hand hand)
  {
    final Map<Character, Integer> cardCountMap = new HashMap<>();
    for(final char card : hand.cards.toCharArray())
    {
      cardCountMap.merge(card, 1, Integer::sum);
    }
    return cardCountMap;
  }

  private TYPE getType(final Map<Character, Integer> cardCountMap)
  {
    final List<Integer> counts = cardCountMap.values().stream().sorted().toList();
    return switch(counts.size())
    {
      case 1 -> TYPE.FIVE_OF_A_KIND;
      case 2 -> counts.get(0) == 1 ? TYPE.FOUR_OF_A_KIND : TYPE.FULL_HOUSE;
      case 3 -> counts.get(1) == 1 ? TYPE.THREE_OF_A_KIND : TYPE.TWO_PAIR;
      case 4 -> TYPE.ONE_PAIR;
      case 5 -> TYPE.HIGH_CARD;
      default -> throw new RuntimeException();
    };
  }

  private TYPE getTypeWithJoker(final Map<Character, Integer> cardCountMap)
  {
    final int jokerCount = cardCountMap.getOrDefault('J', 0);
    cardCountMap.remove('J');

    return switch(jokerCount)
    {
      case 0 -> getType(cardCountMap);
      case 1 ->
      {
        final List<Integer> counts = cardCountMap.values().stream().sorted().toList();
        yield switch(counts.size())
        {
          case 1 -> TYPE.FIVE_OF_A_KIND;
          case 2 -> counts.get(0) == 1 ? TYPE.FOUR_OF_A_KIND : TYPE.FULL_HOUSE;
          case 3 -> TYPE.THREE_OF_A_KIND;
          case 4 -> TYPE.ONE_PAIR;
          default -> throw new RuntimeException();
        };
      }
      case 2 ->
      {
        final List<Integer> counts = cardCountMap.values().stream().sorted().toList();
        yield switch(counts.size())
        {
          case 1 -> TYPE.FIVE_OF_A_KIND;
          case 2 -> TYPE.FOUR_OF_A_KIND;
          case 3 -> TYPE.THREE_OF_A_KIND;
          default -> throw new RuntimeException();
        };
      }
      case 3 ->
      {
        final List<Integer> counts = cardCountMap.values().stream().sorted().toList();
        yield counts.get(0) == 1 ? TYPE.FOUR_OF_A_KIND : TYPE.FIVE_OF_A_KIND;
      }
      case 4, 5 -> TYPE.FIVE_OF_A_KIND;
      default -> throw new RuntimeException();
    };
  }

  private int getCardValue(final char card, final boolean withJoker)
  {
    return switch(card)
    {
      case 'A' -> 14;
      case 'K' -> 13;
      case 'Q' -> 12;
      case 'J' -> withJoker ? 1 : 11;
      case 'T' -> 10;
      default -> Character.getNumericValue(card);
    };
  }

  private Comparator<Hand> getComparator(final boolean withJoker)
  {
    return (o1, o2) -> {

      final TYPE type = withJoker
        ? getTypeWithJoker(getCardCountMap(o1))
        : getType(getCardCountMap(o1));
      final TYPE otherType = withJoker
        ? getTypeWithJoker(getCardCountMap(o2))
        : getType(getCardCountMap(o2));

      int compare = Integer.compare(type.ordinal(), otherType.ordinal());
      if(compare == 0)
      {
        for(int i = 0; i < Math.min(o1.cards.length(), o2.cards.length()); i++)
        {
          final int cardValue = getCardValue(o1.cards.charAt(i), withJoker);
          final int otherCardValue = getCardValue(o2.cards.charAt(i), withJoker);
          compare = Integer.compare(cardValue, otherCardValue);
          if(compare != 0)
          {
            break;
          }
        }
      }
      return compare;
    };
  }

  private record Hand(String cards, int bid) {}

}