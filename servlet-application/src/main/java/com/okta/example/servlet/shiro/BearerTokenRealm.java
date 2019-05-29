package com.okta.example.servlet.shiro;

import com.okta.jwt.AccessTokenVerifier;
import com.okta.jwt.Jwt;
import com.okta.jwt.JwtVerificationException;
import com.okta.jwt.JwtVerifiers;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.BearerToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class BearerTokenRealm extends AuthorizingRealm {

    private final AccessTokenVerifier jwtVerifier;

    public BearerTokenRealm() {
        this.setName("Okta Bearer Token Realm");
        this.setAuthenticationTokenClass(BearerToken.class);
        this.setCredentialsMatcher(new AllowAllCredentialsMatcher());
        this.jwtVerifier = JwtVerifiers.accessTokenVerifierBuilder()
                .setIssuer(System.getProperty("okta.oauth2.issuer"))
                .build();
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        try {
            BearerToken bearerToken = (BearerToken) authenticationToken;
            Jwt jwt = jwtVerifier.decode(bearerToken.getToken());
            return new SimpleAuthenticationInfo(jwt.getClaims(), null, getName());

        } catch (JwtVerificationException e) {
            throw new AuthenticationException(e);
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // some ugly casting that should be cleaned ups
        Map<String, Object> claims = (Map<String, Object>) principalCollection.getPrimaryPrincipal();
        List<String> groups = (List<String>) claims.get("groups");

        // convert 'groups' claim to roles
        return new SimpleAuthorizationInfo(new HashSet<>(groups));
    }
}