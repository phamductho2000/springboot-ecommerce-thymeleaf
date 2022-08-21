//package com.webecommerce.springboot.configuration;
//
//import com.webecommerce.springboot.dto.UserDTO;
//import com.webecommerce.springboot.entity.PermissionEntity;
//import com.webecommerce.springboot.entity.RoleEntity;
//import com.webecommerce.springboot.entity.UserEntity;
//import com.webecommerce.springboot.repository.UserRepository;
//import com.webecommerce.springboot.service.IUserService;
//import com.webecommerce.springboot.service.impl.UserService;
//import io.jsonwebtoken.ExpiredJwtException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//
//@Component
//public class JwtRequestFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private IUserService userService;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        if (!request.getServletPath().equals("/api/v1/users/login")) {
//            final String requestTokenHeader = request.getHeader(AUTHORIZATION);
//            Long userId = null;
//            String jwtToken = null;
//            // JWT Token is in the form "Bearer token". Remove Bearer word and get
//            // only the Token
//            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
//                jwtToken = requestTokenHeader.substring(7);
//                try {
//                    userId = jwtTokenUtil.getUserIdFromJWT(jwtToken);
//                } catch (IllegalArgumentException e) {
//                    System.out.println("Unable to get JWT Token");
//                } catch (ExpiredJwtException e) {
//                    System.out.println("JWT Token has expired");
//                }
//            } else {
//                logger.warn("JWT Token does not begin with Bearer String");
//            }
//
//            // Once we get the token validate it.
//            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                UserDTO userDTO = userService.findOne(userId);
//                UserEntity user = userRepository.findByUsernameAndStatus(userDTO.getUsername(), 1);
//                List<String> privileges = new ArrayList<>();
//                List<PermissionEntity> collection = new ArrayList<>();
//                for (RoleEntity role : user.getRoles()) {
//                    privileges.add(role.getCode());
//                    collection.addAll(role.getPermissions());
//                }
//                for (PermissionEntity item : collection) {
//                    privileges.add(item.getName());
//                }
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                for (String privilege : privileges) {
//                    authorities.add(new SimpleGrantedAuthority(privilege));
//                }
//
//                // if token is valid configure Spring Security to manually set
//                // authentication
//                if (jwtTokenUtil.validateToken(jwtToken)) {
//
//                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                            userDTO.getUsername(), null, authorities);
//                    usernamePasswordAuthenticationToken
//                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    // After setting the Authentication in the context, we specify
//                    // that the current user is authenticated. So it passes the
//                    // Spring Security Configurations successfully.
//                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                }
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}
