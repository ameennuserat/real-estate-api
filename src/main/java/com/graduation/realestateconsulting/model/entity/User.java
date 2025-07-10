package com.graduation.realestateconsulting.model.entity;

import com.graduation.realestateconsulting.model.enums.Role;
import com.graduation.realestateconsulting.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean enable;

    private UserStatus status;

    private String verificationCode;

    private String imageUrl;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Client client;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Office office;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Expert expert;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Booking> booking;



    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked = false;

    @Column(name = "block_expires_at")
    private LocalDateTime blockExpiresAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enable;
    }
}