package com.herowar.dao;

import com.herowar.models.entity.UserPermission;

public class UserPermissionDAO extends BaseDAO<Long, UserPermission> {

	private UserPermissionDAO() {
		super(Long.class, UserPermission.class);
	}

	private static final UserPermissionDAO instance = new UserPermissionDAO();

	public static UserPermission findByValue(String value) {
		return instance.getSingleByPropertyValue("value", value);
	}
}
