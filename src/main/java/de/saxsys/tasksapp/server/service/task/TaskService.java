package de.saxsys.tasksapp.server.service.task;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;

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
								msg.reply("{\"id\":\"" + rs.getResults().get(0).getList().get(0).toString() + "\"}");
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
								JsonObject resultObject = new JsonObject();
								JsonArray array = new JsonArray();

								rs.getResults().forEach(resultSet -> {
									JsonObject tempResult = new JsonObject();
									tempResult.put("id", resultSet.getList().get(0).toString());
									tempResult.put("title", resultSet.getList().get(1).toString());
									tempResult.put("status", resultSet.getList().get(2).toString());
									array.add(tempResult);
								});

								resultObject.put("tasks", array);

								msg.reply(resultObject.toString());
							});
					}
				});
			} catch (RuntimeException e) {
				log.log(Level.WARNING, "Internal Error " + e.getMessage());
				e.printStackTrace();
				msg.fail(500, "");
			}
		});
		eventBus.consumer("updateTaskStatus", msg -> {
			try {
				JsonObject transferObject = (JsonObject) msg.body();

				client.getConnection(conn -> {
					if (conn.failed()) {
						log.log(Level.WARNING, "Connection to database failed + " + conn.cause().getMessage());
						msg.fail(500, "");
					} else {
						update(conn.result(), "UPDATE task SET t_status = " + transferObject.getString("status") + " WHERE t_id=" + transferObject.getString("taskId") + ";", create -> msg.reply(""));
					}
				});
			} catch (RuntimeException e) {
				log.log(Level.WARNING, "Internal Error " + e.getMessage());
				e.printStackTrace();
				msg.fail(500, "");
			}
		});
		eventBus.consumer("deleteTask", msg -> {
			try {
				String taskId = msg.body().toString();

				client.getConnection(conn -> {
					if (conn.failed()) {
						log.log(Level.WARNING, "Connection to database failed + " + conn.cause().getMessage());
						msg.fail(500, "");
					} else {
						update(conn.result(), "DELETE FROM task WHERE t_id=" + taskId +";", create -> msg.reply(""));
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

	private void update(SQLConnection conn, String sql, Handler<UpdateResult> done){
		conn.update(sql, res -> {
			if (res.failed()) {
				throw new RuntimeException(res.cause());
			}

			done.handle(res.result());
		});
	}
}
