cmake_minimum_required(VERSION 3.10)
project(RoutingService)

set(CMAKE_CXX_STANDARD 17)

find_package(Threads REQUIRED)

add_executable(routing_service main.cpp)
target_link_libraries(routing_service Threads::Threads)