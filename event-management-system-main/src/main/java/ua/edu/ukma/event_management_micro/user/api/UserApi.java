package ua.edu.ukma.event_management_micro.user.api;

/**
 * Internal API for other modules to communicate with User module
 */
public interface UserApi {

    boolean validateUserExists(Long userId);

}