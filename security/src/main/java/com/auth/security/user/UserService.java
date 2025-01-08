package com.auth.security.user;

import com.auth.security.exception.PasswordChangeException;
import com.auth.security.exception.UnauthorizedAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Method to delete user by ID, restricted to admins deleting users with USER role
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the user has USER role
        if (user.getRole() == Role.USER) {
            userRepository.delete(user);

        } else {
            throw new UnauthorizedAccessException("Admins can only delete users with USER role.");
        }
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new PasswordChangeException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new PasswordChangeException("New Password and Confirmation Password are not the same");
        }
        // Check if the new password is the same as the current password
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new PasswordChangeException("New password must be different from the current password");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        userRepository.save(user);
    }

    public List<User> getAllActiveUsers() {
        return userRepository.findByStatus(User.Status.ACTIVE);
    }

    public List<User> getAllInactiveUsers() {
        return userRepository.findByStatus(User.Status.INACTIVE);
    }

    public void updateUserStatus(Integer userId, User.Status newStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setStatus(newStatus);
        userRepository.save(user);
    }
}
