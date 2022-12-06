package de.mpasche.aoc;

import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.utils.ChallengeLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

@Slf4j
public class AdventOfCode
{
  public static void main(String[] args)
  {
    final Option yearOption = Option.builder().option("y").longOpt("year").hasArg().type(Number.class).required().desc("Integer of the year").build();
    final Option dayOption = Option.builder().option("d").longOpt("day").hasArg().type(Number.class).required().desc("Integer of the day").build();
    final Options options = new Options().addOption(yearOption).addOption(dayOption);

    final int year;
    final int day;

    try
    {
      final CommandLine cli = new DefaultParser().parse(options, args);
      year = ((Number) cli.getParsedOptionValue(yearOption)).intValue();
      day = ((Number) cli.getParsedOptionValue(dayOption)).intValue();
    }
    catch(ParseException e)
    {
      new HelpFormatter().printHelp(AdventOfCode.class.getSimpleName(), options);
      System.exit(1);
      return;
    }

    final Challenge challenge = ChallengeLoader.loadChallenge(year, day);
    log.info("Execute the challenge for year {} day {}.", year, day);
    challenge.task1();
    challenge.task2();
  }
}