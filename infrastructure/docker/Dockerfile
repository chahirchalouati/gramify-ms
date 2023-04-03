# run command below to build image
# docker build -f Dockerfile . -t ghcr.io/chahirchalouati/github-runner:1.0.1
FROM debian:buster-slim

ARG GITHUB_RUNNER_VERSION="2.301.1"
ARG MAVEN_VERSION=3.6.3

ENV GITHUB_PAT ""
ENV GITHUB_OWNER ""
ENV GITHUB_REPOSITORY ""

ENV RUNNER_NAME "runner"
ENV RUNNER_WORKDIR "_work"

LABEL org.opencontainers.image.source https://github.com/chahirchalouati/gramify-ms

RUN apt-get update \
    && apt-get install -y \
        curl \
        sudo \
        git \
        jq \
        wget \
        pgp \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && useradd -m github \
    && usermod -aG sudo github \
    && echo "%sudo ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers

RUN wget https://downloads.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    && tar -zxvf apache-maven-$MAVEN_VERSION-bin.tar.gz \
    && rm apache-maven-$MAVEN_VERSION-bin.tar.gz \
    && mv apache-maven-$MAVEN_VERSION /usr/lib/mvn

ENV MAVEN_HOME /usr/lib/mvn
ENV PATH $MAVEN_HOME/bin:$PATH

USER github
WORKDIR /home/github

RUN curl -Ls https://github.com/actions/runner/releases/download/v${GITHUB_RUNNER_VERSION}/actions-runner-linux-x64-${GITHUB_RUNNER_VERSION}.tar.gz | tar xz \
    && sudo ./bin/installdependencies.sh

COPY --chown=github:github entrypoint.sh ./entrypoint.sh

# Install dos2unix using sudo
RUN sudo apt-get update && sudo apt-get install -y dos2unix
RUN sudo dos2unix ./entrypoint.sh
RUN sudo chmod u+x ./entrypoint.sh

ENTRYPOINT ["/home/github/entrypoint.sh"]
