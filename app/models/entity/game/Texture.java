package models.entity.game;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import models.entity.User;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@Entity
@Table(name = "texture")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Texture implements Serializable {
	private static final long serialVersionUID = 4686002174493439419L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String map;

	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private User uploadUser;

	// isUsed In
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "texture")
	@JsonIgnore
	private Set<Material> materials;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public User getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(User uploadUser) {
		this.uploadUser = uploadUser;
	}

	public Set<Material> getMaterials() {
		return materials;
	}

	public void setMaterials(Set<Material> materials) {
		this.materials = materials;
	}

}
