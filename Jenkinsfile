#!/usr/bin/groovy
@Library('github.com/iocanel/fabric8-pipeline-library@master')
def test = 'dummy'
maven {
  checkout scm
  sh "git remote set-url origin git@github.com:fabric8io/pipeline-test-project.git"

  def pipeline = load 'release.groovy'

  stage 'Stage'
  def stagedProject = pipeline.stage()
  
  stage 'Deploy'
  pipeline.deploy(stagedProject)
  
  stage 'Approve'
  pipeline.approveRelease(stagedProject)
  
  stage 'Promote'
  pipeline.release(stagedProject)
  
}
