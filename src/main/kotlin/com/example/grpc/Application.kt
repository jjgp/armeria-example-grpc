package com.example.grpc

import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.Server
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.grpc.GrpcService
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import proto.example.grpc.Hello.HelloRequest
import proto.example.grpc.HelloServiceGrpc

class Application {
    companion object {
        @JvmStatic
        val logger: Logger = LoggerFactory.getLogger(Application::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            val exampleRequest = HelloRequest.newBuilder().setName("World").build()
            val grpcService = grpcService()
            val server: Server = Server.builder()
                .http(8080)
                .https(8443)
                .tlsSelfSigned()
                .service(grpcService)
                .service("prefix:/prefix", grpcService)
                .serviceUnder(
                    "/docs",
                    DocService.builder()
                        .exampleRequests(HelloServiceGrpc.SERVICE_NAME, "Hello", exampleRequest)
                        .exclude(
                            DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME)
                        )
                        .build()
                )
                .build()

            server.closeOnJvmShutdown()
            server.start().join()

            logger.info("Server has been started. Serving DocService at http://127.0.0.1:${server.activeLocalPort()}/docs")
        }

        private fun grpcService(): GrpcService =
            GrpcService.builder()
                .addService(HelloServiceGrpcImpl())
                .addService(ProtoReflectionService.newInstance())
                .enableUnframedRequests(true)
                .supportedSerializationFormats(GrpcSerializationFormats.values())
                .build()
    }
}
