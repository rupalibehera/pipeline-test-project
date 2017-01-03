#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')
def dummy
mavenNode {
  dockerNode {
    checkout scm
    sh "git remote set-url origin git@github.com:fabric8io/pipeline-test-project.git"

    def pipeline = load 'release.groovy'

    stage 'Stage'
    def stagedProject = pipeline.stage()

    stage 'Deploy'
    pipeline.deploy(stagedProject)

// Temporarily commented out since our proxy based approach doesn't allow for console based approval
//   stage 'Approve'
//   pipeline.approveRelease(stagedProject)

    stage 'Website'
    pipeline.website(stagedProject)

    stage 'Promote'
    pipeline.release(stagedProject)
  }
}
