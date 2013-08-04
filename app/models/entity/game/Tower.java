package models.entity.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "tower", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@SuppressWarnings("serial")
public class Tower implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String name;

	@Column(length = 2000)
	private String description;

	private Integer price = 0;
	private Integer coverage = 0;
	private Integer reload = 0;
	private Integer damageMin = 0;
	private Integer damageMax = 0;
	private Integer damageAirplane = 0;
	private Integer damageTank = 0;
	private Integer damageTrooper = 0;
	private Integer rotationSpeed = 0;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Geometry geometry;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tower")
	private Set<TowerWeapon> weapons = new HashSet<TowerWeapon>();

	// GETTER & SETTER //

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getCoverage() {
		return coverage;
	}

	public void setCoverage(Integer coverage) {
		this.coverage = coverage;
	}

	public Integer getReload() {
		return reload;
	}

	public void setReload(Integer reload) {
		this.reload = reload;
	}

	public Integer getDamageMin() {
		return damageMin;
	}

	public void setDamageMin(Integer damageMin) {
		this.damageMin = damageMin;
	}

	public Integer getDamageMax() {
		return damageMax;
	}

	public void setDamageMax(Integer damageMax) {
		this.damageMax = damageMax;
	}

	public Integer getDamageAirplane() {
		return damageAirplane;
	}

	public void setDamageAirplane(Integer damageAirplane) {
		this.damageAirplane = damageAirplane;
	}

	public Integer getDamageTank() {
		return damageTank;
	}

	public void setDamageTank(Integer damageTank) {
		this.damageTank = damageTank;
	}

	public Integer getDamageTrooper() {
		return damageTrooper;
	}

	public void setDamageTrooper(Integer damageTrooper) {
		this.damageTrooper = damageTrooper;
	}

	public Integer getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(Integer rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Set<TowerWeapon> getWeapons() {
		return weapons;
	}

	public void setWeapons(Set<TowerWeapon> weapons) {
		this.weapons = weapons;
	}

	@Override
	public String toString() {
		return "Tower [id=" + id + ", name=" + name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((damageMax == null) ? 0 : damageMax.hashCode());
		result = prime * result + ((damageMin == null) ? 0 : damageMin.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((coverage == null) ? 0 : coverage.hashCode());
		result = prime * result + ((reload == null) ? 0 : reload.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tower other = (Tower) obj;
		if (damageMax == null) {
			if (other.damageMax != null)
				return false;
		} else if (!damageMax.equals(other.damageMax))
			return false;
		if (damageMin == null) {
			if (other.damageMin != null)
				return false;
		} else if (!damageMin.equals(other.damageMin))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (coverage == null) {
			if (other.coverage != null)
				return false;
		} else if (!coverage.equals(other.coverage))
			return false;
		if (reload == null) {
			if (other.reload != null)
				return false;
		} else if (!reload.equals(other.reload))
			return false;
		return true;
	}

}
