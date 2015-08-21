package de.saxsys.tasksapp.server.service.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

import java.util.logging.Logger;

/**
 * Created by andre.krause on 21.08.2015.
 */
public class AnalysisService extends AbstractVerticle {

	private final static Logger log = Logger.getLogger(AnalysisService.class.getName());

	public void start(){
		EventBus eventBus = vertx.eventBus();

		eventBus.consumer("getTaskStatus", msg -> {
			eventBus.send("getTaskStatusFromDB", "", result -> {
				msg.reply(result.result().body().toString());
			});
		});
	}
}
