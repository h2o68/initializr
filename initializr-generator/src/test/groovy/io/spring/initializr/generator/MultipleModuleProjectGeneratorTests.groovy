package io.spring.initializr.generator

import io.spring.initializr.metadata.Dependency
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.SimpleInitializrMetadataProvider

import org.junit.Before
import org.junit.Rule;
import org.junit.Test
import org.junit.rules.TemporaryFolder;
import org.springframework.context.ApplicationEventPublisher

import static org.mockito.Mockito.mock

import io.spring.initializr.test.metadata.InitializrMetadataTestBuilder

/**
 * @author marsqing
 *
 * Nov 3, 2016 10:04:10 PM
 */
class MultipleModuleProjectGeneratorTests {
	
	protected final ProjectGenerator projectGenerator = new ProjectGenerator()
	
	private final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher)
	
	@Before
	void setup() {
		def web = new Dependency(id: 'web')
		web.facets << 'web'
		def hermes = new Dependency(id: 'hermes')
		def metadata = InitializrMetadataTestBuilder.withDefaults()
				.addDependencyGroup('web', web)
				.addDependencyGroup('test', 'security', 'data-jpa', 'aop', 'batch', 'integration').build()
		applyMetadata(metadata)
		projectGenerator.eventPublisher = eventPublisher
		projectGenerator.requestResolver = new ProjectRequestResolver([])
		projectGenerator.tmpdir = '/Users/marsqing/tmp/initializr/'
	}
	
	@Test
	void test() {
		def req = new MultipleModuleProjectRequest()
		req.parent = createProjectRequest('web')
		req.children << createProjectRequest('aop')
		projectGenerator.generateMultipleModuleProjectStructure(req)
	}
	
	void applyMetadata(InitializrMetadata metadata) {
		projectGenerator.metadataProvider = new SimpleInitializrMetadataProvider(metadata)
	}
	
	ProjectRequest createProjectRequest(String... styles) {
		def request = new ProjectRequest()
		request.initialize(projectGenerator.metadataProvider.get())
		request.style.addAll Arrays.asList(styles)
		request
	}
}
