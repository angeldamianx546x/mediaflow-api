package com.mediaflow.api.mapper;

import com.mediaflow.api.dto.CommentRequest;
import com.mediaflow.api.dto.CommentRespose;
import com.mediaflow.api.model.Comment;

public final class CommentMapper {
    
    public static CommentRespose toResponse(Comment comment) {
        if (comment == null)
            return null;
        
        CommentRespose.CommentResposeBuilder builder = CommentRespose.builder()
                .commentId(comment.getCommentId())
                .avatarUrl(comment.getAvatarUrl())
                .nameUser(comment.getNameUser())
                .body(comment.getBody())
                .createdAt(comment.getCreatedAt());
        
        if (comment.getContent() != null) {
            builder.contentId(comment.getContent().getContentId());
        }
        
        return builder.build();
    }

    public static Comment toEntity(CommentRequest dto) {
        if (dto == null)
            return null;
        return Comment.builder()
                .avatarUrl(dto.getAvatarUrl())
                .nameUser(dto.getNameUser())
                .body(dto.getBody())
                .createdAt(dto.getCreatedAt())
                .build();
    }

    public static void copyToEntity(CommentRequest dto, Comment entity) {
        if (dto == null || entity == null)
            return;
        entity.setAvatarUrl(dto.getAvatarUrl());
        entity.setNameUser(dto.getNameUser());
        entity.setBody(dto.getBody());
        entity.setCreatedAt(dto.getCreatedAt());
    }
}
