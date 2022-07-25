package com.omanski.recruitment.service;

import com.omanski.recruitment.configuration.RedisConfig;
import com.omanski.recruitment.model.Airport;
import com.omanski.recruitment.model.GeoPosition;
import com.omanski.recruitment.repository.AirportsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({RedisConfig.class, AirportService.class})
@ExtendWith(SpringExtension.class)
@EnableCaching
@ImportAutoConfiguration(classes = {
        CacheAutoConfiguration.class,
        RedisAutoConfiguration.class
})
public class AirportServiceCachingTest {

    @MockBean
    private AirportsRepository airportsRepository;

    @Autowired
    private AirportService airportService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void redisCachingTest() {
        //given
        List<Airport> airportsList = Arrays.asList(
                new Airport("uTUclIFUdn", 4563496, 106, "LeufsTo", "yvjPMgmdYKSiEJUTKFSr",
                        "EZP", "AwOFMFUW", "nIkaLW",
                        new GeoPosition(-11.26171f, -71.221405f),
                        723086, false, "EB", false, 6988),
                new Airport("kscRyPiwpi", 47254818, 7852, "QyYTWOK", "xRywzQQoLnRFZurRVxQz",
                        "ARN", "GsPlTNoE", "HVJbNx",
                        new GeoPosition(-64.0f, -71.0f),
                        276887, false, "WN", true, 17),
                new Airport("OSYBUckTNi", 12636970, 8802, "aQMfmKu", "xUTkERZoGvOAQdvqoWhD",
                        "BWW", "sDqBOnfo", "oDdvEN",
                        new GeoPosition(-19.52464f, -84.685905f),
                        468830, false, "UX", false, 2614)
        );

        Mockito
                .when(airportsRepository.findAll())
                .thenReturn(airportsList);

        //when
        List<Airport> airportsFromRepo = airportService.getAirports();
        List<Airport> airportsFromCache = airportService.getAirports();

        //then
        assertThat(airportsFromRepo).isEqualTo(airportsList);
        assertThat(airportsFromCache).isEqualTo(airportsList);

        verify(airportsRepository, times(1)).findAll();
        assertThat(itemFromCache()).isEqualTo(airportsList);

        //when
        airportService.save(airportsList.get(0));
        airportsFromRepo = airportService.getAirports();

        //then
        assertThat(airportsFromRepo).isEqualTo(airportsList);
        assertThat(airportsFromCache).isEqualTo(airportsList);

        verify(airportsRepository, times(2)).findAll();
        assertThat(itemFromCache()).isEqualTo(airportsList);
    }

    private Object itemFromCache() {
        return cacheManager.getCache("airports").get("airportsList").get();
    }

    @TestConfiguration
    static class EmbeddedRedisConfiguration {

        private final RedisServer redisServer;

        public EmbeddedRedisConfiguration() {
            this.redisServer = new RedisServer();
        }

        @PostConstruct
        public void startRedis() {
            redisServer.start();
        }

        @PreDestroy
        public void stopRedis() {
            this.redisServer.stop();
        }
    }


}
