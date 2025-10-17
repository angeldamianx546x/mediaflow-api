package com.mediaflow.api.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mediaflow.api.dto.FileRequest;
import com.mediaflow.api.dto.FileResponse;
import com.mediaflow.api.model.ContentType;
import com.mediaflow.api.service.FileService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class FileController {

    private final FileService fileService;

    // 🔹 Obtener todos los archivos de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileResponse>> getFilesByUser(
            @PathVariable Integer userId, @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(fileService.findByUserFiles(userId, pageable));
    }

    // 🔹 Obtener archivos por tipo de contenido (VIDEO, AUDIO, TEXTO, IMAGEN)
    @GetMapping("/user/{userId}/type/{contentType}")
    public ResponseEntity<List<FileResponse>> getFilesByUserAndType(
            @PathVariable Integer userId,
            @PathVariable ContentType contentType,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(fileService.findByUserContentType(userId, contentType,pageable));
    }

    // 🔹 Obtener un archivo por su ID
    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFileById(@PathVariable Integer fileId) {
        return ResponseEntity.ok(fileService.findById(fileId));
    }

    // 🔹 Crear un nuevo archivo
    @PostMapping
    public ResponseEntity<FileResponse> createFile(@RequestBody FileRequest request) {
        FileResponse created = fileService.create(request);
        return ResponseEntity.ok(created);
    }

    // 🔹 Actualizar archivo
    @PutMapping("/{fileId}")
    public ResponseEntity<FileResponse> updateFile(
            @PathVariable Integer fileId,
            @RequestBody FileRequest request
    ) {
        return ResponseEntity.ok(fileService.update(fileId, request));
    }

    // 🔹 Eliminar archivo
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Integer fileId) {
        fileService.delete(fileId);
        return ResponseEntity.noContent().build();
    }
}
