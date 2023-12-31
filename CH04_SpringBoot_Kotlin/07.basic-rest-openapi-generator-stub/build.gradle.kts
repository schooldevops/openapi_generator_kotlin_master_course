import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"

//	openapi generator 관련 플러그인 추가
	id("org.openapi.generator") version "6.5.0"
}

group = "com.schooldevops.kotlin"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

// spring cloud를 위한 의존성 리포지토리 설정
dependencyManagement {

	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2021.0.4")
	}
	imports {
		mavenBom("io.awspring.cloud:spring-cloud-aws-dependencies:2.4.4")
	}
}

dependencies {

//	FeignClient용
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.0.1")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

//	openapi generator 관련 의존성 추가
	implementation("org.springdoc:springdoc-openapi-ui:1.7.0") // SpringDoc OpenAPI UI
	implementation("org.springdoc:springdoc-openapi-data-rest:1.7.0") // SpringDoc OpenAPI Data REST
	implementation("io.springfox:springfox-boot-starter:3.0.0")
	implementation("io.springfox:springfox-swagger-ui:3.0.0")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6") // Optional: 널 가능한 타입을 처리하기 위해서 Jackson module을 추가
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	implementation("javax.validation:validation-api:2.0.1.Final")
	compileOnly("javax.servlet:javax.servlet-api:4.0.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootBuildImage {
	builder.set("paketobuildpacks/builder-jammy-base:latest")
}

// openapi generator 태스크 지정
openApiGenerate {
	generatorName = "spring"
	inputSpec = "$rootDir/src/main/resources/openapi/openapi-spec.yaml"
	apiPackage = "com.schooldevops.kotlin.basicrest.api"
	modelPackage = "com.schooldevops.kotlin.basicrest.model"
	configOptions = mapOf(
		"interfaceOnly" to "true",
		"dateLibrary" to "java8",
		"useTags" to "true"
	)
}

tasks {
	register("openApiGenerateStub", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
		generatorName.set("spring")
//		inputSpec = "$rootDir/src/main/resources/openapi/openapi-spec.yaml"
		inputSpec.set("src/main/resources/openapi/openapi-spec.yaml")
		outputDir.set("${buildDir}/generate-resources/main")
		apiPackage = "com.schooldevops.kotlin.basicrest.feign"
		modelPackage = "com.schooldevops.kotlin.basicrest.model"
		library = "spring-cloud"
		configOptions.set(mapOf(
			"interfaceOnly" to "true",
			"dateLibrary" to "java8",
			"useTags" to "true"
		))
	}
}

sourceSets.main {
	java.srcDirs("src/main/kotlin", "build/generate-resources/main/src/main/java")
}