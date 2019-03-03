package com.qantas.proj.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public interface Utils {

	final String ROLE_ADMIN = "ROLE_ADMIN";
	final String ROLE_USER = "ROLE_USER";
	public static boolean isRole(String role) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		return authorities.stream().anyMatch(auth->{
			if( auth.getAuthority().equals(role)){
			return true;
			}
			return false;
		});
		
	}
	
	public static String getUserFromAuth() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getName();
	}
}
