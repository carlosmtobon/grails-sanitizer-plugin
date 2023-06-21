package sanitizer

import grails.gorm.validation.Constraint
import org.apache.commons.logging.LogFactory
import org.grails.core.exceptions.GrailsDomainException
import org.grails.datastore.gorm.validation.constraints.factory.ConstraintFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.MessageSource
import org.springframework.util.Assert

/**
 * Constraint Factory that allows dependencies to be injected to Constraints at runtime.
 * @author daniel
 *
 */
class ApplicationContextAwareConstraintFactory implements ConstraintFactory {
	
	private static final log = LogFactory.getLog(this)

	private Class constraintClass
	private ApplicationContext applicationContext
	private List<String> beansToInject = []

	final MessageSource messageSource

	/**
	 * Constructor requires the Application Context containing the beans you would like
	 * to inject, the Class of the Constraint to create, and a list of strings containing
	 * the names of the beans you would like to inject.
	 * <p>You will receive an Illegal Argument Exception if either applicationContext
	 * or constraint is null.
	 * @param applicationContext
	 * @param constraint
	 * @param beansToInject
	 */
    ApplicationContextAwareConstraintFactory(ApplicationContext applicationContext,
                                             Class constraint, MessageSource messageSource, List<String> beansToInject) {
		Assert.notNull applicationContext, "Argument [applicationContext] cannot be null"
		Assert.notNull constraint, "Argument [constraint] cannot be null"
		Assert.isTrue Constraint.isAssignableFrom(constraint), "Argument [constraint] must be an instance of $Constraint.name"

		this.applicationContext = applicationContext
		this.constraintClass = constraint
		this.messageSource = messageSource

		for(String beanName : beansToInject){
			boolean springContained = applicationContext.containsBean(beanName)
			boolean classContained = constraint.metaClass.hasMetaProperty(beanName)

			if(springContained && classContained) {
				this.beansToInject.add(beanName)
			}else{
				String message = springContained ? "" : "Bean ${beanName} does not exist in Application Context - Constraint: "
				message = message + (classContained ? "" : "  Property ${beanName} does not exist for Constraint: ")

				throw new IllegalArgumentException(message + Constraint)
			}
		}
	}

	private void injectDependencies( applicationContext,  constraint, beansToInject){
		for(String beanName : beansToInject){
			constraint[(beanName)] = applicationContext.getBean(beanName)
		}
	}

	/**
	 * Create an instance of the Constraint Class for this factory.  Injects dependencies specified by the Factory Constructor
	 */
	Constraint newInstance(Class owner, String property, Object constrainingValue) {
		try {
			Constraint instance = constraintClass.newInstance(owner, property, constrainingValue, messageSource)

			if(beansToInject){
				injectDependencies(applicationContext, instance, beansToInject)
			}

			return instance
		} catch (InstantiationException e) {
			throw new GrailsDomainException("Error instantiating constraint [" +
				constraintClass + "] during validation: " + e.getMessage(), e )
		} catch (IllegalAccessException e) {
			throw new GrailsDomainException("Error instantiating constraint [" +
				constraintClass + "] during validation: " + e.getMessage(), e )
		}
	}

	@Override
	Class getType() {
		return this.class
	}

	@Override
	String getName() {
		return MarkupConstraint.MARKUP_CONSTRAINT
	}

	@Override
	boolean supports(Class targetType) {
		return true;
	}

	@Override
	Constraint build(Class owner, String property, Object constrainingValue) {
		return newInstance(owner, property, constrainingValue)
	}
}
