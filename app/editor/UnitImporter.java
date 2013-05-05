package editor;

import java.io.File;

import models.entity.game.Unit;

public class UnitImporter extends AbstractImporter<Unit> {

  private static UnitImporter instance;

  public static UnitImporter getInstance() {
    if (instance == null) {
      instance = new UnitImporter();
    }
    return instance;
  }

  @Override
  public String getBaseFolder() {
    return "public" + File.separator + "geometries" + File.separator + "units";
  }

}
