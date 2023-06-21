package sanitizer

import grails.gorm.validation.ConstrainedProperty
import grails.plugins.*
import org.grails.datastore.gorm.validation.constraints.registry.DefaultValidatorRegistry
import org.grails.datastore.gorm.validation.javax.JavaxValidatorRegistry
import org.grails.datastore.mapping.validation.ValidatorRegistry
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader

class SanitizerGrailsPlugin extends Plugin {

    def grailsVersion = "5.1.9 > *"
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]
    def title = "Grails Sanitizer Plugin"
    def author = "Carlos Tobon"
    def authorEmail = "carlos.mauro.tobon@gmail.com"
    def description = '''\
Plugin for Sanitizing Markup(HTML, XHTML, CSS) using OWASP AntiSamy.
Filters malicious content from User generated content (such as that entered through Rich Text boxes).

Features -
* Constraint "markup"
  - can be added to domain/command classes to validate that a string is valid and safe markup
  - important note:  The constraint is for validation only, it does not sanitize the string
* Encoding-only Codec "myText.encodeAsSanitizedMarkup()"
  - use the codec or the service to sanitize the string
  - (the codec uses the service, too)
* MarkupSanitizerService
  - use the codec or the service to sanitize the string
  - access in your controllers/services via
    	def markupSanitizerService
  - method MarkupSanitizerResult sanitize(String dirtyString)
  - effectively a singleton, which means the ruleset only needs to be read once on startup

This module does not sanitize a string that does not contain valid markup.  If it does not contain
valid markup, it will simply return an empty string.
'''
    def profiles = ['web']
    def documentation = "TBD"
    def license = "MIT"
    def developers = [ [ name: "Charles Amat", email: "charlesamat@gmail.com" ],
                       [ name: "Daniel Bower", email: "daniel@bowerstudios.com" ] ]
    def issueManagement = [ system: "GitHub Issues", url: "https://github.com/carlosmtobon/grails-5-sanitizer/issues" ]
    def scm = [ url: "https://github.com/carlosmtobon/grails-5-sanitizer/" ]

    Closure doWithSpring() { { ->
        policyFileResource(ClassPathResource, application.config.sanitizer.config?:'does not exist')
        markupSanitizerService(MarkupSanitizerService, ref('policyFileResource'))
    }
    }

    void doWithDynamicMethods() {

    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }

    void doWithApplicationContext() {

        def myBean = applicationContext.getBean(ValidatorRegistry)
        def factory = new ApplicationContextAwareConstraintFactory(
                applicationContext, MarkupConstraint, applicationContext.messageSource, ["markupSanitizerService"])
        ((JavaxValidatorRegistry)myBean).addConstraintFactory(factory)

    }
}
