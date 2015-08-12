package de.saxsys.tasksapp.server.service;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * Created by andre.krause on 12.08.2015.
 */
public class InitDatabaseVerticle extends AbstractVerticle {

	public void start(){
		final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
				.put("url", "jdbc:hsqldb:file:db/taskDB")
				.put("driver_class", "org.hsqldb.jdbcDriver")
				.put("max_pool_size", 30), "taskDB");

		client.getConnection(conn -> {
			if (conn.failed()) {
				System.err.println(conn.cause().getMessage());
				return;
			}

			conn.result().execute("create sequence t_id_squence", voidAsyncResult -> {
			});
			conn.result().execute("create table task(t_id bigint GENERATED BY DEFAULT AS SEQUENCE t_id_squence PRIMARY KEY, t_title varchar(255) NOT NULL, t_status BOOLEAN NOT NULL);", res->{
				if(!res.succeeded()){
					System.out.println(res.cause().getMessage());
				}
			});
		});
	}
}