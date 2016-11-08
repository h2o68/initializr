package io.spring.initializr.generator

import groovy.util.logging.Slf4j;
import groovy.transform.ToString

/**
 * @author marsqing
 *
 * Nov 3, 2016 5:04:41 PM
 */
@Slf4j
@ToString(ignoreNulls = true, includePackage = false, includeNames = true)
class MultipleModuleProjectRequest {

	ProjectRequest parent
	
	List<ProjectRequest> children = []
	
}
