package de.mpasche.aoc;

import com.google.common.reflect.ClassPath;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.ChallengeProvider;
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

    final ChallengeProvider challengeProvider = getChallengeProvider(year, day);
    log.info("Run challenges for year {} day {}.", year, day);
    challengeProvider.challenge1();
    challengeProvider.challenge2();
  }

  private static ChallengeProvider getChallengeProvider(final int year, final int day)
  {
    try
    {
      final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

      final Class<?> aClass = ClassPath.from(classLoader)
        .getTopLevelClasses("de.mpasche.aoc._" + year)
        .stream()
        .map(ClassPath.ClassInfo::load)
        .filter(clazz -> clazz.isAnnotationPresent(Challenge.class))
        .filter(clazz -> clazz.getAnnotation(Challenge.class).year() == year)
        .filter(clazz -> clazz.getAnnotation(Challenge.class).day() == day)
        .findFirst()
        .orElseThrow();

      return classLoader.loadClass(aClass.getName()).asSubclass(ChallengeProvider.class).getDeclaredConstructor().newInstance();
    }
    catch(Exception e)
    {
      log.error("Challenge for year {} day {} is not implemented.", year, day);
      System.exit(1);
      return null;
    }
  }
}