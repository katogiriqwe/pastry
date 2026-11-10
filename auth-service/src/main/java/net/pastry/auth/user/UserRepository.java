package net.pastry.auth.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "SELECT u FROM User u WHERE u.name = lower(:name)")
    Optional<User> findByLowerName(String name);

    @Query(value = "SELECT count(u) > 0 FROM User u WHERE u.name = lower(:name)")
    boolean existsByLowerName(String name);

}
