import com.google.protobuf.gradle.id

group = "org.example"
version = "1.0-SNAPSHOT"

val armeriaVersion by extra("1.22.1")
val grpcVersion by extra("1.55.1")
val grpcKotlinVersion by extra("1.3.0")
val protobufVersion by extra("3.23.2")
val slf4jVersion by extra("1.7.36")

plugins {
    kotlin("jvm") version "1.8.21"
    application
    id("com.google.protobuf") version "0.9.3"
}

apply(plugin = "com.google.protobuf")

repositories {
    mavenCentral()
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
    }

    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                it.generateDescriptorSet = true
                it.descriptorSetOptions.includeImports = true

                it.builtins {
                    id("kotlin")
                }

                it.plugins {
                    id("grpc")
                    id("grpckt")
                }
            }
        }
    }
}

dependencies {
    implementation(platform("com.linecorp.armeria:armeria-bom:$armeriaVersion"))
    implementation("com.linecorp.armeria:armeria-grpc:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria:$armeriaVersion}")

    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("com.google.protobuf:protobuf-kotlin:$protobufVersion")

    runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")

    testImplementation(kotlin("test"))
    testImplementation("com.linecorp.armeria:armeria-junit5:$armeriaVersion")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.example.grpc.MainKt")
}