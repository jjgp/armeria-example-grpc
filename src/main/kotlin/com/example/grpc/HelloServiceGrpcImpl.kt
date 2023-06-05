package com.example.grpc

import proto.example.grpc.Hello.HelloReply
import proto.example.grpc.Hello.HelloRequest
import proto.example.grpc.HelloServiceGrpcKt.HelloServiceCoroutineImplBase

class HelloServiceGrpcImpl : HelloServiceCoroutineImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloReply =
        HelloReply.newBuilder()
            .setMessage("Hello, ${request.name}!")
            .build()
}
