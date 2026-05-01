package com.womensafety.service;

import com.womensafety.database.DatabaseManager;
import com.womensafety.exception.AuthenticationException;
import com.womensafety.exception.UserNotFoundException;
import com.womensafety.model.EmergencyContact;
import com.womensafety.model.Location;
import com.womensafety.model.User;
import com.womensafety.util.InputValidator;
import com.womensafety.util.Logger;
import com.womensafety.util.PasswordUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * UserService — manages user registration, authentication, profile, and contacts.
 */
public class UserService {

    private final DatabaseManager db;
    private final Logger logger;

    public UserService(DatabaseManager db) {
        this.db = db;
        this.logger = Logger.getInstance();
    }

    public User register(String name, String phone, String email, String password) {
        if (!InputValidator.isValidPhone(phone))
            throw new IllegalArgumentException("Invalid phone number. Must be 10-digit Indian mobile number.");
        if (!InputValidator.isValidEmail(email))
            throw new IllegalArgumentException("Invalid email address.");
        if (!InputValidator.isValidPassword(password))
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        if (db.findUserByPhone(phone).isPresent())
            throw new IllegalArgumentException("Phone number already registered.");
        if (db.findUserByEmail(email).isPresent())
            throw new IllegalArgumentException("Email already registered.");

        String userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String hash = PasswordUtil.hashPassword(password);
        User user = new User(userId, InputValidator.sanitize(name), phone, email.toLowerCase(), hash);
        db.saveUser(user);
        logger.log("INFO", "New user registered: " + name + " | " + phone);
        return user;
    }

    public User login(String phone, String password) {
        Optional<User> opt = db.findUserByPhone(phone);
        if (opt.isEmpty()) throw new UserNotFoundException("No account found for phone: " + phone);
        User user = opt.get();
        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash()))
            throw new AuthenticationException("Incorrect password.");
        user.updateLastActive();
        logger.log("INFO", "User logged in: " + user.getName());
        return user;
    }

    public void updateLocation(User user, Location location) {
        user.setCurrentLocation(location);
        db.saveUser(user);
        logger.log("INFO", "Location updated for " + user.getName() + ": " + location.toShortString());
    }

    public EmergencyContact addEmergencyContact(User user, String name, String phone,
                                                String email, String relationship) {
        if (!InputValidator.isValidPhone(phone))
            throw new IllegalArgumentException("Invalid contact phone number.");
        EmergencyContact contact = new EmergencyContact(name, phone, email, relationship);
        user.addEmergencyContact(contact);
        db.saveUser(user);
        logger.log("INFO", "Emergency contact added for " + user.getName() + ": " + name);
        return contact;
    }

    public void removeEmergencyContact(User user, String contactId) {
        user.removeEmergencyContact(contactId);
        db.saveUser(user);
    }

    public List<User> getAllUsers() { return db.getAllUsers(); }

    public Optional<User> findById(String id) { return db.findUserById(id); }
}
