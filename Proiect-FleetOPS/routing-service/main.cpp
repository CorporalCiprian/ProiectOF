#include "crow_all.h" 

int main() {
    crow::SimpleApp app;

    CROW_ROUTE(app, "/calculate-route").methods(crow::HTTPMethod::POST)
    ([](const crow::request& req) {
        auto x = crow::json::load(req.body);
        if (!x) return crow::response(400);

        double startX = x["startX"].d();
        double startY = x["startY"].d();
        double endX = x["endX"].d();
        double endY = x["endY"].d();

        crow::json::wvalue response;
        response["route"][0] = {{"x", startX}, {"y", startY}};
        response["route"][1] = {{"x", (startX + endX) / 2}, {"y", (startY + endY) / 2}};
        response["route"][2] = {{"x", endX}, {"y", endY}};

        return crow::response(response);
    });

    app.bindaddr("0.0.0.0").port(8081).multithreaded().run();
}