#!/usr/bin/groovy
@Library('github.com/rupalibehera/fabric8-pipeline-library@test-pipeline')
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

    stage 'Approve'
    pipeline.approveRelease(stagedProject)

    stage 'Website'
    pipeline.website(stagedProject)

    stage 'Promote'
    pipeline.release(stagedProject)
  }
}
