#!/usr/bin/env groovy

import hudson.model.*
//import hudson.EnvVars
import groovy.json.JsonSlurperClassic
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.net.URL

env.M2_HOME = "/data/admin/jenkins/.jenkins/tools/apache-maven-3.5.0"
env.JAVA_HOME = '/opt/jdk1.8.0_72'
env.TOMCAT_HOME = '/opt/tomcat'
env.Git_URL='https://github.com/himanshubhardwaj87/petclinicJavaProject.git'

def envVariables() {
	env.PATH = "${M2_HOME}/bin:${env.PATH}"
	env.JAVA_HOME = "$JAVA_HOME"
	env.PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
	env.PATH = "${TOMCAT_HOME}/bin:${env.PATH}"
}

def CodeCheckout() {
	def WORKSPACE = pwd()
	echo  "\u2600 **********SVN Code Checkout Stage Begins*******,Git_URL=> ${Git_URL}"
	sh "rm -rf *"
	scm checkout
	stash 'codefiles'
}

def CodeBuild() {
	def PROJECT_PATH = "petclinicJavaProject"
	echo  "\u2601 **********Build started ******************"
	dir(PROJECT_PATH){
		sh 'mvn clean package'
		step([$class: 'ArtifactArchiver', artifacts: '**/target/*.war', fingerprint: true])
	}
}

def SonarExecution() {
	def PROJECT_PATH = "petclinicJavaProject"
	echo  "\u2602 **********Sonar Execution started******************"
	dir(PROJECT_PATH){ sh 'mvn sonar:sonar' }
}

def CodeDeploy() {
	echo  "\u2603 **********Code Deployment started******************"
	def PROJECT_PATH = "petclinicJavaProject"
	def exists = fileExists '${TOMCAT_HOME}/webapps*.war'
	dir(PROJECT_PATH) {
		if(fileExists ('/opt/tomcat/webapps/petclinic.war')) {
			echo "*******War file already exists******"
			//clean env		
					sh '''rm -rf ${TOMCAT_HOME}temp/*
			
						  rm -rf ${TOMCAT_HOME}work/*
			
						  rm -rf ${TOMCAT_HOME}webapps/*
			
						 '''
					sh  '''Pid=$(netstat -nlp | grep 8080 | awk '{print $7}' | awk -F"/" '{ print $1 }')
						   if [ ! $Pid ]; then
							   echo "PID is NONE"
						   else
							   echo  $Pid
							   kill -9 $Pid
						   fi
						   '''
			sh "cp -r target/*.war ${TOMCAT_HOME}webapps"
			sh "sleep 5"
			sh "${TOMCAT_HOME}/bin/startup.sh"
			sh "sleep 5"
		}
		else {
			echo "*******War file not exist******"
			sh "${TOMCAT_HOME}/bin/shutdown.sh"
			sh "sleep 5"
			sh "cp -r target/*.war ${TOMCAT_HOME}"
			sh "sleep 5"
			sh "${TOMCAT_HOME}/bin/startup.sh"
			sh "sleep 5"
		}
	}
}
def ExecuteSeleniumTests() {
	node('Devops_POC_Linux') {
		unstash 'codefiles'
		def PROJECT_PATH = "PetClinic_selenium"
		echo  "\u2604 **********Selenium Tests started******************"
		dir(PROJECT_PATH){
			sh 'mvn test'			
		}
		PublishHtmlSelReport()
	}
}

def ExecutePerformanceTests() {
	node('Devops_POC_Linux') {
		unstash 'codefiles'
		def PROJECT_PATH = "PetClinic_Jmeter"
		echo  "\u2605 **********Performance Tests started******************"
		dir(PROJECT_PATH){
			sh 'mvn verify -Pperformance'			
		}
		PublishHtmlPerformanceReport()
	}
}

def PublishHtmlSelReport()
{
	def PROJECT_PATH = "PetClinic_selenium"
	dir(PROJECT_PATH){
		publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/surefire-reports', reportFiles: 'emailable-report.html', reportName: 'Selenium Test HTML Report'])
	}
}

def PublishHtmlJacocoReport()
{
	def PROJECT_PATH = "petclinicJavaProject"
	dir(PROJECT_PATH){
		publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/site/jacoco', reportFiles: 'index.html', reportName: 'Jacoco Test HTML Report'])
	}
}

def PublishHtmlPerformanceReport()
{
	def PROJECT_PATH = "PetClinic_Jmeter"
	dir(PROJECT_PATH){
		publishHTML([allowMissing: true, alwaysLinkToLastBuild: true, keepAll: true, reportDir: 'target/reports', reportFiles: 'index.html', reportName: 'Performance Test HTML Report'])
	}
}
/********************************************************
 *****             Main pipeline stages              *****
 *********************************************************/

node('Devops_POC_Linux') {
	envVariables()
	stage '\u2780 Code Checkout from Git'
	CodeCheckout()
	stage '\u2781 Code Build'
	CodeBuild()
	stage '\u2782 Sonar Execution'
	SonarExecution()
	PublishHtmlJacocoReport()
	stage '\u2783 Deploy Build'
	CodeDeploy()

}
stage '\u2784 Automation Test Execution'
parallel SeleniumTesting: {
	ExecuteSeleniumTests()
}, PerformanceTesting: {
	ExecutePerformanceTests()
}