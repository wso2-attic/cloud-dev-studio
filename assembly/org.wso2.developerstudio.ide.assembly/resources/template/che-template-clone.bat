#!/bin/bash
# import multiple remote git repositories to local CODE dir

# settings / change this to your config


# if no output from the remote ssh cmd, bail out

cd target\3.9.2\temp

IF EXIST che-templates (
    echo che-templates exist
) ELSE (
    mkdir che-templates
)

cd che-templates

git clone https://github.com/codenvy-templates/web-jsp-java-basic.git
git clone https://github.com/codenvy-templates/web-spring-java-simple.git
git clone https://github.com/codenvy-templates/web-gwt-java-simple.git
git clone https://github.com/codenvy-templates/web-grails-java-simple.git
git clone https://github.com/codenvy-templates/web-jsf-java-simple.git
git clone https://github.com/codenvy-templates/web-lift-java-simple.git
git clone https://github.com/codenvy-templates/web-struts-java-simple.git
git clone https://github.com/codenvy-templates/web-vaadin-java-simple.git
git clone https://github.com/codenvy-templates/web-tapestry-java-simple.git
git clone https://github.com/codenvy-templates/web-wicket-java-simple.git
git clone https://github.com/codenvy-templates/web-groovy-java-simple.git
git clone https://github.com/codenvy-templates/web-jruby-java-simple.git
git clone https://github.com/codenvy-templates/web-jython-java-simple.git
git clone https://github.com/codenvy-templates/web-shale-java-simple.git
git clone https://github.com/codenvy-templates/web-velocity-java-simple.git