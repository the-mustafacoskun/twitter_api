package com.workintech.TwitterApi.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tweets")
public class Tweet {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull
    @NotBlank
    @Column(name="content")
    private String content;

    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch=FetchType.LAZY,cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name="user_id")
    private User user;


    @OneToMany(mappedBy="tweet",cascade={CascadeType.ALL},orphanRemoval=true)
    List<Reaction> reactions;


    @OneToMany(mappedBy="tweet",cascade={CascadeType.ALL},orphanRemoval=true)
    List<Comment> comments;


    @OneToMany(mappedBy="tweet",cascade={CascadeType.ALL},orphanRemoval=true)
    List<Retweet> retweets;
}
