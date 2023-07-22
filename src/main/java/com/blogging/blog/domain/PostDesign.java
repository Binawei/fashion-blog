package com.blogging.blog.domain;

import com.blogging.blog.enums.DesignCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class PostDesign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Enumerated(EnumType.STRING)
    private DesignCategory category;


    @ToString.Exclude
    @OneToMany(mappedBy = "postDesign",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commentfac> commentfacs = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "postDesign", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<LikeTable> likeTables = new ArrayList<>();

    private LocalDateTime timestamp;

    private Integer likeCount = 0;
}





