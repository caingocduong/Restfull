package com.example.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class MyPermissionEvaluator implements PermissionEvaluator{
	@Override
	public boolean hasPermission(Authentication authentication,
			Object targetDomainObject, Object permission) {

		return true;
	}

	@Override
	public boolean hasPermission(Authentication authentication,
			Serializable targetId, String targetType, Object permission) {
		throw new RuntimeException("ID based permission evaluation currently not supported.");
	}
}
