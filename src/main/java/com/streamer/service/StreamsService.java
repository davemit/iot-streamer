package com.streamer.service;

import com.streamer.domain.Streams;
import com.streamer.repository.StreamsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Streams.
 */
@Service
@Transactional
public class StreamsService {

    private final Logger log = LoggerFactory.getLogger(StreamsService.class);
    
    @Inject
    private StreamsRepository streamsRepository;
    
    /**
     * Save a streams.
     * 
     * @param streams the entity to save
     * @return the persisted entity
     */
    public Streams save(Streams streams) {
        log.debug("Request to save Streams : {}", streams);
        Streams result = streamsRepository.save(streams);
        return result;
    }

    /**
     *  Get all the streams.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Streams> findAll(Pageable pageable) {
        log.debug("Request to get all Streams");
        Page<Streams> result = streamsRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one streams by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Streams findOne(Long id) {
        log.debug("Request to get Streams : {}", id);
        Streams streams = streamsRepository.findOne(id);
        return streams;
    }

    /**
     *  Delete the  streams by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Streams : {}", id);
        streamsRepository.delete(id);
    }
}
