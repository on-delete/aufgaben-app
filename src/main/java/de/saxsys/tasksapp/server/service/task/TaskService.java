package de.saxsys.tasksapp.server.service.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by andre.krause on 12.08.2015.
 */
public class TaskService extends AbstractVerticle{

	private final static Logger log = Logger.getLogger(TaskService.class.getName());

	public void start() {
		final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
				.put("url", "jdbc:hsqldb:file:db/taskDB")
				.put("driver_class", "org.hsqldb.jdbcDriver")
				.put("max_pool_size", 30), "taskDB");

		EventBus eventBus = vertx.eventBus();

		eventBus.consumer("addTask", msg -> {
			try {
				JsonObject transferObject = (JsonObject) msg.body();

				client.getConnection(conn -> {
					if (conn.failed()) {
						log.log(Level.WARNING, "Connection to database failed + " + conn.cause().getMessage());
						msg.fail(500, "");
					} else {
						execute(conn.result(), "INSERT INTO task VALUES (NEXT VALUE FOR t_id_squence, '"+transferObject.getString("task")+"', false);", create -> {
							query(conn.result(), "SELECT TOP 1 t_id FROM task ORDER BY t_id DESC", rs -> {
								log.info(rs.getResults().get(0).getList().get(0).toString());
								msg.reply("{\"id\":\""+rs.getResults().get(0).getList().get(0).toString()+"\"}");
								client.close();
							});
						});
					}
				});
			} catch (RuntimeException e) {
				log.log(Level.WARNING, "Internal Error " + e.getMessage());
				e.printStackTrace();
				msg.fail(500, "");
			}
		});
		eventBus.consumer("getAllTasks", msg -> {
			try {
				client.getConnection(conn -> {
					if (conn.failed()) {
						log.log(Level.WARNING, "Connection to database failed + " + conn.cause().getMessage());
						msg.fail(500, "");
					} else {
							query(conn.result(), "SELECT * FROM task", rs -> {
								log.info(rs.getResults().get(0).getList().get(0).toString());
								msg.reply("{\"id\":\"" + rs.getResults().get(0).getList().get(0).toString() + "\"}");
								client.close();
							});
					}
				});
			} catch (RuntimeException e) {
				log.log(Level.WARNING, "Internal Error " + e.getMessage());
				e.printStackTrace();
				msg.fail(500, "");
			}
		});
	}
	private void execute(SQLConnection conn, String sql, Handler<Void> done) {
		conn.execute(sql, res -> {
			if (res.failed()) {
				throw new RuntimeException(res.cause());
			}

			done.handle(null);
		});
	}

	private void query(SQLConnection conn, String sql, Handler<ResultSet> done) {
		conn.query(sql, res -> {
			if (res.failed()) {
				throw new RuntimeException(res.cause());
			}

			done.handle(res.result());
		});
	}

}
