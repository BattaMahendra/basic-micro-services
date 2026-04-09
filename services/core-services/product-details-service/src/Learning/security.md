# 🔐 SPRING SECURITY — Complete Guide with Code Examples

> A comprehensive revision guide covering all major Spring Security concepts with diagrams and practical code examples.

---

## Table of Contents

1. [Introduction to Spring Security](#introduction)
2. [Authentication](#authentication)
3. [Authorization](#authorization)
4. [Principal, Authority & Roles](#principal-authority-roles)
5. [Spring Boot Security Setup](#spring-boot-security-setup)
6. [How Spring Security Authentication Works Internally](#how-spring-security-works)
7. [JWT — JSON Web Tokens](#jwt)
8. [OAuth 2.0](#oauth)
9. [OpenID Connect (OIDC)](#openid-connect)

---

## 1. Introduction to Spring Security <a name="introduction"></a>

![Spring Security Overview](images/image1.png)

![Spring Security Architecture Layers](images/image2.png)

![Spring Security Core](images/image3.png)

Spring Security is a powerful and highly customizable authentication and access-control framework for Java applications. It is the de-facto standard for securing Spring-based applications.

### 🔧 Maven Dependency

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### 🔧 Gradle Dependency

```groovy
// build.gradle
implementation 'org.springframework.boot:spring-boot-starter-security'
```

> **Note:** Just adding the dependency will auto-configure basic HTTP security, requiring login for all endpoints with a default generated password printed in the console.

---

## 2. Authentication <a name="authentication"></a>

![Authentication Concept](images/image4.png)

**Authentication** = Identification of a user based on their identity (usually username + password).

### Types of Authentication

| Type | Description |
|---|---|
| **Knowledge-Based** | Username + password from memory. Risky if stolen. |
| **Possession-Based** | Mobile OTP, access cards, token cards. Hard to steal. |
| **Multi-Factor (MFA)** | Knowledge-Based + Possession-Based combined. |

### 🔧 In-Memory Authentication (Basic)

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
            .formLogin(Customizer.withDefaults())  // Enables form login
            .httpBasic(Customizer.withDefaults()); // Enables HTTP Basic auth
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("password"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 🔧 Multi-Factor Authentication Trigger (Conceptual)

```java
@Service
public class MfaService {

    public void sendOtp(String phoneNumber) {
        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);
        // Store OTP in cache (e.g., Redis) with TTL
        otpCache.put(phoneNumber, otp);
        // Send via SMS gateway
        smsGateway.send(phoneNumber, "Your OTP: " + otp);
    }

    public boolean verifyOtp(String phoneNumber, String inputOtp) {
        String cachedOtp = otpCache.get(phoneNumber);
        return inputOtp.equals(cachedOtp);
    }
}
```

---

## 3. Authorization <a name="authorization"></a>

![Authorization Concept](images/image5.png)

**Authorization** = Deciding whether an authenticated user is permitted to perform a specific action.

> ✅ Authentication must happen **before** Authorization.

![Authorization Flow](images/image6.png)

### 🔧 URL-Based Authorization

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/public/**").permitAll()           // No auth needed
            .requestMatchers("/admin/**").hasRole("ADMIN")       // Only ADMIN role
            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/**").hasAuthority("DELETE_PRIVILEGE")
            .anyRequest().authenticated()                        // All others need login
        )
        .formLogin(Customizer.withDefaults());
    return http.build();
}
```

### 🔧 Method-Level Authorization

```java
@Configuration
@EnableMethodSecurity // Enables @PreAuthorize, @PostAuthorize, etc.
public class MethodSecurityConfig {}
```

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @GetMapping
    @PreAuthorize("hasRole('USER')")           // Must have USER role
    public List<Order> getAllOrders() { ... }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ORDER')")  // Specific authority required
    public void deleteOrder(@PathVariable Long id) { ... }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.owner == authentication.name") // Check AFTER execution
    public Order getOrder(@PathVariable Long id) { ... }

    @PutMapping("/{id}")
    @PreAuthorize("#order.owner == authentication.name") // SpEL expression
    public Order updateOrder(@PathVariable Long id, @RequestBody Order order) { ... }
}
```

---

## 4. Principal, Authority & Roles <a name="principal-authority-roles"></a>

### Principal

![Principal Concept](images/image7.png)

**Principal** = The currently logged-in user in the system.

```java
@GetMapping("/whoami")
public String getCurrentUser(Authentication authentication) {
    // authentication.getPrincipal() returns the logged-in user details
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    return "Logged in as: " + principal.getUsername();
}

// Or inject directly via @AuthenticationPrincipal
@GetMapping("/profile")
public String getProfile(@AuthenticationPrincipal UserDetails user) {
    return "Username: " + user.getUsername() + ", Roles: " + user.getAuthorities();
}
```

### Authority

**Authority (Granted Authority)** = Fine-grained permission granted to a user to perform a specific action.

```java
// Creating a user with specific authorities (fine-grained)
UserDetails user = User.builder()
    .username("john")
    .password(encoder.encode("pass"))
    .authorities(
        new SimpleGrantedAuthority("READ_PRODUCTS"),
        new SimpleGrantedAuthority("WRITE_ORDERS"),
        new SimpleGrantedAuthority("DELETE_ORDERS")
    )
    .build();
```

### Roles

![Roles as Group of Authorities](images/image8.png)

**Role** = A coarse-grained group of authorities. E.g., `ROLE_ADMIN` bundles multiple permissions.

> ⚠️ In Spring Security, roles are just authorities prefixed with `ROLE_`. When you use `.roles("ADMIN")` it internally stores `ROLE_ADMIN`.

```java
// Define roles with bundled authorities
@Bean
public UserDetailsService userDetailsService(PasswordEncoder encoder) {

    UserDetails clerk = User.builder()
        .username("clerk")
        .password(encoder.encode("pass"))
        .roles("STORE_CLERK")  // Has: ROLE_STORE_CLERK
        .build();

    UserDetails manager = User.builder()
        .username("manager")
        .password(encoder.encode("pass"))
        .roles("DEPARTMENT_MANAGER") // Has: ROLE_DEPARTMENT_MANAGER
        .build();

    UserDetails storeManager = User.builder()
        .username("storemanager")
        .password(encoder.encode("pass"))
        .roles("STORE_MANAGER")  // Has: ROLE_STORE_MANAGER
        .build();

    return new InMemoryUserDetailsManager(clerk, manager, storeManager);
}
```

```java
// Role-based hierarchy (Spring Security RoleHierarchy)
@Bean
public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy(
        "ROLE_STORE_MANAGER > ROLE_DEPARTMENT_MANAGER\n" +
        "ROLE_DEPARTMENT_MANAGER > ROLE_STORE_CLERK"
    );
    return hierarchy;
}
```

---

## 5. Spring Boot Security Setup <a name="spring-boot-security-setup"></a>

![Spring Boot Security Dependency](images/image9.png)

### 🔧 Full Security Configuration Class

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST APIs
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // For JWT
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 🔧 Custom UserDetailsService (DB-backed)

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword()) // Already BCrypt encoded from DB
            .roles(user.getRoles().toArray(new String[0]))
            .build();
    }
}
```

---

## 6. How Spring Security Authentication Works Internally <a name="how-spring-security-works"></a>

![Authentication Flow Overview](images/image10.png)

### The Authentication Object

![Authentication Object Internals](images/image11.png)

- **Before authentication:** holds credentials (username/password)
- **After authentication:** holds the principal (user details) — credentials are cleared

```java
// Authentication object usage
SecurityContext context = SecurityContextHolder.getContext();
Authentication auth = context.getAuthentication();

String username = auth.getName();                      // Principal name
Object principal = auth.getPrincipal();                // UserDetails object
Object credentials = auth.getCredentials();            // Password (null after auth)
Collection<? extends GrantedAuthority> authorities = auth.getAuthorities(); // Roles/permissions
boolean isAuthenticated = auth.isAuthenticated();
```

### UserDetailsService

![UserDetailsService Role](images/image12.png)

Instead of directly connecting to DB, `AuthenticationProvider` connects to `UserDetailsService`.

```java
// UserDetailsService interface — only one method to implement
public interface UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}

// UserDetails interface provides user state
UserDetails userDetails = userDetailsService.loadUserByUsername("john");
userDetails.getUsername();
userDetails.getPassword();
userDetails.getAuthorities();
userDetails.isAccountNonExpired();
userDetails.isAccountNonLocked();
userDetails.isCredentialsNonExpired();
userDetails.isEnabled();
```

### AuthenticationProvider

![AuthenticationProvider](images/image13.png)

```java
// Custom AuthenticationProvider
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
            );
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // This provider handles only UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

### AuthenticationManager

![AuthenticationManager](images/image14.png)

![supports() method](images/image15.png)

`AuthenticationManager` (usually `ProviderManager`) manages multiple `AuthenticationProvider`s and delegates to the right one via `supports()`.

```java
// ProviderManager delegates to correct AuthenticationProvider
@Bean
public AuthenticationManager authenticationManager() {
    List<AuthenticationProvider> providers = List.of(
        daoAuthenticationProvider(),   // For DB-based auth
        ldapAuthenticationProvider(),  // For LDAP auth
        jwtAuthenticationProvider()    // For JWT-based auth
    );
    return new ProviderManager(providers);
}
```

### Overall Spring Security Filter Chain

![Overall Spring Security Flow](images/image16.png)

![Spring Security Architecture](images/image17.png)

```java
// Spring Security processes requests through a chain of filters
// Key filters in order:
// 1. SecurityContextPersistenceFilter
// 2. UsernamePasswordAuthenticationFilter
// 3. BasicAuthenticationFilter
// 4. BearerTokenAuthenticationFilter (for JWT)
// 5. ExceptionTranslationFilter
// 6. FilterSecurityInterceptor

// Adding a custom filter BEFORE the default login filter
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // Add JWT filter before UsernamePasswordAuthenticationFilter
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        // Add custom filter after a specific filter
        .addFilterAfter(customLoggingFilter, BasicAuthenticationFilter.class);
    return http.build();
}
```

---

## 7. JWT — JSON Web Tokens <a name="jwt"></a>

![JWT Introduction](images/image18.png)

### Why JWT?

**HTTP is stateless.** Every request is independent. We need a mechanism to carry identity across requests without server-side session storage.

#### Evolution: Username/Password → Session → JWT

![Stateless HTTP Problem](images/image19.png)

![Token-based Auth](images/image20.png)

**Session Tokens** (Old approach):

![Session Tokens](images/image21.png)

![Session Flow](images/image22.png)

**Problem: Horizontal Scaling with Sessions**

![Session + Horizontal Scaling Problem](images/image23.png)

**Solution tried: Redis Shared Session Store**

![Redis Shared Session](images/image24.png)

**Problem with Redis: Single point of failure + added complexity.**

**Alternative tried: Sticky Sessions**

![Sticky Sessions](images/image25.jpeg)

> Sticky sessions route a user always to the same server — but this hurts scalability and is complex in microservices.

**Final Solution: JWT**

![JWT Solution](images/image26.png)

![JWT Request Flow](images/image27.png)

![JWT vs Session Comparison](images/image28.png)

### JWT Structure

![JWT Structure](images/image29.png)

A JWT = `Header.Payload.Signature` (Base64URL encoded, dot-separated)

![JWT Decoder Example](images/image30.png)

![JWT Claims](images/image31.png)

![JWT Signing](images/image32.png)

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9   ← Header (algo + type)
.
eyJzdWIiOiJqb2huIiwicm9sZXMiOlsiVVNFUiJdLCJpYXQiOjE3MDAwMDAwMDAsImV4cCI6MTcwMDAwMzYwMH0
                                          ← Payload (claims)
.
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
                                          ← Signature (HMAC/RSA signed)
```

### 🔧 Maven Dependencies for JWT

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

### 🔧 JWT Utility Service

```java
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_MS; // e.g., 3600000 = 1 hour

    // Generate token
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    // Extract username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Validate token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
```

### 🔧 JWT Authentication Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Check if Bearer token is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7); // Remove "Bearer "
        final String username = jwtService.extractUsername(jwt);

        // If user not already authenticated in context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Store in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

### 🔧 Auth Controller — Login & Token Generation

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // This will throw BadCredentialsException if credentials are wrong
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}

// Request/Response DTOs
record LoginRequest(String username, String password) {}
record AuthResponse(String token) {}
```

### 🔧 application.yml for JWT

```yaml
jwt:
  secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
  expiration: 3600000   # 1 hour in milliseconds
```

### Advantage of Sessions over JWT

| | Sessions | JWT |
|---|---|---|
| Revocation | Easy — just invalidate session | Hard — must maintain blacklist |
| Scalability | Hard (shared storage needed) | Easy (stateless) |
| Microservices | Difficult | Natural fit |
| Stolen token | Log out to invalidate | Cannot invalidate easily |

> **If JWT is stolen** — maintain a **blacklisted JWT set** (e.g., in Redis) and check it in the filter chain before accepting any token.

```java
// JWT Blacklist check in filter
if (jwtBlacklistService.isBlacklisted(jwt)) {
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has been revoked");
    return;
}
```

---

## 8. OAuth 2.0 <a name="oauth"></a>

![OAuth Overview](images/image33.png)

OAuth 2.0 is an open standard for **authorization** (not authentication). It allows apps to get **delegated access** to resources on behalf of a user, without sharing credentials.

![OAuth Purpose](images/image34.png)

### OAuth 2.0 — Real Life Analogy (Photo Printing App)

![OAuth Flow Step 1](images/image35.png)

![OAuth Flow Step 2](images/image36.png)

![OAuth Flow Step 3](images/image37.png)

![OAuth Flow Step 4](images/image38.png)

Every time the Photo Printing App wants to access user photos, it presents the token to Google — no password sharing needed.

![OAuth Token Usage](images/image39.png)

### OAuth Terminology

#### Resource Owner

![Resource Owner](images/image40.png)

The **user** who owns the data (e.g., the person whose Google Drive photos are being requested).

![Resource Owner Example](images/image41.png)

#### Client Server (Application)

![Client Server](images/image42.png)

The **application** that wants to access the resource on behalf of the resource owner (e.g., Photo Printing Service).

#### Authorization Server

![Authorization Server](images/image43.png)

The **middleman** (e.g., Google OAuth server) that authenticates the user and issues tokens.

### OAuth Flow 1 — Authorization Code Flow

```
Client ──request──► Authorization Server ──ask user──► Resource Owner
                             │                                │
                     user approves ◄─────────────────────────┘
                             │
                   Authorization Code ──► Client
                             │
                   Client exchanges code for Access Token
                             │
                   Access Token ──► Resource Server ──validates──► Auth Server
                             │
                   Resource returned ──► Client
```

![OAuth Authorization Code Flow](images/image44.png)

![OAuth Abstract Protocol Flow](images/image45.png)

### 🔧 Spring Boot OAuth2 Client Configuration

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

```yaml
# application.yml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET
            scope: openid, profile, email
          github:
            client-id: YOUR_GITHUB_CLIENT_ID
            client-secret: YOUR_GITHUB_CLIENT_SECRET
```

```java
@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService)
                )
            );
        return http.build();
    }
}
```

```java
// Custom OAuth2 User Service — map OAuth2 user to your app's user
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"/"github"
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Register/update user in your database
        userService.registerOAuth2User(provider, email, name);

        return oAuth2User;
    }
}
```

### 🔧 Spring Boot OAuth2 Resource Server (Protect APIs with JWT)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://accounts.google.com  # or your Auth Server URL
```

```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                .jwtAuthenticationConverter(jwtAuthenticationConverter())
            )
        );
    return http.build();
}

@Bean
public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
    converter.setAuthoritiesClaimName("roles");       // Claim name in JWT payload
    converter.setAuthorityPrefix("ROLE_");             // Add ROLE_ prefix

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(converter);
    return jwtConverter;
}
```

### OAuth Flow 2 — Implicit Flow

Same as Authorization Code Flow **except**: Authorization Server directly issues the **Access Token** (skips the authorization code exchange step).

> ⚠️ **Implicit Flow is deprecated** in OAuth 2.1 due to security risks (token exposed in URL). Use Authorization Code Flow with PKCE instead.

### Token Types

| Token | Purpose | TTL |
|---|---|---|
| **Authorization Code** | Short-lived code to exchange for access token | Seconds |
| **Access Token** | Access protected resources | Minutes–Hours |
| **Refresh Token** | Get new access tokens without re-login | Days–Weeks |
| **ID Token** (OIDC) | Identity information of the user | Session |

```java
// Accessing OAuth2 tokens in Spring Security context
@GetMapping("/tokens")
public Map<String, String> getTokens(
        @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient client) {

    String accessToken = client.getAccessToken().getTokenValue();
    String refreshToken = client.getRefreshToken() != null
        ? client.getRefreshToken().getTokenValue() : null;

    return Map.of("access_token", accessToken);
}
```

---

## 9. OpenID Connect (OIDC) <a name="openid-connect"></a>

**OpenID Connect** is built **on top of OAuth 2.0** and adds the **identity layer**.

| | OAuth 2.0 | OpenID Connect |
|---|---|---|
| Answers | **What can you access?** | **Who are you?** |
| Token | Access Token | Access Token + **ID Token** |
| Purpose | Authorization | **Authentication + SSO** |

### The Problem OpenID Connect Solves

Apps like Spotify, Medium, Notion need user login — but they delegate it to trusted providers.

```
User → Spotify
    │
    ▼
Redirect to Google Login (OpenID Connect)
    │
User authenticates with Google
    │
Google issues ID Token (JWT) + Access Token
    │
    ▼
Spotify receives and verifies ID Token
Spotify knows: "This is john@gmail.com"
User is logged in!
```

### 🔧 OIDC Configuration in Spring Boot

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_CLIENT_ID
            client-secret: YOUR_CLIENT_SECRET
            scope:
              - openid    # This triggers OIDC — provides ID Token
              - profile
              - email
```

```java
// Access OIDC user info including ID Token
@GetMapping("/oidc-user")
public Map<String, Object> getOidcUser(@AuthenticationPrincipal OidcUser oidcUser) {
    return Map.of(
        "sub",         oidcUser.getSubject(),           // Unique user ID from provider
        "email",       oidcUser.getEmail(),
        "name",        oidcUser.getFullName(),
        "picture",     oidcUser.getPicture(),
        "id_token",    oidcUser.getIdToken().getTokenValue()
    );
}
```

```java
// Custom OIDC User Service
@Service
public class CustomOidcUserService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        // Extract claims from ID Token
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String sub = oidcUser.getSubject();   // Unique stable ID from provider

        // Save/sync with your own database
        appUserService.findOrCreateUser(sub, email, name);

        return oidcUser;
    }
}
```

```java
// Logout with OIDC (also invalidates provider session)
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout
            .logoutSuccessHandler(oidcLogoutSuccessHandler()) // Provider-aware logout
        );
    return http.build();
}

@Bean
public OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
    OidcClientInitiatedLogoutSuccessHandler handler =
        new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
    handler.setPostLogoutRedirectUri("{baseUrl}/login");
    return handler;
}
```

---

## 🔧 Key Annotations Quick Reference

| Annotation | Purpose |
|---|---|
| `@EnableWebSecurity` | Enable Spring Security |
| `@EnableMethodSecurity` | Enable method-level security (`@PreAuthorize` etc.) |
| `@PreAuthorize("hasRole('ADMIN')")` | Check authority BEFORE method executes |
| `@PostAuthorize("returnObject.owner == authentication.name")` | Check AFTER method executes |
| `@Secured("ROLE_ADMIN")` | Older annotation — role check before method |
| `@AuthenticationPrincipal` | Inject the current logged-in user into a method param |
| `@WithMockUser` | For unit tests — simulate a logged-in user |

---

## 🔧 Testing Spring Security

```java
@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldDenyUnauthenticated() throws Exception {
        mockMvc.perform(get("/api/admin/data"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john", roles = {"USER"})
    void shouldAllowAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/user/profile"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldAllowAdminToDelete() throws Exception {
        mockMvc.perform(delete("/api/admin/user/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldDenyUserFromAdminEndpoint() throws Exception {
        mockMvc.perform(delete("/api/admin/user/1"))
            .andExpect(status().isForbidden());
    }
}
```

---

## 🗺️ Complete Architecture Summary

```
HTTP Request
     │
     ▼
┌─────────────────────────────────────────┐
│           Security Filter Chain          │
│  ┌─────────────────────────────────┐    │
│  │  JwtAuthenticationFilter        │    │
│  │  (validates Bearer token)       │    │
│  └────────────────┬────────────────┘    │
│                   │                      │
│  ┌────────────────▼────────────────┐    │
│  │  UsernamePasswordAuthFilter     │    │
│  │  (handles form login)           │    │
│  └────────────────┬────────────────┘    │
│                   │                      │
│  ┌────────────────▼────────────────┐    │
│  │  ExceptionTranslationFilter     │    │
│  │  (401/403 error handling)       │    │
│  └────────────────┬────────────────┘    │
└───────────────────┼─────────────────────┘
                    │
                    ▼
         ┌──────────────────┐
         │ ProviderManager  │   ← AuthenticationManager
         │  (AuthMgr impl)  │
         └────────┬─────────┘
                  │ delegates to
         ┌────────▼───────────────────────┐
         │  DaoAuthenticationProvider     │
         │  (supports UsernamePassword)   │
         └────────┬───────────────────────┘
                  │ loads user via
         ┌────────▼───────────────────────┐
         │    UserDetailsService          │
         │  (loads from DB/LDAP/etc)      │
         └────────────────────────────────┘
                  │
                  ▼
         Authentication stored in
         SecurityContextHolder
```

---

*References:*
- *[JWT Deep Dive](https://medium.com/swlh/why-do-we-need-the-json-web-token-jwt-in-the-modern-web-8490a7284482)*
- *[JWT Security Risks](https://developer.okta.com/blog/2018/06/20/what-happens-if-your-jwt-is-stolen)*
- *[OAuth 2.0 Explained](https://youtu.be/t4-416mg6iU)*
- *[Spring Security Official Docs](https://docs.spring.io/spring-security/reference/)*