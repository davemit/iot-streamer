package com.streamer.web.rest;

import com.streamer.StreamerApp;
import com.streamer.domain.Streams;
import com.streamer.repository.StreamsRepository;
import com.streamer.service.StreamsService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StreamsResource REST controller.
 *
 * @see StreamsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StreamerApp.class)
@WebAppConfiguration
@IntegrationTest
public class StreamsResourceIntTest {

    private static final String DEFAULT_PUBURL = "AAAAA";
    private static final String UPDATED_PUBURL = "BBBBB";
    private static final String DEFAULT_PUBKEY = "AAAAA";
    private static final String UPDATED_PUBKEY = "BBBBB";
    private static final String DEFAULT_PRIVATEKEY = "AAAAA";
    private static final String UPDATED_PRIVATEKEY = "BBBBB";
    private static final String DEFAULT_DELETEKEY = "AAAAA";
    private static final String UPDATED_DELETEKEY = "BBBBB";
    private static final String DEFAULT_FORMAT = "AAAAA";
    private static final String UPDATED_FORMAT = "BBBBB";
    private static final String DEFAULT_EXAMPLE = "AAAAA";
    private static final String UPDATED_EXAMPLE = "BBBBB";

    @Inject
    private StreamsRepository streamsRepository;

    @Inject
    private StreamsService streamsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStreamsMockMvc;

    private Streams streams;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StreamsResource streamsResource = new StreamsResource();
        ReflectionTestUtils.setField(streamsResource, "streamsService", streamsService);
        this.restStreamsMockMvc = MockMvcBuilders.standaloneSetup(streamsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        streams = new Streams();
        streams.setPuburl(DEFAULT_PUBURL);
        streams.setPubkey(DEFAULT_PUBKEY);
        streams.setPrivatekey(DEFAULT_PRIVATEKEY);
        streams.setDeletekey(DEFAULT_DELETEKEY);
        streams.setFormat(DEFAULT_FORMAT);
        streams.setExample(DEFAULT_EXAMPLE);
    }

    @Test
    @Transactional
    public void createStreams() throws Exception {
        int databaseSizeBeforeCreate = streamsRepository.findAll().size();

        // Create the Streams

        restStreamsMockMvc.perform(post("/api/streams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(streams)))
                .andExpect(status().isCreated());

        // Validate the Streams in the database
        List<Streams> streams = streamsRepository.findAll();
        assertThat(streams).hasSize(databaseSizeBeforeCreate + 1);
        Streams testStreams = streams.get(streams.size() - 1);
        assertThat(testStreams.getPuburl()).isEqualTo(DEFAULT_PUBURL);
        assertThat(testStreams.getPubkey()).isEqualTo(DEFAULT_PUBKEY);
        assertThat(testStreams.getPrivatekey()).isEqualTo(DEFAULT_PRIVATEKEY);
        assertThat(testStreams.getDeletekey()).isEqualTo(DEFAULT_DELETEKEY);
        assertThat(testStreams.getFormat()).isEqualTo(DEFAULT_FORMAT);
        assertThat(testStreams.getExample()).isEqualTo(DEFAULT_EXAMPLE);
    }

    @Test
    @Transactional
    public void getAllStreams() throws Exception {
        // Initialize the database
        streamsRepository.saveAndFlush(streams);

        // Get all the streams
        restStreamsMockMvc.perform(get("/api/streams?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(streams.getId().intValue())))
                .andExpect(jsonPath("$.[*].puburl").value(hasItem(DEFAULT_PUBURL.toString())))
                .andExpect(jsonPath("$.[*].pubkey").value(hasItem(DEFAULT_PUBKEY.toString())))
                .andExpect(jsonPath("$.[*].privatekey").value(hasItem(DEFAULT_PRIVATEKEY.toString())))
                .andExpect(jsonPath("$.[*].deletekey").value(hasItem(DEFAULT_DELETEKEY.toString())))
                .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
                .andExpect(jsonPath("$.[*].example").value(hasItem(DEFAULT_EXAMPLE.toString())));
    }

    @Test
    @Transactional
    public void getStreams() throws Exception {
        // Initialize the database
        streamsRepository.saveAndFlush(streams);

        // Get the streams
        restStreamsMockMvc.perform(get("/api/streams/{id}", streams.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(streams.getId().intValue()))
            .andExpect(jsonPath("$.puburl").value(DEFAULT_PUBURL.toString()))
            .andExpect(jsonPath("$.pubkey").value(DEFAULT_PUBKEY.toString()))
            .andExpect(jsonPath("$.privatekey").value(DEFAULT_PRIVATEKEY.toString()))
            .andExpect(jsonPath("$.deletekey").value(DEFAULT_DELETEKEY.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.example").value(DEFAULT_EXAMPLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStreams() throws Exception {
        // Get the streams
        restStreamsMockMvc.perform(get("/api/streams/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStreams() throws Exception {
        // Initialize the database
        streamsService.save(streams);

        int databaseSizeBeforeUpdate = streamsRepository.findAll().size();

        // Update the streams
        Streams updatedStreams = new Streams();
        updatedStreams.setId(streams.getId());
        updatedStreams.setPuburl(UPDATED_PUBURL);
        updatedStreams.setPubkey(UPDATED_PUBKEY);
        updatedStreams.setPrivatekey(UPDATED_PRIVATEKEY);
        updatedStreams.setDeletekey(UPDATED_DELETEKEY);
        updatedStreams.setFormat(UPDATED_FORMAT);
        updatedStreams.setExample(UPDATED_EXAMPLE);

        restStreamsMockMvc.perform(put("/api/streams")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStreams)))
                .andExpect(status().isOk());

        // Validate the Streams in the database
        List<Streams> streams = streamsRepository.findAll();
        assertThat(streams).hasSize(databaseSizeBeforeUpdate);
        Streams testStreams = streams.get(streams.size() - 1);
        assertThat(testStreams.getPuburl()).isEqualTo(UPDATED_PUBURL);
        assertThat(testStreams.getPubkey()).isEqualTo(UPDATED_PUBKEY);
        assertThat(testStreams.getPrivatekey()).isEqualTo(UPDATED_PRIVATEKEY);
        assertThat(testStreams.getDeletekey()).isEqualTo(UPDATED_DELETEKEY);
        assertThat(testStreams.getFormat()).isEqualTo(UPDATED_FORMAT);
        assertThat(testStreams.getExample()).isEqualTo(UPDATED_EXAMPLE);
    }

    @Test
    @Transactional
    public void deleteStreams() throws Exception {
        // Initialize the database
        streamsService.save(streams);

        int databaseSizeBeforeDelete = streamsRepository.findAll().size();

        // Get the streams
        restStreamsMockMvc.perform(delete("/api/streams/{id}", streams.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Streams> streams = streamsRepository.findAll();
        assertThat(streams).hasSize(databaseSizeBeforeDelete - 1);
    }
}
