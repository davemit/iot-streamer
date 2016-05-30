package com.streamer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.streamer.domain.Streams;
import com.streamer.service.StreamsService;
import com.streamer.web.rest.util.HeaderUtil;
import com.streamer.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Streams.
 */
@RestController
@RequestMapping("/api")
public class StreamsResource {

    private final Logger log = LoggerFactory.getLogger(StreamsResource.class);
        
    @Inject
    private StreamsService streamsService;
    
    /**
     * POST  /streams : Create a new streams.
     *
     * @param streams the streams to create
     * @return the ResponseEntity with status 201 (Created) and with body the new streams, or with status 400 (Bad Request) if the streams has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/streams",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Streams> createStreams(@RequestBody Streams streams) throws URISyntaxException {
        log.debug("REST request to save Streams : {}", streams);
        if (streams.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("streams", "idexists", "A new streams cannot already have an ID")).body(null);
        }
        Streams result = streamsService.save(streams);
        return ResponseEntity.created(new URI("/api/streams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("streams", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /streams : Updates an existing streams.
     *
     * @param streams the streams to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated streams,
     * or with status 400 (Bad Request) if the streams is not valid,
     * or with status 500 (Internal Server Error) if the streams couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/streams",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Streams> updateStreams(@RequestBody Streams streams) throws URISyntaxException {
        log.debug("REST request to update Streams : {}", streams);
        if (streams.getId() == null) {
            return createStreams(streams);
        }
        Streams result = streamsService.save(streams);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("streams", streams.getId().toString()))
            .body(result);
    }

    /**
     * GET  /streams : get all the streams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of streams in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/streams",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Streams>> getAllStreams(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Streams");
        Page<Streams> page = streamsService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/streams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /streams/:id : get the "id" streams.
     *
     * @param id the id of the streams to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the streams, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/streams/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Streams> getStreams(@PathVariable Long id) {
        log.debug("REST request to get Streams : {}", id);
        Streams streams = streamsService.findOne(id);
        return Optional.ofNullable(streams)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /streams/:id : delete the "id" streams.
     *
     * @param id the id of the streams to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/streams/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStreams(@PathVariable Long id) {
        log.debug("REST request to delete Streams : {}", id);
        streamsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("streams", id.toString())).build();
    }

}
