package controllers.utils;

import java.util.Comparator;

import models.entity.game.Environment;

public class EnvironmentComparator implements Comparator<Environment> {

  @Override
  public int compare(Environment env1, Environment env2) {
    return env1.getName().compareTo(env2.getName());
  }

}
