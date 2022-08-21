//package com.webecommerce.springboot.configuration;
//
//import com.webecommerce.springboot.dto.MyUser;
//import com.webecommerce.springboot.service.IRoleService;
//import io.jsonwebtoken.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class JwtTokenUtil {
//
//    @Autowired
//    private IRoleService roleService;
//
//    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
//
//    private static final Logger log = LoggerFactory.getLogger(com.webecommerce.springboot.configuration.JwtTokenUtil.class);
//
//    @Value("${jwt.secret}")
//    private String secret;
//
//    public String generateToken(MyUser myUser) {
//        Map<String, Object> claims = new HashMap<>();
//        String username = myUser.getUsername();
////        List<RoleDTO> roles = roleService.findAllRoleByUsername(username);
////        for(RoleDTO role : roles){
////            claims.put("ROLES", role);
////        }
//        return doGenerateToken(claims, String.valueOf(myUser.getId()));
//    }
//
//    // Lấy thông tin user từ jwt
//    public Long getUserIdFromJWT(String token) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//        return Long.parseLong(claims.getSubject());
//    }
//
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
//            return true;
//        } catch (MalformedJwtException ex) {
//            log.error("Invalid JWT token");
//        } catch (ExpiredJwtException ex) {
//            log.error("Expired JWT token");
//        } catch (UnsupportedJwtException ex) {
//            log.error("Unsupported JWT token");
//        } catch (IllegalArgumentException ex) {
//            log.error("JWT claims string is empty.");
//        }
//        return false;
//    }
//}
