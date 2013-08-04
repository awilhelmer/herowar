package dao;

import models.entity.SecurityRole;

public class SecurityRoleDAO extends BaseDAO<Long, SecurityRole> {
	private SecurityRoleDAO() {
		super(Long.class, SecurityRole.class);
	}

	private static final SecurityRoleDAO instance = new SecurityRoleDAO();

	public static long getSecurityRoleCount() {
		return instance.getBaseCount();
	}

	public static SecurityRole findByRoleName(String roleName) {
		return instance.getSingleByPropertyValue("roleName", roleName);
	}
}
