# Advent of Java

My solution for https://adventofcode.com in Java 17.

## Usage

1. Log in to https://adventofcode.com and read your session cookie. 
2. Create the file `/resources/COOKIE` and write in your session cookie. 
3. Start the application with the argument `-y` / `--year` and `-d` / `--day`.

```
$ mvn clean package
$ java -jar target/${project.bulid.finalName}-jar-with-dependencies.jar
```