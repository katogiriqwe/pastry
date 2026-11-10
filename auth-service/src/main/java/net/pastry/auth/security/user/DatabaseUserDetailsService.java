package net.pastry.auth.security.user;

import lombok.RequiredArgsConstructor;
import net.pastry.auth.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DatabaseUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLowerName(username)
                .map(AuthUser::from)
                .orElseThrow(() -> new UsernameNotFoundException("User %s is not found".formatted(username)));
    }
}
