package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.MyUserDetails;
import com.webecommerce.springboot.entity.PermissionEntity;
import com.webecommerce.springboot.entity.RoleEntity;
import com.webecommerce.springboot.entity.UserEntity;
import com.webecommerce.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsernameAndStatus(username, true);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user, getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<RoleEntity> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<RoleEntity> roles) {

        List<String> privileges = new ArrayList<>();
        List<PermissionEntity> collection = new ArrayList<>();
        for (RoleEntity role : roles) {
            privileges.add(role.getCode());
            collection.addAll(role.getPermissions());
        }
        for (PermissionEntity item : collection) {
            privileges.add(item.getCode());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
