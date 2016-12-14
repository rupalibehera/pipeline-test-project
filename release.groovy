#!/usr/bin/groovy

def imagesBuiltByPipline() {
  return ['pipeline-test-project']
}

def externalImages() {
  return ['pipeline-test-external-image']
}

def repo() {
  return 'fabric8io/pipeline-test-project'
}

def stage() {
  return stageProject {
    project = repo()
    useGitTagForNextVersion = true
    extraImagesToStage = externalImages()
  }
}

def deploy(project) {
  //deployProject{
  //  stagedProject = project
  //  resourceLocation = 'target/classes/kubernetes.json'
  //  environment = 'fabric8'
  //}
  echo 'unable to deploy on plain kuberentes see https://github.com/fabric8io/kubernetes-client/issues/437'
}

def approveRelease(project) {
  def releaseVersion = project[1]
  approve {
    room = null
    version = releaseVersion
    console = null
    environment = 'fabric8'
  }
}

def release(project) {
  releaseProject {
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8'
    githubOrganisation = 'fabric8io'
    artifactIdToWatchInCentral = 'pipeline-test-project'
    artifactExtensionToWatchInCentral = 'jar'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = imagesBuiltByPipline()
    extraImagesToTag = externalImages()
  }
}

def mergePullRequest(prId) {
  mergeAndWaitForPullRequest {
    project = repo()
    pullRequestId = prId
  }

}

def documentation(project) {
  helmPush = false
  def releaseVersion = project[1]
  Model m = readMavenPom file: 'pom.xml'
  // checkout latest the release version
  checkout scm: [$class          : 'GitSCM',
                 useRemoteConfigs: [[url: 'https://github.com/' + repo()]],
                 branches        : [[name: 'refs/tags/v' + releaseVersion]]],
    changelog: false, poll: false
  // Run documentation goals
  sh 'mvn -Pdoc-html'
  sh 'mvn -Pdoc-pdf'
  // now clone the gh-pages
  sh 'git clone -b gh-pages' + 'https://github.com/' + repo() + ' gh-pages'
  sh 'cp -rv target/generated-docs/* gh-pages/'
  sh 'cd gh-pages'
  sh 'mv index.pdf ' + m.artifactId + '.pdf'
  sh 'git add --ignore-errors *'
  sh 'git commit -m "generated documentation'
  sh 'git push origin gh-pages'
}

return this;
