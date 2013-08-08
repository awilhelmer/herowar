package com.herowar.util;

import java.util.Comparator;

import com.herowar.models.entity.game.Material;


public class MaterialComparator implements Comparator<Material> {

	@Override
	public int compare(Material mat1, Material mat2) {
		return mat1.getSortIndex().compareTo(mat2.getSortIndex());
	}

}