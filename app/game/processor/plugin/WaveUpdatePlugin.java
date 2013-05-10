package game.processor.plugin;

import java.util.Iterator;
import java.util.Set;

import play.Logger;

import models.entity.game.Wave;

import game.GameSession;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

/**
 * The WaveUpdatePlugin controls the wave behaviors and update all units.
 * 
 * @author Sebastian Sachtleben
 */
public class WaveUpdatePlugin extends AbstractPlugin implements IPlugin {

  private final static Logger.ALogger log = Logger.of(WaveUpdatePlugin.class);
  
  private Set<Wave> allWaves;
  private Wave currentWave;
  private int currentWaveCount;
  private int allWavesCount;
  
  public WaveUpdatePlugin(GameProcessor processor) {
    super(processor);
    allWaves = getProcessor().getMap().getWaves();
    currentWaveCount = 1;
    allWavesCount = allWaves.size();
    currentWave = getNextWave();
  }

  @Override
  public void process() {
    //log.debug("Processing " + this.toString() + " with wave " + currentWaveCount + " / " + allWavesCount + " (" + currentWave.toString() + ")");
  }

  @Override
  public void addPlayer(GameSession player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void removePlayer(GameSession player) {
    // TODO Auto-generated method stub
  }
  
  private Wave getNextWave() {
    Iterator<Wave> wavesIterator = allWaves.iterator();
    if (wavesIterator.hasNext()) {
      return wavesIterator.next();
    }
    return null;
  }

  @Override
  public String toString() {
    return "WaveUpdatePlugin";
  }
}
