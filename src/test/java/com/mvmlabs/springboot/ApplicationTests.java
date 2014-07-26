package com.mvmlabs.springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mvmlabs.springboot.Application;

/**
 * For testing to work as-is, out of the box, I had to introduce a configuration class
 * that replaces the Apache Tiles configurer with one that locates the configuration file
 * as a file system resource relative to the project root directory. Other than that, this
 * is the test created by Spring Source Tool Suite.
 * 
 * @author Mark Meany
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class, TestConfigurtaion.class})
@WebAppConfiguration
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}
}
