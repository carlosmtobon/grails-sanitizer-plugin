# Grails Sanitizer Plugin
The Grails Sanitizer plugin provides a convenient way to sanitize markup (HTML, XHTML, CSS) and protect against Cross-Site Scripting (XSS) attacks using OWASP AntiSamy library.
# Features
- Configurable Rulesets in `/src/groovy/antisamyconfigs/antisamy-policy.xml`
- A custom `markup` constraint that can be added to Domain/Command classes to validate that a string is valid/safe markup.
- Encoding-only Codec (e.g., `myText.encodeAsSanitizedMarkup()`)
- Markup Santizier Service (e.g., `markupSanitizerService.sanitize(input)`)
# Contributing
Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request on GitHub.
# License
This plugin is released under the MIT License. See the [LICENSE.txt](LICENSE.txt) file for more information.
