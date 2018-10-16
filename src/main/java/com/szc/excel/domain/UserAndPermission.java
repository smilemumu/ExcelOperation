package com.szc.excel.domain;

import java.util.List;

public class UserAndPermission {

    private User user;

    private List<String> permissions;

    public User getUser() {
        return user;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
