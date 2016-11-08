/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.web.support

import groovy.json.JsonBuilder;
import groovy.util.logging.Slf4j
import io.spring.initializr.metadata.DefaultMetadataElement
import io.spring.initializr.metadata.InitializrMetadata
import io.spring.initializr.metadata.InitializrMetadataProvider

import org.springframework.cache.annotation.Cacheable
import org.springframework.web.client.RestTemplate
import groovy.json.JsonSlurper

/**
 * A default {@link InitializrMetadataProvider} that is able to refresh
 * the metadata with the status of the main spring.io site.
 *
 * @author Stephane Nicoll
 * @since 1.0
 */
@Slf4j
class DefaultInitializrMetadataProvider implements InitializrMetadataProvider {

	private final InitializrMetadata metadata
	private final RestTemplate restTemplate

	DefaultInitializrMetadataProvider(InitializrMetadata metadata, RestTemplate restTemplate) {
		this.metadata = metadata
		this.restTemplate = restTemplate
	}

	@Override
	@Cacheable(value = 'initializr', key = "'metadata'")
	InitializrMetadata get() {
		updateInitializrMetadata(metadata)
		metadata
	}

	protected void updateInitializrMetadata(InitializrMetadata metadata) {
		def bootVersions = fetchBootVersions()
		if (bootVersions) {
			if (!bootVersions.find { it.default }) { // No default specified
				bootVersions[0].default = true
			}
			metadata.bootVersions.content.clear()
			metadata.bootVersions.content.addAll(bootVersions)
		}
	}

	protected List<DefaultMetadataElement> fetchBootVersions() {
		def json = '''
			{
  "id": "spring-boot",
  "name": "Spring Boot",
  "repoUrl": "https://github.com/spring-projects/spring-boot",
  "siteUrl": "http://projects.spring.io/spring-boot",
  "category": "active",
  "stackOverflowTags": "spring-boot",
  "projectReleases": [
    {
      "releaseStatus": "SNAPSHOT",
      "refDocUrl": "http://docs.spring.io/spring-boot/docs/2.0.0.BUILD-SNAPSHOT/reference/htmlsingle/",
      "apiDocUrl": "http://docs.spring.io/spring-boot/docs/2.0.0.BUILD-SNAPSHOT/api/",
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot",
      "repository": {
        "id": "spring-snapshots",
        "name": "Spring Snapshots",
        "url": "https://repo.spring.io/libs-snapshot",
        "snapshotsEnabled": true
      },
      "generalAvailability": false,
      "preRelease": false,
      "versionDisplayName": "2.0.0",
      "current": false,
      "snapshot": true,
      "version": "2.0.0.BUILD-SNAPSHOT"
    },
    {
      "releaseStatus": "SNAPSHOT",
      "refDocUrl": "http://docs.spring.io/spring-boot/docs/1.5.0.BUILD-SNAPSHOT/reference/htmlsingle/",
      "apiDocUrl": "http://docs.spring.io/spring-boot/docs/1.5.0.BUILD-SNAPSHOT/api/",
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot",
      "repository": {
        "id": "spring-snapshots",
        "name": "Spring Snapshots",
        "url": "https://repo.spring.io/libs-snapshot",
        "snapshotsEnabled": true
      },
      "generalAvailability": false,
      "preRelease": false,
      "versionDisplayName": "1.5.0",
      "current": false,
      "snapshot": true,
      "version": "1.5.0.BUILD-SNAPSHOT"
    },
    {
      "releaseStatus": "SNAPSHOT",
      "refDocUrl": "http://docs.spring.io/spring-boot/docs/1.4.2.BUILD-SNAPSHOT/reference/htmlsingle/",
      "apiDocUrl": "http://docs.spring.io/spring-boot/docs/1.4.2.BUILD-SNAPSHOT/api/",
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot",
      "repository": {
        "id": "spring-snapshots",
        "name": "Spring Snapshots",
        "url": "https://repo.spring.io/libs-snapshot",
        "snapshotsEnabled": true
      },
      "generalAvailability": false,
      "preRelease": false,
      "versionDisplayName": "1.4.2",
      "current": false,
      "snapshot": true,
      "version": "1.4.2.BUILD-SNAPSHOT"
    },
    {
      "releaseStatus": "GENERAL_AVAILABILITY",
      "refDocUrl": "http://docs.spring.io/spring-boot/docs/1.4.1.RELEASE/reference/htmlsingle/",
      "apiDocUrl": "http://docs.spring.io/spring-boot/docs/1.4.1.RELEASE/api/",
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot",
      "repository": null,
      "generalAvailability": true,
      "preRelease": false,
      "versionDisplayName": "1.4.1",
      "current": true,
      "snapshot": false,
      "version": "1.4.1.RELEASE"
    },
    {
      "releaseStatus": "SNAPSHOT",
      "refDocUrl": "http://docs.spring.io/spring-boot/docs/1.3.9.BUILD-SNAPSHOT/reference/htmlsingle/",
      "apiDocUrl": "http://docs.spring.io/spring-boot/docs/1.3.9.BUILD-SNAPSHOT/api/",
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot",
      "repository": {
        "id": "spring-snapshots",
        "name": "Spring Snapshots",
        "url": "https://repo.spring.io/libs-snapshot",
        "snapshotsEnabled": true
      },
      "generalAvailability": false,
      "preRelease": false,
      "versionDisplayName": "1.3.9",
      "current": false,
      "snapshot": true,
      "version": "1.3.9.BUILD-SNAPSHOT"
    },
    {
      "releaseStatus": "GENERAL_AVAILABILITY",
      "refDocUrl": "http://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/reference/htmlsingle/",
      "apiDocUrl": "http://docs.spring.io/spring-boot/docs/1.3.8.RELEASE/api/",
      "groupId": "org.springframework.boot",
      "artifactId": "spring-boot",
      "repository": null,
      "generalAvailability": true,
      "preRelease": false,
      "versionDisplayName": "1.3.8",
      "current": false,
      "snapshot": false,
      "version": "1.3.8.RELEASE"
    }
  ],
  "aggregator": false,
  "stackOverflowTagList": [
    "spring-boot"
  ]
}
		'''
		
		new JsonSlurper().parseText(json).projectReleases.collect {
			def version = new DefaultMetadataElement()
			version.id = it.version
			def name = it.versionDisplayName
			version.name = (it.snapshot ? name + ' (SNAPSHOT)' : name)
			version.setDefault(it.current)
			version
		}
	}
	
	/*
	protected List<DefaultMetadataElement> fetchBootVersions() {
		def url = metadata.configuration.env.springBootMetadataUrl
		if (url) {
			try {
				log.info("Fetching boot metadata from $url")
				//return new SpringBootMetadataReader(restTemplate, url).bootVersions
				def bootVersions = new SpringBootMetadataReader(restTemplate, url).bootVersions
				log.info(new JsonBuilder(bootVersions))
				bootVersions
			} catch (Exception e) {
				log.warn('Failed to fetch spring boot metadata', e)
			}
		}
		null
	}
	*/

}
