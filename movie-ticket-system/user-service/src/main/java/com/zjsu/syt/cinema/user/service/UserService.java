package com.zjsu.syt.cinema.user.service;

import com.zjsu.syt.cinema.user.model.PurchaseHistory;
import com.zjsu.syt.cinema.user.model.User;
import com.zjsu.syt.cinema.user.repository.PurchaseHistoryRepository;
import com.zjsu.syt.cinema.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PurchaseHistoryRepository purchaseHistoryRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateUserPurchaseStats(String userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setTotalSpent(user.getTotalSpent() + amount);
        user.setTicketsPurchased(user.getTicketsPurchased() + 1);
        userRepository.save(user);
    }

    public PurchaseHistory addPurchaseHistory(PurchaseHistory history) {
        return purchaseHistoryRepository.save(history);
    }

    public List<PurchaseHistory> getUserPurchaseHistory(String userId) {
        return purchaseHistoryRepository.findByUserIdOrderByPurchasedAtDesc(userId);
    }

    public Optional<PurchaseHistory> getPurchaseHistoryById(String id) {
        return purchaseHistoryRepository.findById(id);
    }

    /**
     * 用户登录认证
     * @param username 用户名
     * @param password 密码
     * @return 认证成功返回用户对象，失败返回Optional.empty()
     */
    public Optional<User> authenticateUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        // 验证密码
        if (passwordEncoder.matches(password, user.getPassword())) {
            return Optional.of(user);
        }

        return Optional.empty();
    }

    /**
     * 创建用户时加密密码
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    public User createUserWithEncryption(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
