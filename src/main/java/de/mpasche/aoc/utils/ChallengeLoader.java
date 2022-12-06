package de.mpasche.aoc.utils;

import com.google.common.reflect.ClassPath;
import de.mpasche.aoc.common.Challenge;
import de.mpasche.aoc.common.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ChallengeLoader
{
  public static Challenge loadChallenge(final int year, final int day)
  {
    try
    {
      final ClassLoader classLoader = ClassLoader.getSystemClassLoader();

      final Class<?> aClass = ClassPath.from(classLoader)
        .getTopLevelClassesRecursive("de.mpasche.aoc._" + year)
        .stream()
        .map(ClassPath.ClassInfo::load)
        .filter(clazz -> clazz.isAnnotationPresent(Date.class))
        .filter(clazz -> clazz.getAnnotation(Date.class).year() == year)
        .filter(clazz -> clazz.getAnnotation(Date.class).day() == day)
        .findFirst()
        .orElseThrow();

      return classLoader.loadClass(aClass.getName()).asSubclass(Challenge.class).getDeclaredConstructor().newInstance();
    }
    catch(Exception e)
    {
      log.error("Challenge for year {} day {} is not implemented.", year, day);
      System.exit(1);
      return null;
    }
  }

  private ChallengeLoader() {}

}
