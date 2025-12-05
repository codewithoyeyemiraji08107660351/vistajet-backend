package com.vistajet.vistajet.auth;


import com.vistajet.vistajet.security.JwtService;
import com.vistajet.vistajet.user.Role;
import com.vistajet.vistajet.user.Users;
import com.vistajet.vistajet.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder encoder;
    private final UserRepository repository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) {

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email Already Exists");
        }

        var user = Users.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .phone_no(request.getPhone_no())
                .address(request.getAddress())
                .build();
        repository.save(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((Users)auth.getPrincipal());
        claims.put("fullname", user.fullname());
        String jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public List<AllAdminResponse> getAllAdmin() {
        List<Users> admins = repository.findAll();
        return admins.stream()
                .map(this::toResponse)
                .toList();
    }

    public AllAdminResponse toResponse(Users users){
        return AllAdminResponse.builder()
                .id(users.getId())
                .firstname(users.getFirstname())
                .lastname(users.getLastname())
                .email(users.getEmail())
                .phone_no(users.getPhone_no())
                .role(users.getRole())
                .address(users.getAddress())
                .build();
    }
}
