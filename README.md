# Grails Sanitizer Plugin
A Grails Plugin for sanitizing markup using the OWASP AntiSamy library.
# Installation
To use the Grails Sanitizer Plugin, add the following dependency to your `build.gradle` file:
```groovy
implementation 'org.grails.plugins:sanitizer:1.0.0'
```
# Features
The Grails Sanitizer plugin provides a convenient way to sanitize markup (HTML, XHTML, CSS) and protect against Cross-Site Scripting (XSS) attacks using OWASP AntiSamy library.
- Configurable Rulesets in `/src/groovy/antisamyconfigs/antisamy-policy.xml`
- A custom `markup` constraint to validates that a string is valid/safe markup.
- Encoding-only Codec (e.g., `myText.encodeAsSanitizedMarkup()`)
- Markup Santizier Service (e.g., `markupSanitizerService.sanitize(input)`)
# Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request on GitHub.
# License
This plugin is released under the MIT License. See the [LICENSE.txt](LICENSE.txt) file for more information.
