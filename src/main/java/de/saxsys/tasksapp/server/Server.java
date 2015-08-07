package de.saxsys.tasksapp.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * Created by andre.krause on 06.08.2015.
 */
public class Server extends AbstractVerticle{

	public void start(){
		HttpServer httpServer = vertx.createHttpServer();
		Router router = Router.router(vertx);
		router.get("/tasksapp/*").handler(routingContext ->  {
			routingContext.response().sendFile(getClass().getClassLoader().getResource("webapp/index.html").getFile());
		});
		router.get("/auswertung/*").handler(routingContext -> {
			routingContext.response().sendFile(getClass().getClassLoader().getResource("webapp/auswertung.html").getFile());
		});

		httpServer.requestHandler(router::accept).listen(8080);
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}
}
