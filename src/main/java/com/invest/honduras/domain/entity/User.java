package com.invest.honduras.domain.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String password;
	private String surnames;
	private String fullname;
	private String codeStatus;
	private String email;
	private String did;
	private String proxyAddress;
	private String secretkey;
	
	@Getter @Setter
	private List<Role> roles;
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getRoles().stream().
				map(authority -> new SimpleGrantedAuthority(authority.getCode())).collect(Collectors.toList());	
	}
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}
	@Override
	public boolean isEnabled() {
		return false;
	}


	@Override
	public String getUsername() {
		return this.email;
	}
	
}
