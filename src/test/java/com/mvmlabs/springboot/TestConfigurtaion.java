package com.mvmlabs.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;

/**
 * Unit tests were failing because could not locate tiles.xml, so swap out the configurer with
 * one that knows the relative location of the file.
 * 
 * @author Mark Meany
 */
@Configuration
public class TestConfigurtaion {

    /**
     * Configure tiles using a filesystem location for the configuration file rather than a
     * URL based location.
     * 
     * @return tiles configurer
     */
	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions(new String[] { "file:src/main/webapp/WEB-INF/tiles/tiles.xml" });
		configurer.setCheckRefresh(true);
		return configurer;
	}

}
