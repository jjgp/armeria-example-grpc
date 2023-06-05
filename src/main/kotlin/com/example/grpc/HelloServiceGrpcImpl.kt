package com.example.grpc

import com.example.grpc.Hello.HelloReply
import com.example.grpc.Hello.HelloRequest

class HelloServiceGrpcImpl : HelloServiceGrpcKt.HelloServiceCoroutineImplBase() {
    override suspend fun sayHello(request: HelloRequest): HelloReply =
        HelloReply.newBuilder()
            .setMessage("Hello, ${request.name}!")
            .build()
}