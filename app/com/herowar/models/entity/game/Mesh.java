package com.herowar.models.entity.game;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@Table(name = "mesh")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Mesh implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private Boolean visible;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "position_x")),
			@AttributeOverride(name = "y", column = @Column(name = "position_y")),
			@AttributeOverride(name = "z", column = @Column(name = "position_z")) })
	private Vector3 position;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "rotation_x")),
			@AttributeOverride(name = "y", column = @Column(name = "rotation_y")),
			@AttributeOverride(name = "z", column = @Column(name = "rotation_z")) })
	private Vector3 rotation;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "scale_x")),
			@AttributeOverride(name = "y", column = @Column(name = "scale_y")),
			@AttributeOverride(name = "z", column = @Column(name = "scale_z")) })
	private Vector3 scale;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "geo_id", referencedColumnName = "id")
	private Geometry geometry;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, optional = false)
	@JoinColumn(name = "map_id", referencedColumnName = "id")
	@JsonIgnore
	private Map map;

	@Transient
	private Long geoId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public Vector3 getRotation() {
		return rotation;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	public Vector3 getScale() {
		return scale;
	}

	public void setScale(Vector3 scale) {
		this.scale = scale;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Long getGeoId() {
		return geoId;
	}

	public void setGeoId(Long geoId) {
		this.geoId = geoId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}
