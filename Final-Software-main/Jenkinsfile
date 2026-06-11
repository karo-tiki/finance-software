pipeline {
    agent any

    tools {
        maven 'Maven'
        jdk 'JDK11'
    }

    stages {

        stage('1. Construcción Automática') {
            steps {
                echo 'Compilando y empaquetando el proyecto...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('2. Análisis Estático - SonarQube') {
            steps {
                echo 'Analizando calidad del código con SonarQube...'
                sh 'mvn sonar:sonar -Dsonar.host.url=http://localhost:9000'
            }
        }

        stage('3. Pruebas Unitarias - JUnit5 + Mockito') {
            steps {
                echo 'Ejecutando pruebas unitarias...'
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: 'target/jacoco.exec',
                        classPattern: 'target/classes',
                        sourcePattern: 'backend/src/main/java'
                    )
                }
            }
        }

        stage('4. Pruebas Funcionales - Selenium') {
            steps {
                echo 'Ejecutando pruebas funcionales con Selenium...'
                sh 'mvn verify -Pintegration-tests'
            }
        }

        stage('5. Pruebas de Performance - JMeter') {
            steps {
                echo 'Ejecutando pruebas de rendimiento con JMeter...'
                sh 'mvn jmeter:jmeter'
            }
        }

        stage('6. Pruebas de Seguridad - OWASP ZAP') {
            steps {
                echo 'Ejecutando pruebas de seguridad con OWASP ZAP...'
                sh 'mvn zap:analyze'
            }
        }

        stage('7. Despliegue - Docker') {
            steps {
                echo 'Construyendo imagen Docker...'
                sh 'docker build -t finance-software:latest .'
                echo 'Desplegando contenedor...'
                sh 'docker run -d -p 8080:8080 finance-software:latest'
            }
        }
    }

    post {
        success {
            echo 'Pipeline ejecutado exitosamente!'
        }
        failure {
            echo 'Pipeline falló. Revisar logs.'
        }
    }
}
