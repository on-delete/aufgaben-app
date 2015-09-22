package de.saxsys.tasksapp.server;

import de.saxsys.tasksapp.server.service.task.AnalysisService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by andre.krause on 12.08.2015.
 */
public class AnalysisServiceServer extends AbstractVerticle {

	private EventBus eventBus;

	public void start() {
		eventBus = vertx.eventBus();
		HttpServer httpServer = vertx.createHttpServer();
		Router router = Router.router(vertx);

		router.route().method(HttpMethod.POST).method(HttpMethod.PUT).handler(BodyHandler.create());

		router.route(HttpMethod.GET, "/getTaskStatus").handler(routingContext -> eventBus.send("getTaskStatus", "", result -> sendResponseWithBody(result, routingContext)));

		httpServer.requestHandler(router::accept).listen(3421);

		vertx.deployVerticle(new AnalysisService());
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
}
