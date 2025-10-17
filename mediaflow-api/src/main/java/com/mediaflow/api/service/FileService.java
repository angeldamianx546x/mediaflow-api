package com.mediaflow.api.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.mediaflow.api.dto.FileRequest;
import com.mediaflow.api.dto.FileResponse;
import com.mediaflow.api.model.ContentType;

public interface FileService {
    List<FileResponse> findByUserContentType(Integer userId, ContentType contentType,Pageable pageable);
    //devuelve todo
    List<FileResponse> findByUserFiles(Integer userId,Pageable pageable);

    FileResponse findById(Integer fileId);

    FileResponse create(FileRequest req);

    FileResponse update(Integer fileId, FileRequest req);

    void delete(Integer roleId);
}
