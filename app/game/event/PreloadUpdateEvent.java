package game.event;

/**
 * The PreloadUpdateEvent will be fired when the server gets information about the preloaded progress of a player.
 * 
 * @author Sebastian Sachtleben
 */
public class PreloadUpdateEvent {

	private Long playerId;
	private Integer progress;

	public PreloadUpdateEvent(Long playerId, Integer progress) {
		this.playerId = playerId;
		this.progress = progress;
	}

	public Long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(Long playerId) {
		this.playerId = playerId;
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
}
