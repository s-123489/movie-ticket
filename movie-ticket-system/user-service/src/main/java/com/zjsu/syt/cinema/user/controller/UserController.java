package com.zjsu.syt.cinema.user.controller;

import com.zjsu.syt.cinema.user.dto.LoginRequest;
import com.zjsu.syt.cinema.user.dto.LoginResponse;
import com.zjsu.syt.cinema.user.model.PurchaseHistory;
import com.zjsu.syt.cinema.user.model.User;
import com.zjsu.syt.cinema.user.service.UserService;
import com.zjsu.syt.cinema.user.util.JwtUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Value("${server.port}")
    private String currentPort;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private String getHostname() {
        String hostname = System.getenv("HOSTNAME");
        if (hostname != null && !hostname.isEmpty()) {
            return hostname;
        }
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            log.warn("Failed to get hostname: {}", e.getMessage());
        }
        return "unknown-" + currentPort;
    }

    /**
     * 用户登录接口
     */
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        log.info("User Service [port: {}, hostname: {}] user login attempt: {}",
                currentPort, getHostname(), request.username());

        Optional<User> userOpt = userService.authenticateUser(request.username(), request.password());

        if (userOpt.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("port", currentPort);
            errorResponse.put("hostname", getHostname());
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        LoginResponse loginResponse = new LoginResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", loginResponse);
        response.put("status", "SUCCESS");
        log.info("User Service [port: {}, hostname: {}] user login successful: {}",
                currentPort, getHostname(), request.username());
        return ResponseEntity.ok(response);
    }

    /**
     * 用户注册接口（带密码加密）
     */
    @PostMapping("/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(@Valid @RequestBody UserRequest request) {
        log.info("User Service [port: {}, hostname: {}] registering new user: {}",
                currentPort, getHostname(), request.username());

        User user = new User(
                request.username(),
                request.email(),
                request.password(),
                request.name(),
                request.phone()
        );
        User created = userService.createUserWithEncryption(user);

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", UserResponse.from(created));
        response.put("status", "SUCCESS");
        return response;
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createUser(@Valid @RequestBody UserRequest request) {
        log.info("User Service [port: {}, hostname: {}] creating user: {}",
                currentPort, getHostname(), request.username());

        User user = new User(
                request.username(),
                request.email(),
                request.password(),
                request.name(),
                request.phone()
        );
        User created = userService.createUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", UserResponse.from(created));
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        log.info("User Service [port: {}, hostname: {}] getting all users",
                currentPort, getHostname());

        List<UserResponse> users = userService.getAllUsers().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", users);
        response.put("count", users.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable String id) {
        log.info("User Service [port: {}, hostname: {}] getting user by id: {}",
                currentPort, getHostname(), id);

        return userService.getUserById(id)
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", UserResponse.from(user));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "User with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @GetMapping("/users/username/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        log.info("User Service [port: {}, hostname: {}] getting user by username: {}",
                currentPort, getHostname(), username);

        return userService.getUserByUsername(username)
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", UserResponse.from(user));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "User with username " + username + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRequest request) {
        log.info("User Service [port: {}, hostname: {}] updating user: {}",
                currentPort, getHostname(), id);

        return userService.getUserById(id)
                .map(existing -> {
                    existing.setUsername(request.username());
                    existing.setEmail(request.email());
                    existing.setName(request.name());
                    existing.setPhone(request.phone());
                    if (request.password() != null && !request.password().isEmpty()) {
                        existing.setPassword(request.password());
                    }
                    User updated = userService.updateUser(existing);

                    Map<String, Object> response = new HashMap<>();
                    response.put("port", currentPort);
                    response.put("hostname", getHostname());
                    response.put("data", UserResponse.from(updated));
                    response.put("status", "SUCCESS");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> errorResponse = new HashMap<>();
                    errorResponse.put("port", currentPort);
                    errorResponse.put("hostname", getHostname());
                    errorResponse.put("status", "ERROR");
                    errorResponse.put("message", "User with id " + id + " not found");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
                });
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        log.info("User Service [port: {}, hostname: {}] deleting user: {}",
                currentPort, getHostname(), id);
        userService.deleteUser(id);
    }

    @PostMapping("/users/{userId}/purchase")
    public Map<String, Object> addPurchaseHistory(
            @PathVariable String userId,
            @Valid @RequestBody PurchaseHistoryRequest request) {
        log.info("User Service [port: {}, hostname: {}] adding purchase history for user: {}",
                currentPort, getHostname(), userId);

        PurchaseHistory history = new PurchaseHistory(
                userId,
                request.ticketId(),
                request.movieTitle(),
                request.showtime(),
                request.seatNumber(),
                request.price(),
                request.paymentId()
        );
        PurchaseHistory created = userService.addPurchaseHistory(history);
        userService.updateUserPurchaseStats(userId, request.price());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", PurchaseHistoryResponse.from(created));
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/users/{userId}/history")
    public Map<String, Object> getUserPurchaseHistory(@PathVariable String userId) {
        log.info("User Service [port: {}, hostname: {}] getting purchase history for user: {}",
                currentPort, getHostname(), userId);

        List<PurchaseHistoryResponse> history = userService.getUserPurchaseHistory(userId).stream()
                .map(PurchaseHistoryResponse::from)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("port", currentPort);
        response.put("hostname", getHostname());
        response.put("data", history);
        response.put("count", history.size());
        response.put("status", "SUCCESS");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("port", currentPort);
        health.put("hostname", getHostname());
        health.put("service", "user-service");
        health.put("timestamp", System.currentTimeMillis());
        return health;
    }

    record UserRequest(
            String username,
            String email,
            String password,
            String name,
            String phone
    ) {}

    record UserResponse(
            String id,
            String username,
            String email,
            String name,
            String phone,
            Double totalSpent,
            Integer ticketsPurchased,
            String createdAt
    ) {
        static UserResponse from(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getName(),
                    user.getPhone(),
                    user.getTotalSpent(),
                    user.getTicketsPurchased(),
                    user.getCreatedAt() != null ? user.getCreatedAt().toString() : null
            );
        }
    }

    record PurchaseHistoryRequest(
            String ticketId,
            String movieTitle,
            java.time.LocalDateTime showtime,
            String seatNumber,
            Double price,
            String paymentId
    ) {}

    record PurchaseHistoryResponse(
            String id,
            String userId,
            String ticketId,
            String movieTitle,
            String showtime,
            String seatNumber,
            Double price,
            String paymentId,
            String purchasedAt
    ) {
        static PurchaseHistoryResponse from(PurchaseHistory history) {
            return new PurchaseHistoryResponse(
                    history.getId(),
                    history.getUserId(),
                    history.getTicketId(),
                    history.getMovieTitle(),
                    history.getShowtime() != null ? history.getShowtime().toString() : null,
                    history.getSeatNumber(),
                    history.getPrice(),
                    history.getPaymentId(),
                    history.getPurchasedAt() != null ? history.getPurchasedAt().toString() : null
            );
        }
    }
}
