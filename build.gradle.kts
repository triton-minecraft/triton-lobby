plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "dev.kyriji"
version = ""

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:9803f2bfe3")
    compileOnly("dev.kyriji:triton-stom:0.0.0")
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "dev.kyriji.lobby.TritonLobby"
            attributes["Module-Name"] = "triton-lobby"
            attributes["Module-Dependencies"] = "minestom-dependencies,triton-core"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
        archiveClassifier.set("")
    }
}
