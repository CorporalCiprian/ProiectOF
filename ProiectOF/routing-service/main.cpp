#include "crow_all.h"
#include <vector> 

int main() {
    crow::SimpleApp app;

    CROW_ROUTE(app, "/calculate-route").methods(crow::HTTPMethod::POST)
    ([](const crow::request& req) {
        auto x = crow::json::load(req.body);
        if (!x) return crow::response(400, "Invalid JSON");

        if (!x.has("startX") || !x.has("startY") || !x.has("endX") || !x.has("endY")) {
             return crow::response(400, "Missing coordinates");
        }

        double startX = x["startX"].d();
        double startY = x["startY"].d();
        double endX = x["endX"].d();
        double endY = x["endY"].d();

        std::vector<crow::json::wvalue> routePoints;

        crow::json::wvalue p1;
        p1["x"] = startX;
        p1["y"] = startY;
        routePoints.push_back(p1);

        crow::json::wvalue p2;
        p2["x"] = (startX + endX) / 2;
        p2["y"] = (startY + endY) / 2;
        routePoints.push_back(p2);

        crow::json::wvalue p3;
        p3["x"] = endX;
        p3["y"] = endY;
        routePoints.push_back(p3);

        crow::json::wvalue response;
        response["route"] = std::move(routePoints);
        response["status"] = "success"; // E bine să ai și un status

        return crow::response(200, response);
    });

    CROW_ROUTE(app, "/health")([]{
        crow::json::wvalue x;
        x["status"] = "OK";
        return x;
    });

    app.bindaddr("0.0.0.0").port(8081).multithreaded().run();
}