package io.vertx.example;

import java.util.logging.Logger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;

/**
 * @author Santhosh Kani
 */
public class EventGenerator extends AbstractVerticle {
    private static final Logger LOG = Logger.getLogger(EventGenerator.class.getName());

    /* (non-Javadoc)
     * @see io.vertx.core.AbstractVerticle#start()
     */
    @Override
    public void start() throws Exception {

        final EventBus eb = vertx.eventBus();
        vertx.setPeriodic(3000, new Handler<Long>() {

            @Override
            public void handle(Long event) {
                LOG.info("Publishing a event.");
                eb.publish("message-queue", "Received a new message.");
            }
        });
    }

}
