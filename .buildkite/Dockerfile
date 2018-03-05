FROM quay.io/vital/scala:jdk-master

ADD project /app/project
ADD build.sbt /app

# Grab any updated dependencies
RUN sbt update

# Add rest of sources
ADD . /app
