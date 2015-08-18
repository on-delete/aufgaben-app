package de.saxsys.tasksapp.server;

import de.saxsys.tasksapp.server.service.InitDatabaseVerticle;
import de.saxsys.tasksapp.server.service.task.TaskService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by andre.krause on 06.08.2015.
 */
public class TasksServiceServer extends AbstractVerticle {

    private EventBus eventBus;

    public void start() {
        eventBus = vertx.eventBus();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route().method(HttpMethod.POST).method(HttpMethod.PUT).handler(BodyHandler.create());

        router.route(HttpMethod.POST, "/addTask").handler(routingContext -> {
            JsonObject request = routingContext.getBodyAsJson();
            eventBus.send("addTask", request, result -> sendResponseWithBody(result, routingContext));
        });

        router.route(HttpMethod.GET, "/getAllTasks").handler(routingContext -> eventBus.send("getAllTasks", "", result -> sendResponseWithBody(result, routingContext)));

        router.route(HttpMethod.PUT, "/updateTaskStatus").handler(routingContext -> {
            JsonObject request = routingContext.getBodyAsJson();
            eventBus.send("updateTaskStatus", request, result -> sendResponseWithoutBody(result, routingContext));
        });

        httpServer.requestHandler(router::accept).listen(3420);
    }

    private void sendResponseWithoutBody(AsyncResult<Message<Object>> result, RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/plain");
        if (result.failed()) {
            response.setStatusCode(500);
            response.end("Internal Error!");
        } else {
            response.setStatusCode(200);
            response.end("Success!");
        }
    }

    private void sendResponseWithBody(AsyncResult<Message<Object>> result, RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        if (result.failed()) {
            response.putHeader("content-type", "text/plain");
            response.setStatusCode(500);
            response.end("Internal Error!");
        } else {
            String resultBody = result.result().body().toString();
            response.putHeader("content-type", "application/json");
            response.setStatusCode(200);
            response.end(resultBody);
        }
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new InitDatabaseVerticle());
        vertx.deployVerticle(new TasksServiceServer());
        vertx.deployVerticle(new TaskService());
    }
}
