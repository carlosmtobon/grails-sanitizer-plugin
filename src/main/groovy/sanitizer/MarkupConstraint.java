package sanitizer;


/**
 * This constraint will trigger an error only if the html that is input is invalid.
 * It will return an error message with the problem that it found.
 *
 * <p>Be sure to still use the sanitizer service or codec in order to actually remove content
 * not on the whitelist.
 * </p>
 */

import org.springframework.validation.Errors;
import org.grails.datastore.gorm.validation.constraints.AbstractConstraint;
import org.springframework.context.MessageSource;

class MarkupConstraint extends AbstractConstraint {

    MarkupSanitizerService markupSanitizerService;

    public static final String MARKUP_CONSTRAINT = "markup";
    private final boolean markup;

    public MarkupConstraint(Class<?> constraintOwningClass, String constraintPropertyName, Object constraintParameter, MessageSource messageSource) {
        super(constraintOwningClass, constraintPropertyName, constraintParameter, messageSource);
        this.markup = (boolean) this.constraintParameter;
    }

    @Override
    protected java.lang.Object validateParameter(java.lang.Object constraintParameter) {
        return constraintParameter;
    }

//    @java.lang.Override
//    protected void processValidate(java.lang.Object target, java.lang.Object propertyValue, Errors errors) {
//        if (!markup) return;
//
//        boolean isValid = false;
//        // Custom validation logic here
//
//        if (propertyValue instanceof String) {
//            isValid = !((String)propertyValue).matches("(?s).*<[^>]+>.*");
//        } else {
//            isValid = true;
//        }
//        if (!isValid) {
//            rejectValue(target, errors, "default.invalid.email.message","No bueno", new Object[]{});
//        }
//    }

    @Override
    protected void processValidate(java.lang.Object target, java.lang.Object propertyValue, Errors errors) {
        if (!markup) return;
        MarkupSanitizerResult result = markupSanitizerService.sanitize((String) propertyValue);

        if (result.isInvalid()) {

            String errorMesg = (null != result.getErrorMessages()) ? result.getErrorMessages().toString() : "Invalid Markup entered";

            Object[] args = {constraintPropertyName, constraintOwningClass, propertyValue};

            rejectValueWithDefaultMessage(target, errors, errorMesg, new String[0], args);
        }
    }


    @Override
    public boolean supports(java.lang.Class type) {
        return type != null && String.class.isAssignableFrom(type);
    }

    @Override
    public java.lang.String getName() {
        return MARKUP_CONSTRAINT;
    }
}
