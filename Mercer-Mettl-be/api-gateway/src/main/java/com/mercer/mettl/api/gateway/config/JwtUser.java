package com.mercer.mettl.api.gateway.config;


public class JwtUser {

    private String userId;

    private Integer orgId;

    private String role;

    public JwtUser(String userId, Integer orgId, String role) {
        this.userId=userId;
        this.orgId=orgId;
        this.role=role;
    }
}
