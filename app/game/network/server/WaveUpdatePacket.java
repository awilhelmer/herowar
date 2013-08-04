package game.network.server;

import java.util.List;

import models.entity.game.Vector3;
import game.network.BasePacket;
import game.network.PacketType;

/**
 * The WaveUpdatePacket will be send from server to client and contains the current wave id and eta of the next wave.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class WaveUpdatePacket extends BasePacket {

	protected int current;
	protected long start;
	protected long eta;
	protected List<Vector3> positions;
	protected List<String> units;

	public WaveUpdatePacket(int current, long start, long eta, List<Vector3> positions, List<String> units) {
		super();
		this.type = PacketType.WaveUpdatePacket;
		this.current = current;
		this.start = start;
		this.eta = eta;
		this.positions = positions;
		this.units = units;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEta() {
		return eta;
	}

	public void setEta(long eta) {
		this.eta = eta;
	}

	public List<Vector3> getPositions() {
		return positions;
	}

	public void setPositions(List<Vector3> positions) {
		this.positions = positions;
	}

	public List<String> getUnits() {
		return units;
	}

	public void setUnits(List<String> units) {
		this.units = units;
	}

	@Override
	public String toString() {
		return "WaveUpdatePacket [type=" + type + ", createdTime=" + createdTime + ", current=" + current + ", eta=" + eta + "]";
	}
}
