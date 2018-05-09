pipeline {
    agent any

   stages {
           stage('Build') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo "Building.."
                   sh "/opt/maven/bin/mvn clean package -Dmaven.test.skip=true"
               }
           }
           stage('Test') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo 'Testing..'
               }
           }
           stage('Deploy') {
               steps {
                   echo "env: ${env.BRANCH_NAME}"
                   echo "douruimi-web docker stop...."
                   sh "docker stop douruimi-web-${env.BRANCH_NAME}"


                   echo "douruimi-web bakup...."
                   sh ''' if [ ! -f "/home/data/douruimi/$BRANCH_NAME/app.jar" ];then
                            echo "文件不存在"
                       else
                           mv /home/data/douruimi/$BRANCH_NAME/app.jar  /home/data/douruimi/app-$(date +%Y%m%d%h%m%s).jar
                       fi
                   '''

                   echo "douruimi-web deploying...."
                   sh 'cp ./sys-web/target/drm-web-1.0-SNAPSHOT.jar /home/data/douruimi/$BRANCH_NAME/app.jar'

                   echo "douruimi-web docker start...."
                   sh "docker start douruimi-web-${env.BRANCH_NAME}"
               }
           }
       }
}