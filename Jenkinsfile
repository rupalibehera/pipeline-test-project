#!/usr/bin/groovy
@Library('github.com/kameshsampath/fabric8-pipeline-library@gen-website-docs')
def dummy
mavenNode {
  ws {
    checkout scm
    sh "git remote set-url origin git@github.com:kameshsampath/pipeline-test-project.git"

    def pipeline = load 'release.groovy'

    stage 'Stage'
    def stagedProject = pipeline.stage()

    //stage 'Deploy'
    //pipeline.deploy(stagedProject)

    //stage 'Approve'
    //pipeline.approveRelease(stagedProject)

    stage 'Documentation'
    pipeline.documentation(stagedProject)

    //stage 'Promote'
    //pipeline.release(stagedProject)

  }
}
