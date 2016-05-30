package com.streamer.repository;

import com.streamer.domain.Streams;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Streams entity.
 */
@SuppressWarnings("unused")
public interface StreamsRepository extends JpaRepository<Streams,Long> {

    @Query("select streams from Streams streams where streams.user.login = ?#{principal.username}")
    List<Streams> findByUserIsCurrentUser();

}
