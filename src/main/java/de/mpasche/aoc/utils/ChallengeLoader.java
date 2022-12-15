package de.mpasche.aoc.utils;

import com.google.common.reflect.ClassPath;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Challenge;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * A utility class for loading a challenge for a specific year and day.
 * Challenges are expected to be subclasses of the `Day` class, and should be annotated with the `Challenge` annotation,
 * which specifies the year and day of the challenge.
 *
 * @author mpasche
 */
@Slf4j
@UtilityClass
public class ChallengeLoader
{
  /**
   * Loads a challenge for the given year and day.
   *
   * @param year the year of the challenge to load.
   * @param day the day of the challenge to load.
   * @return an instance of the challenge, if it is found.
   */
  public static Day loadChallenge(final int year, final int day)
  {
    try
    {
      final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

      final Class<?> aClass = ClassPath.from(classLoader)
        .getTopLevelClassesRecursive("de.mpasche.aoc")
        .stream()
        .map(ClassPath.ClassInfo::load)
        .filter(clazz -> clazz.isAnnotationPresent(Challenge.class))
        .filter(clazz -> clazz.getAnnotation(Challenge.class).year() == year)
        .filter(clazz -> clazz.getAnnotation(Challenge.class).day() == day)
        .findFirst()
        .orElseThrow();

      return classLoader.loadClass(aClass.getName()).asSubclass(Day.class).getDeclaredConstructor().newInstance();
    }
    catch(Exception e)
    {
      log.error("Challenge for year {} day {} is not implemented.", year, day);
      System.exit(1);
      return null;
    }
  }
}
