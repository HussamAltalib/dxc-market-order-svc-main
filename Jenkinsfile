pipeline {
    agent any
    tools {
        maven "Maven3"
        jdk "OpenJDK11"
    }

    environment {

        SERVICE = "order"
        REVISION = "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}"
        // Change this to your service name
        PROJECT_NAME = "dxc-market-${SERVICE}-svc"

        // Change this to your service name
        // Make sure you change the name of the repo in nexus as well
        NEXUS_REPO_NAME = "dxc-market-repo"

        NEXUS_REPO_URL = "	http://192.168.56.43:8081/repository/dxc-market-repo/com/dxc/"

        // A repo urls are valid here
        // e.g. REPOSITORY = "https://github.dxc.com/ksa-grads/dxc-market-refund-svc"
        REPOSITORY = scm.getUserRemoteConfigs()[0].getUrl()

        // Docker Hub repository information
        DOCKER_HUB_REGISTRY = "mhmd1492" // Replace with your Docker Hub username
        DOCKER_IMAGE_NAME = "dxc-market" // Replace with your image name
        DOCKER_IMAGE_TAG = "${SERVICE}-${env.BUILD_ID}-${env.BUILD_TIMESTAMP}"
    }

    stages {    
        stage("Fetch Code") {
            steps {
                git branch: 'main',
                credentialsId: 'github-cred',
                url: "${REPOSITORY}"
            }
        }

        stage("Build") {
            steps {
                sh "mvn clean install -DskipTests"
            }

            post {
                success {
                    echo "Archiving artifact..."
                    archiveArtifacts artifacts: "**/*.jar"
                }
            }
        }

        stage("Unit Tests") {
            steps {
                sh "mvn test"
            }
        }

        stage("CheckStyle Analysis") {
            steps {
                sh "mvn checkstyle:checkstyle && mvn jacoco:report"
            }
        }

        stage("SonarQube Analysis") {
            environment {
                scannerHome = tool "Sonar4.7"
            }

            steps {
                withSonarQubeEnv("Sonar") {
                    sh """${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=market-${SERVICE} \
                   -Dsonar.projectName=${PROJECT_NAME} \
                   -Dsonar.projectVersion=${REVISION} \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=target/test-classes/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml"""
                }
            }
        }

        stage("SonarQube Quality Gate") {
            steps {
                timeout(time: 1, unit: "HOURS") {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage("Nexus Upload Artifact") {
            steps {
                // nexusArtifactUploader(
                //     nexusVersion: "nexus3",
                //     protocol: "http",
                //     nexusUrl: "192.168.56.43:8081",
                //     groupId: "QA",
                //     version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                //     repository: "${NEXUS_REPO_NAME}",
                //     credentialsId: "nexus-cred",
                //     // Change the details here as appropriate
                //     artifacts: [
                //         [artifactId: "dxc-market-refund-svc",
                //         classifier: "",
                //         file: "target/dxc-market-refund-svc-1.0.jar",
                //         type: "jar"]
                //     ]
                // )
                script {
                    // Configure Maven to use the provided settings.xml file  
                     withMaven(
                        // Maven settings.xml file defined with the Jenkins Config File Provider Plugin
                        // We recommend to define Maven settings.xml globally at the folder level using
                        // navigating to the folder configuration in the section "Pipeline Maven Configuration / Override global Maven configuration"
                        // or globally to the entire master navigating to  "Manage Jenkins / Global Tools Configuration"
                        mavenSettingsConfig: '175e3d83-a5f8-4eb1-9123-88cb2ea26dac' // (3)
                    ) {
                    // Run the maven build
                        sh "mvn deploy -Drevision=${REVISION} -DskipTests"
                        // sh "mvn deploy:deploy-file -DgroupId=com.dxc -DartifactId=dxc-market-refund-svc   -Dpackaging=jar -Dname='DXC Market Refund Service' -Ddescription='DXC Market is an API that manages a market consisting of customers, orders, and products' -Dfile=target/dxc-market-refund-svc-1.0.jar -Durl=http://192.168.56.43:8081/repository/dxc-market-repo -DrepositoryId=dxc-market-refund-svc-repo -DgeneratePom=true"                }

                    }
                }
            }
                
        }

        stage("Build Docker Image") {
            steps {
                script {
                    // Build the Docker image from the Dockerfile
                    def customImage = docker.build(
                        "${DOCKER_HUB_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}",
                        "--build-arg='SERVICE=${SERVICE}' --build-arg='REVISION=${REVISION}' --build-arg='PROJECT_NAME=${PROJECT_NAME}' --build-arg='NEXUS_REPO_URL=${NEXUS_REPO_URL}' -f Dockerfile ."
                    )       
                    
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-cred'){
                        customImage.push()
                    }
                }
            }
        } 

        stage("Deploy to K8 Cluster") {
            steps {
                script {
                    // def serviceName = $ // Replace with your service name
                    def podDefinition = """
apiVersion: v1
kind: Pod
metadata: 
  name: ${SERVICE}-pod-svc
  labels: 
    name: ${SERVICE}-pod-svc
    app: dxc-market
    type: java-app
spec: 
  containers: 
  - name: ${SERVICE}
    image: ${DOCKER_HUB_REGISTRY}/${DOCKER_IMAGE_NAME}:${DOCKER_IMAGE_TAG}
    ports: 
    - containerPort: 8080
    env: 
    - name: DB_URL
      value: "jdbc:mysql://10.106.33.86:3306/dxc_market_db"
    volumeMounts:
    - name: logs-volume
      mountPath: /app/logs
  volumes:
  - name: logs-volume
    emptyDir: {}
  imagePullSecrets:
  - name: regcred
        """

                    // Create a temporary file for the pod definition
                    writeFile(file: 'pod-def.yaml', text: podDefinition)

                    // Check if the pod exists
                    def podExists = sh(script: "kubectl get pod | grep -q ${SERVICE}-pod-svc", returnStatus: true)

                    if (podExists == 0) {
                        // If the pod exists, delete it
                        sh "kubectl delete pod ${SERVICE}-pod-svc"
                        echo "Pod ${SERVICE}-pod-svc deleted."
                    }

                    // Create the pod
                    sh "kubectl create -f pod-def.yaml"

                    echo "Pod ${SERVICE}-pod-svc created."
                }
            }
        }
    }
}