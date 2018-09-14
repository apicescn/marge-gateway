node('jenkins-slave') {
      stage('Prepare') {
            withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'gitlab',
              usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                  sh 'rm -rf marge-cloud'
                  sh "git clone http://${USERNAME}:${PASSWORD}@xx.songshu.com/szss/marge-gateway.git"
            }
      }

      stage('mvn clean and package') {
            sh 'ls'
            dir('./marge-cloud') {
                  sh "git branch -a"
                  sh "git checkout -b feature-v1.0 origin/feature-v1.0"
                  sh 'chmod a+x ./mvnw'
                  sh './mvnw -version'
                  sh './mvnw clean package -DskipTests=true -Pprod'
            }
      }

      stage('image build') {
            dir('./marge-cloud') {
                    def modules = "${module}".split(",")
                    for(def path : modules){
                            path = path.replaceAll("\"","")
                            dir(path) {
                                    sh 'chmod a+x ./mvnw'
                                    sh './mvnw docker:build'
                                    def name = path.split("/")
                                    def dockerImageName = name[name.length-1]
                                    def pom = readMavenPom file: 'pom.xml'
                                    def version = pom.version
                                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'aliyun-registry',
                                            usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                                            sh "sudo docker login -u ${USERNAME} -p ${PASSWORD} registry.cn-songshu.aliyuncs.com"
                                            sh "sudo docker tag szss-containers/${dockerImageName}:latest registry.cn-songshu.aliyuncs.com/szss-containers/${dockerImageName}:${version}-${BUILD_NUMBER}"
                                            sh "sudo docker push registry.cn-songshu.aliyuncs.com/szss-containers/${dockerImageName}:${version}-${BUILD_NUMBER}"
                                            sh "sudo docker rmi szss-containers/${dockerImageName}:latest"
                                            sh "sudo docker rmi registry.cn-songshu.aliyuncs.com/szss-containers/${dockerImageName}:${version}-${BUILD_NUMBER}"
                                    }

                            }
                    }
            }
      }
}

