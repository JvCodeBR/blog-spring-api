package br.com.jvcodebr.blogspring.repository;

import br.com.jvcodebr.blogspring.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer> {

    List<PostEntity> findAllByOrderByPostDateDesc();
    List<PostEntity> findAllByUserIdOrderByPostDateDesc(Integer userId);
}
