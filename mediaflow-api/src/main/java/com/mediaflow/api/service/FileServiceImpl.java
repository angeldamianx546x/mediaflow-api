package com.mediaflow.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mediaflow.api.dto.FileRequest;
import com.mediaflow.api.dto.FileResponse;
import com.mediaflow.api.mapper.FileMapper;
import com.mediaflow.api.model.ContentType;
import com.mediaflow.api.model.File;
import com.mediaflow.api.model.Location;
import com.mediaflow.api.model.User;
import com.mediaflow.api.repository.FileRepository;
import com.mediaflow.api.repository.LocationRepository;
import com.mediaflow.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    // 🔹 Obtener archivos por tipo (paginados)
    @Override
    public List<FileResponse> findByUserContentType(Integer userId, ContentType contentType, Pageable pageable) {
        Page<File> files = fileRepository.findByUserAndContentType(userId, contentType, pageable);
        return files.stream().map(FileMapper::toResponse).toList();
    }

    @Override
    public List<FileResponse> findByUserFiles(Integer userId, Pageable pageable) {
        Page<File> files = fileRepository.findByUserFiles(userId, pageable);
        return files.stream().map(FileMapper::toResponse).toList();
    }

    // 🔹 Buscar archivo por ID
    @Override
    public FileResponse findById(Integer fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
        return FileMapper.toResponse(file);
    }

    // 🔹 Crear nuevo archivo (vinculado al usuario y su ubicación)
    @Override
    public FileResponse create(FileRequest req) {
        // 1️⃣ Buscar usuario por ID
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2️⃣ Usar la ubicación del usuario si existe
        Location location = user.getLocation();
        if (location == null) {
            // Si el usuario no tiene location, busca la enviada por el request
            location = locationRepository.findById(req.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
        }

        // 3️⃣ Convertir el DTO en entidad
        File file = FileMapper.toEntity(req);
        file.setUser(user);
        file.setLocation(location);

        // 4️⃣ Guardar el archivo
        File saved = fileRepository.save(file);

        // 5️⃣ Devolver respuesta
        return FileMapper.toResponse(saved);
    }

    // 🔹 Actualizar archivo existente
    @Override
    public FileResponse update(Integer fileId, FileRequest req) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));

        FileMapper.copyToEntity(req, file);

        // Si se envía un nuevo userId
        if (req.getUserId() != null) {
            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            file.setUser(user);
        }

        // Si se envía un nuevo locationId
        if (req.getLocationId() != null) {
            Location location = locationRepository.findById(req.getLocationId())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
            file.setLocation(location);
        }

        File updated = fileRepository.save(file);
        return FileMapper.toResponse(updated);
    }

    // 🔹 Eliminar archivo
    @Override
    public void delete(Integer fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new RuntimeException("Archivo no encontrado");
        }
        fileRepository.deleteById(fileId);
    }
}
