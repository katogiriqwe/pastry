package net.pastry.auth.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false, length = 32)
    String name;

    @Setter(AccessLevel.PRIVATE)
    @Column(nullable = false, length = 100)
    String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    Role role = Role.USER;

    @CreationTimestamp
    Instant createdAt;

    public static User create(@NonNull String name, @NonNull String passwordHash) {
        var user = new User();

        user.setName(name);
        user.setPasswordHash(passwordHash);

        return user;
    }
}
