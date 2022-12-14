package de.mpasche.aoc;

import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.utils.ChallengeLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

/**
 * The main class for the Advent of Code challenge.
 * This class contains the main method which is the entry point for the application.
 * It parses the command line arguments and uses them to load and execute the challenge for the specified year and day.
 *
 * @author mpasche
 */
@Slf4j
public class AdventOfCode
{
  public static void main(String[] args)
  {
    // print important stuff
    System.out.println("    _       _                 _            __    ____          _      ");
    System.out.println("   / \\   __| |_   _____ _ __ | |_    ___  / _|  / ___|___   __| | ___ ");
    System.out.println("  / _ \\ / _` \\ \\ / / _ \\ '_ \\| __|  / _ \\| |_  | |   / _ \\ / _` |/ _ \\");
    System.out.println(" / ___ \\ (_| |\\ V /  __/ | | | |_  | (_) |  _| | |__| (_) | (_| |  __/");
    System.out.println("/_/   \\_\\__,_| \\_/ \\___|_| |_|\\__|  \\___/|_|    \\____\\___/ \\__,_|\\___|");
    System.out.println();

    // command line options
    final Option yearOption = Option.builder().option("y").longOpt("year").hasArg().type(Number.class).required().desc("Integer of the year").build();
    final Option dayOption = Option.builder().option("d").longOpt("day").hasArg().type(Number.class).required().desc("Integer of the day").build();
    final Options options = new Options().addOption(yearOption).addOption(dayOption);

    final int inputYear;
    final int inputDay;

    try // parse command line input
    {
      final CommandLine cli = new DefaultParser().parse(options, args);
      inputYear = ((Number) cli.getParsedOptionValue(yearOption)).intValue();
      inputDay = ((Number) cli.getParsedOptionValue(dayOption)).intValue();
    }
    catch(ParseException e)
    {
      new HelpFormatter().printHelp(AdventOfCode.class.getSimpleName(), options);
      System.exit(1);
      return;
    }

    final Day day = ChallengeLoader.loadChallenge(inputYear, inputDay);
    log.info("Execute the challenge for year {} day {}.", inputYear, inputDay);
    day.task1();
    day.task2();
    System.out.println();
  }
}