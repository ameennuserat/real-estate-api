package com.graduation.realestateconsulting.services;

public interface UserManagementService {

    void blockUser(Long userIdToBlock, int days);

    void warnUser(Long userIdToWarn);
}
