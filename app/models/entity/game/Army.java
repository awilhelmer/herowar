package models.entity.game;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author Sebastian Sachtleben
 */
// @Entity
@SuppressWarnings("serial")
public class Army implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private Wave wave;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "army")
	private Set<Unit> units;

	// GETTER & SETTER //

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Wave getWave() {
		return wave;
	}

	public void setWave(Wave wave) {
		this.wave = wave;
	}

	public Set<Unit> getUnits() {
		return units;
	}

	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

}