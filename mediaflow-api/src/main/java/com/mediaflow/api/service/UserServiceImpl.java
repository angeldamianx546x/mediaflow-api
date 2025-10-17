package com.mediaflow.api.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mediaflow.api.dto.LocationResponse;
import com.mediaflow.api.dto.LocationRquest;
import com.mediaflow.api.dto.UserAuth;
import com.mediaflow.api.dto.UserRequest;
import com.mediaflow.api.dto.UserResponse;
import com.mediaflow.api.mapper.LocationMapper;
import com.mediaflow.api.mapper.UserMapper;
import com.mediaflow.api.model.Location;
import com.mediaflow.api.model.Profile;
import com.mediaflow.api.model.Role;
import com.mediaflow.api.model.User;
import com.mediaflow.api.repository.LocationRepository;
import com.mediaflow.api.repository.ProfileRepository;
import com.mediaflow.api.repository.RoleRepository;
import com.mediaflow.api.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository repositoryRole;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final LocationRepository locationRepository;

    @Override
    public UserResponse findById(Integer userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not funnd: " + userId));
        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse create(UserRequest req) {
        User user = UserMapper.toEntity(req);
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        // === 🧭 Manejo de localización ===
        if (req.getLocation() != null) {
            LocationRquest locReq = req.getLocation();

            // Buscar si ya existe
            Location location = locationRepository.findExisting(
                    locReq.getCountry(),
                    locReq.getRegion(),
                    locReq.getCity(),
                    locReq.getLat(),
                    locReq.getLng()
            ).orElseGet(() -> {
                // Si no existe, crearla
                Location newLocation = LocationMapper.toEntity(locReq);
                return locationRepository.save(newLocation);
            });

            // Asignar localización al usuario
            user.setLocation(location);
        }
        // 2. Asignar roles
        List<Role> roles = new ArrayList<>();
        Role defaultRole = repositoryRole.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        roles.add(defaultRole);

        if (req.getRoles() != null && !req.getRoles().isEmpty()) {
            req.getRoles().forEach(roleId -> {
                if (roleId != 1) {
                    Role role = repositoryRole.findById(roleId)
                            .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));
                    roles.add(role);
                }
            });
        }

        Map<Integer, Role> rolesMap = roles.stream()
                .collect(Collectors.toMap(Role::getRoleId, r -> r, (r1, r2) -> r1));
        user.setRoles(new ArrayList<>(rolesMap.values()));

        // 3. Guardar el usuario primero
        User savedUser = repository.save(user);

        // 4. Crear el perfil predeterminado asociado al usuario
        String language = (req.getPreferredLanguage() != null && !req.getPreferredLanguage().isBlank()) 
                ? req.getPreferredLanguage() 
                : "es";
                
        Profile profile = Profile.builder()
                .displayName(savedUser.getName())
                .preferredLanguage(language)
                .avatarUrl("https://media.istockphoto.com/id/1495088043/es/vector/icono-de-perfil-de-usuario-avatar-o-icono-de-persona-foto-de-perfil-s%C3%ADmbolo-de-retrato.jpg?s=612x612&w=0&k=20&c=mY3gnj2lU7khgLhV6dQBNqomEGj3ayWH-xtpYuCXrzk=")
                .bio("Nuevo usuario registrado.")
                .user(savedUser)
                .build();

        Profile savedProfile = profileRepository.save(profile);
        
        // 5. Actualizar la referencia del perfil en el usuario
        savedUser.setProfile(savedProfile);
        
        // 6. Retornar la respuesta con el perfil incluido
        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse update(Integer userId, UserRequest req) {

        User existing = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        
        UserMapper.copyToEntity(req, existing);

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        Set<Role> roles = new HashSet<>();
        Role defaultRole = repositoryRole.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Default role not found"));
        roles.add(defaultRole);

        if (req.getRoles() != null && !req.getRoles().isEmpty()) {
            req.getRoles().forEach(roleId -> {
                if (roleId != 1) {
                    Role role = repositoryRole.findById(roleId)
                            .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleId));
                    roles.add(role);
                }
            });
        }

        existing.setRoles(new ArrayList<>(roles));
        
        // Actualizar idioma del perfil si se proporciona
        if (req.getPreferredLanguage() != null && !req.getPreferredLanguage().isBlank() 
                && existing.getProfile() != null) {
            existing.getProfile().setPreferredLanguage(req.getPreferredLanguage());
        }
        
        User saved = repository.save(existing);
        return UserMapper.toResponse(saved);
    }



    @Override
    public void delete(Integer userId) {
        if (!repository.existsById(userId)) {
            throw new EntityNotFoundException("Role not found: " + userId);
        }
        repository.deleteById(userId);
    }

    @Override
    public UserAuth login(String email, String password) {
         UserAuth user = repository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Date not correct"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Date not correct");
        }
        return user;
    }

    @Override
    public LocationResponse getUserLocation(Integer userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        if (user.getLocation() == null) {
            throw new EntityNotFoundException("User has no location assigned");
        }

        return LocationMapper.toResponse(user.getLocation());
    }

}
