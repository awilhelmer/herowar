package models.api;

import java.util.Comparator;

import models.entity.game.Material;

public class MaterialsComparator implements Comparator<Material> {

  @Override
  public int compare(Material o1, Material o2) {
    return o1.getArrayIndex().compareTo(o2.getArrayIndex());
  }

}
