package com.webecommerce.springboot.service.impl;

import com.webecommerce.springboot.dto.RoleDTO;
import com.webecommerce.springboot.dto.UserDTO;
import com.webecommerce.springboot.entity.AuthenticationProvider;
import com.webecommerce.springboot.entity.RoleEntity;
import com.webecommerce.springboot.entity.UserEntity;
import com.webecommerce.springboot.repository.RoleRepository;
import com.webecommerce.springboot.repository.UserRepository;
import com.webecommerce.springboot.service.RoleService;
import com.webecommerce.springboot.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ModelMapper mapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        List<UserDTO> userDTOS = new ArrayList<>();
//        for (UserEntity item : userEntities) {
//            UserDTO userDTO = userConverter.entityToDTO(item);
//            userDTOS.add(userDTO);
//        }
        return userDTOS;
    }

    @Override
    public List<RoleDTO> findAllRole() {
        return roleRepository.findAll().stream()
                .map(r -> mapper.map(r, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (UserEntity item : userEntities) {
            ModelMapper mapper = new ModelMapper();
            UserDTO userDTO = mapper.map(item, UserDTO.class);
            userDTOS.add(userDTO);
        }
        return userDTOS;
    }

    @Override
    public List<UserDTO> findAllAccount() {
        return userRepository.findAllAccount().stream()
                .map(u -> mapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if (userEntity.isPresent()) {
            ModelMapper mapper = new ModelMapper();
            UserDTO userDTO = mapper.map(userEntity.get(), UserDTO.class);
            return userDTO;
        }
        return null;
    }

    @Override
    public UserEntity findEntityById(Long id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        return userEntity.orElse(null);
    }

    @Override
    public UserDTO findOneByUsernameAndStatus(String username, boolean status) {
        UserEntity userEntity = userRepository.findByUsernameAndStatus(username, status);
//        UserDTO userDTO = userConverter.entityToDTO(userEntity);
        return null;
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity createNewUserAfterOAuth2LoginSuccess(String email, String name, AuthenticationProvider provider) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setStatus(true);
        userEntity.setAuthProvider(provider);
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity updateUserAfterOAuth2LoginSuccess(UserEntity userEntity, String name, AuthenticationProvider provider) {
        userEntity.setName(name);
        userEntity.setAuthProvider(provider);
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity save(UserDTO userDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserEntity newUser = mapper.map(userDTO, UserEntity.class);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleService.findEntityById(userDTO.getRoleId()));
        newUser.setRoles(roleEntities);
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public UserEntity update(UserDTO userDTO) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserEntity newUser = findEntityById(userDTO.getId());
        newUser = mapper.map(userDTO, UserEntity.class);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(roleService.findEntityById(userDTO.getRoleId()));
        newUser.setRoles(roleEntities);
        if(userDTO.getPassword() != null) {
            newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        return userRepository.save(newUser);
    }

    @Override
    public void registerUser(String user, String pass, String email) {
        List<RoleEntity> roleEntityList = new ArrayList<>();
        RoleEntity roleEntity = roleRepository.findByCode("USER");
        roleEntityList.add(roleEntity);
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user);
        userEntity.setEmail(email);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userEntity.setPassword(encoder.encode(pass));
        userEntity.setRoles(roleEntityList);
        userRepository.save(userEntity);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }
}
