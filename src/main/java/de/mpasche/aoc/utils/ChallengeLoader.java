package de.mpasche.aoc.utils;

import com.google.common.reflect.ClassPath;
import de.mpasche.aoc.common.Day;
import de.mpasche.aoc.common.Challenge;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class ChallengeLoader
{
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
