package sptech.school.biblioteca.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sptech.school.biblioteca.dto.AuthRequest;
import sptech.school.biblioteca.dto.AuthResponse;
import sptech.school.biblioteca.dto.CadastroRequest;
import sptech.school.biblioteca.model.Usuario;
import sptech.school.biblioteca.repository.UsuarioRepository;
import sptech.school.biblioteca.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody CadastroRequest request) {
        Usuario usuario = new Usuario(
                request.email(),
                passwordEncoder.encode(request.senha()),
                request.role()
        );
        usuarioRepository.save(usuario);
        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );
        UserDetails usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow();
        String token = jwtService.gerarToken(usuario);
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
